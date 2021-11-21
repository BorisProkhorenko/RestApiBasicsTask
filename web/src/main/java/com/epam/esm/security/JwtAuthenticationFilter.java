package com.epam.esm.security;

import com.epam.esm.exceptions.InvalidRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
    private static final String EMPTY = "";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);
        String authToken = getToken(header);
        String username = getUsername(authToken);
        setAuthentication(username,authToken,req);

        chain.doFilter(req, res);
    }


    private String getToken(String header) {
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            return header.replace(TOKEN_PREFIX, EMPTY);
        } else {
            return EMPTY;
        }
    }

    private String getUsername(String authToken) {
        if (authToken.isEmpty()) {
            return EMPTY;
        }
        try {
            return jwtTokenUtil.getUsernameFromToken(authToken);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("an error occured during getting username from token, cause:"
                    + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new InvalidRequestException("the token is expired and not valid anymore");
        }
    }

    private void setAuthentication(String username, String authToken, HttpServletRequest req) {
        if (!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthentication
                        (authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    }
}