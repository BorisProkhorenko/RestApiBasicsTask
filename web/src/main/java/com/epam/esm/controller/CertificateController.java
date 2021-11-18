package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateDtoMapper;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.Certificate;
import com.epam.esm.service.CertificateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * This Controller provides public API for operations with {@link Certificate} entity.
 * Uses {@link CertificateService} to access Data Base through business-logic layer.
 * Uses {@link ObjectMapper} to map objects from JSON
 * Uses {@link CertificateDtoMapper} to map objects from Entity to Dto
 * Uses {@link PaginatedController} for pagination
 *
 * @author Boris Prokhorenko
 * @see CertificateService
 * @see CertificateDto
 * @see Certificate
 * @see CertificateDto
 * @see CertificateDtoMapper
 * @see ObjectMapper
 * @see PaginatedController
 */
@RestController
@RequestMapping(value = "/certificates")
public class CertificateController extends PaginatedController<CertificateController, CertificateDto, Certificate> {

    private final CertificateService service;
    private final ObjectMapper objectMapper;
    private final CertificateDtoMapper dtoMapper;

    private final static String DELIMITER = ",";
    private final static String CERTIFICATES = "certificates";

    public CertificateController(CertificateService service, ObjectMapper objectMapper,
                                 CertificateDtoMapper dtoMapper, Jdk8Module jdk8Module) {
        super(service);
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
        CertificateDto certificate = dtoMapper.toDto(service.getById(id));
        buildCertificateLinks(certificate);
        return certificate;
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
            CertificateDto certificateDto = dtoMapper.toDto(service.create(certificate));
            buildCertificateLinks(certificateDto);
            return certificateDto;
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
        service.delete(new Certificate(id));
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
            CertificateDto certificateDto = dtoMapper.toDto(service.update(certificate));
            buildCertificateLinks(certificateDto);
            return certificateDto;
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

    @Override
    public CollectionModel<CertificateDto> getAll(@RequestParam(name = "page") Optional<Integer> page,
                                                  @RequestParam(name = "size") Optional<Integer> size) {
        return getAll(page, size, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * Method allows getting {@link Certificate} objects from db filtered and/or sorted
     *
     * @param page - page of displayed dto objects
     * @param size - count of displayed dto objects
     * @param tags - filtering by tags id (id numbers must be separated by delimiter "," without any spaces)
     * @param part - filtering by part of certificate name or description
     * @param name - sorting by name(asc/desc)
     * @param date - sorting by date of the last update(asc/desc)
     * @return @return {@link CollectionModel} of {@link CertificateDto} DTO of entity objects from DB
     */
    @Override
    @GetMapping(produces = {"application/hal+json"})
    public CollectionModel<CertificateDto> getAll(@RequestParam(name = "page") Optional<Integer> page,
                                                  @RequestParam(name = "size") Optional<Integer> size,
                                                  @RequestParam(name = "filter_by_tags") Optional<String> tags,
                                                  @RequestParam(name = "filter_by_part") Optional<String> part,
                                                  @RequestParam(name = "sort_by_name") Optional<String> name,
                                                  @RequestParam(name = "sort_by_date") Optional<String> date) {

        Set<Long> tagIdSet = new HashSet<>();
        if (tags.isPresent()) {
            tagIdSet = parseTagIdParam(tags.get());
        }
        List<CertificateDto> certificates = service.getAll(tagIdSet, part,
                        name, date, page, size)
                .stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());
        buildCertificateCollectionLinks(certificates);
        List<Link> links = buildPagination(page, size, CertificateController.class, tags, part, name, date);
        Link selfLink = linkTo(CertificateController.class).withSelfRel();
        links.add(selfLink);
        return CollectionModel.of(certificates, links);


    }

    /*package-private*/
    static void buildCertificateCollectionLinks(Iterable<CertificateDto> certificates) {

        for (CertificateDto certificate : certificates) {
            TagController.buildTagCollectionLinks(certificate.getTags());
            Long id = certificate.getId();
            Link selfLink = linkTo(CertificateController.class).slash(id).withSelfRel();
            certificate.add(selfLink);
        }


    }

    private Set<Long> parseTagIdParam(String tagIdParam) {
        String[] params = tagIdParam.split(DELIMITER);
        return Arrays.stream(params)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    private void buildCertificateLinks(CertificateDto certificate) {
        TagController.buildTagCollectionLinks(certificate.getTags());
        Link allCertificates = linkTo(CertificateController.class).withRel(CERTIFICATES);
        certificate.add(allCertificates);
        Long id = certificate.getId();
        Link selfLink = linkTo(CertificateController.class).slash(id).withSelfRel();
        certificate.add(selfLink);
    }


}
