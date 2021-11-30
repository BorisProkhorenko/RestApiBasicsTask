package com.epam.esm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Value("${security.jwt.client-id}")
    private String clientId;

    @Value("${security.jwt.client-id.write}")
    private String clientWriteId;

    @Value("${security.jwt.client-secret}")
    private String clientSecret;


    @Value("${security.signing-key}")
    private String signingKey;
    @Value("${security.verifier-key}")
    private String verifierKey;

    private final static String SCOPE_READ = "read";
    private final static String SCOPE_WRITE = "write";
    private final static String SCOPE_TRUST = "trust";
    private final static String TOKEN_KEY = "permitAll()";
    private final static String TOKEN_CHECK = "isAuthenticated()";

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(verifierKey);
        converter.setSigningKey(signingKey);
        return converter;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore())
                .accessTokenConverter(tokenEnhancer());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess(TOKEN_KEY).checkTokenAccess(TOKEN_CHECK);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient(clientId).secret(encoder.encode(clientSecret))
                .scopes(SCOPE_READ, SCOPE_TRUST).authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(20000).and().withClient(clientWriteId).secret(encoder.encode(clientSecret))
                .scopes(SCOPE_READ, SCOPE_TRUST,SCOPE_WRITE).authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(20000);

    }

}