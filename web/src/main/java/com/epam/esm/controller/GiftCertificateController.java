package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateDtoMapper;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This Controller provides public API for operations with {@link GiftCertificate} entity.
 * Uses {@link GiftCertificateService} to access Data Base through business-logic layer.
 * Uses {@link ObjectMapper} to map objects from JSON
 *
 * @author Boris Prokhorenko
 * @see GiftCertificateService
 * @see GiftCertificate
 * @see ObjectMapper
 */
@RestController
@RequestMapping(value = "/certificates")
public class GiftCertificateController {

    private final GiftCertificateService service;
    private final ObjectMapper objectMapper;
    private final GiftCertificateDtoMapper dtoMapper;

    public GiftCertificateController(GiftCertificateService service, ObjectMapper objectMapper,
                                     GiftCertificateDtoMapper dtoMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Method allows getting {@link GiftCertificate} from DB by its id
     *
     * @param id - primary key to search {@link GiftCertificate} entity object in DB
     * @return {@link GiftCertificateDto} DTO of entity object from DB
     */
    @GetMapping(value = "/{id}")
    public GiftCertificateDto getCertificateById(@PathVariable Long id) {

        return dtoMapper.toDto(service.getCertificateById(id));
    }

    /**
     * Method allows getting all {@link GiftCertificate} entity objects from DB
     *
     * @return {@link List} of {@link GiftCertificateDto} DTO of entity objects from DB
     */
    @GetMapping
    public List<GiftCertificateDto> getAllCertificates() {
        return service.getAllCertificates()
                .stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Method allows creating a new {@link GiftCertificate} entity object in DB
     *
     * @param json - tag object to map from request body
     * @return {@link GiftCertificateDto} DTO of object, which you created
     */
    @PostMapping
    public GiftCertificateDto createCertificate(@RequestBody String json) {
        try {
            GiftCertificate certificate = objectMapper.readValue(json, GiftCertificate.class);
            if (certificate.getName() == null || certificate.getDescription() == null
                    || certificate.getPrice() == null || certificate.getDuration() == null) {
                throw new InvalidRequestException("Empty field");
            }
            return dtoMapper.toDto( service.createCertificate(certificate));
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

    /**
     * Method allows deleting {@link GiftCertificate} from DB by its id
     *
     * @param id - primary key to search {@link GiftCertificateDto} DTO of entity object in DB
     */
    @DeleteMapping(value = "/{id}")
    public void deleteCertificate(@PathVariable Long id) {
        service.deleteCertificate(id);
    }


    /**
     * Method allows updating {@link GiftCertificate} info in DB by its id
     *
     * @param json - tag object to map from request body
     * @return {@link ResponseEntity} ok if update succeeded and INTERNAL_SERVER_ERROR if not
     */
    @PutMapping(consumes = "application/json")
    public GiftCertificateDto updateCustomer(@RequestBody String json) {
        try {
            GiftCertificate certificate = objectMapper.readValue(json, GiftCertificate.class);
            if (certificate.getId() == 0) {
                throw new InvalidRequestException("id = 0");
            }
            return dtoMapper.toDto(service.updateCertificate(certificate));
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

    /**
     * Method allows getting {@link GiftCertificate} objects from db filtered and/or sorted
     *
     * @param tagId            - if presents filtering by tag id will be applied
     * @param part             - if presents filtering by part of name and description will be applied
     * @param nameSort         - if presents sorting(ASC/DESC) by name will be applied. If nameSort param
     *                         is different from "asc" or "desc" in ignored case sort will not be applied.
     * @param descriptionSort- if presents sorting(ASC/DESC) by name will be applied. If nameSort param
     *                         *                 is different from "asc" or "desc" in ignored case sort will not be applied.
     * @return @return {@link List} of {@link GiftCertificateDto} DTO of entity objects from DB
     */
    @GetMapping(value = {"/params"})
    public List<GiftCertificateDto> getCertificatesWithParams(@RequestParam("tagId") Optional<String> tagId,
                                                           @RequestParam("part") Optional<String> part,
                                                           @RequestParam("nameSort") Optional<String> nameSort,
                                                           @RequestParam("descriptionSort") Optional<String> descriptionSort) {
        return service.getCertificatesWithParams(tagId, part,
                nameSort, descriptionSort)
                .stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());
    }


}
