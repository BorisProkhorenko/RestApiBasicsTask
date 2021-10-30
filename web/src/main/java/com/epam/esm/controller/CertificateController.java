package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateDtoMapper;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.Certificate;
import com.epam.esm.service.CertificateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This Controller provides public API for operations with {@link Certificate} entity.
 * Uses {@link CertificateService} to access Data Base through business-logic layer.
 * Uses {@link ObjectMapper} to map objects from JSON
 *
 * @author Boris Prokhorenko
 * @see CertificateService
 * @see Certificate
 * @see ObjectMapper
 */
@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {

    private final CertificateService service;
    private final ObjectMapper objectMapper;
    private final CertificateDtoMapper dtoMapper;
    private final static String DELIMITER = ",";

    public CertificateController(CertificateService service, ObjectMapper objectMapper,
                                 CertificateDtoMapper dtoMapper, Jdk8Module jdk8Module) {
        this.service = service;
        this.dtoMapper = dtoMapper;
        objectMapper.registerModule(jdk8Module);
        this.objectMapper = objectMapper;
    }

    /**
     * Method allows getting {@link Certificate} from DB by its id
     *
     * @param id - primary key to search {@link Certificate} entity object in DB
     * @return {@link CertificateDto} DTO of entity object from DB
     */
    @GetMapping(value = "/{id}")
    public CertificateDto getCertificateById(@PathVariable Long id) {

        return dtoMapper.toDto(service.getCertificateById(id));
    }


    /**
     * Method allows creating a new {@link Certificate} entity object in DB
     *
     * @param json - tag object to map from request body
     * @return {@link CertificateDto} DTO of object, which you created
     */
    @PostMapping(consumes = "application/json")
    public CertificateDto createCertificate(@RequestBody String json) {
        try {
            Certificate certificate = objectMapper.readValue(json, Certificate.class);
            if (certificate.getName() == null || certificate.getDescription() == null
                    || certificate.getPrice() == null || certificate.getDuration() == null) {
                throw new InvalidRequestException("Empty field");
            }
            return dtoMapper.toDto(service.createCertificate(certificate));
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

    /**
     * Method allows deleting {@link Certificate} from DB by its id
     *
     * @param id - primary key to search {@link CertificateDto} DTO of entity object in DB
     */
    @DeleteMapping(value = "/{id}")
    public void deleteCertificate(@PathVariable Long id) {
        service.deleteCertificate(id);
    }


    /**
     * Method allows updating {@link Certificate} info in DB
     *
     * @param json - tag object to map from request body
     * @return {@link CertificateDto}  - dto of updated entity
     */
    @PutMapping(consumes = "application/json")
    public CertificateDto updateCustomer(@RequestBody String json) {
        try {
            Certificate certificate = objectMapper.readValue(json, Certificate.class);
            if (certificate.getId() == 0) {
                throw new InvalidRequestException("id = 0");
            }
            return dtoMapper.toDto(service.updateCertificate(certificate));
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

    /**
     * Method allows getting {@link Certificate} objects from db filtered and/or sorted
     *
     * @return @return {@link List} of {@link CertificateDto} DTO of entity objects from DB
     */
    @GetMapping
    public List<CertificateDto> getAllCertificates(@RequestParam(name = "limit") Optional<Integer> limit,
                                                          @RequestParam(name = "offset") Optional<Integer> offset,
                                                          @RequestParam(name = "filter_by_tags") Optional<String> tags,
                                                          @RequestParam(name = "filter_by_part") Optional<String> part,
                                                          @RequestParam(name = "sort_by_name") Optional<String> name,
                                                          @RequestParam(name = "sort_by_date") Optional<String> date) {

        Set<Long> tagIdSet = new HashSet<>();
        if (tags.isPresent()) {
            tagIdSet = parseTagIdParam(tags.get());
        }

        return service.getAllCertificates(tagIdSet, part,
                        name, date, limit, offset)
                .stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());


    }

    private Set<Long> parseTagIdParam(String tagIdParam) {
        String[] params = tagIdParam.split(DELIMITER);
        return Arrays.stream(params)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }


}
