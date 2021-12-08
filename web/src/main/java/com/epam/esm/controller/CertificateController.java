package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateDtoMapper;
import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.CertificateSpecificationsBuilder;
import com.epam.esm.service.SearchCriteria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final static String ASC = "asc";
    private final static String DESC = "desc";
    private final static String PARAM_PATTERN = "\\(.*\\)";
    private final static String LEFT_PATTERN = ".*\\(";
    private final static String RIGHT_PATTERN = "\\)";
    private final static String EMPTY = "";
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
        Optional<Certificate> optionalCertificate =  service.findById(id);
        if(!optionalCertificate.isPresent()){
            throw new CertificateNotFoundException(id);
        }
        CertificateDto certificate = dtoMapper.toDto(optionalCertificate.get());
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
    @PreAuthorize("#oauth2.hasScope('write') and hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("#oauth2.hasScope('write') and hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("#oauth2.hasScope('write') and hasRole('ROLE_ADMIN')")
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
    public CollectionModel<CertificateDto> getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                  @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return getAll(page, size, Optional.empty(), Optional.empty(), EMPTY);
    }

    /**
     * Method allows getting {@link Certificate} objects from db filtered and/or sorted
     *
     * @param pageNum - page of displayed dto objects
     * @param size - count of displayed dto objects
     * @param tags - filtering by tags id (id numbers must be separated by delimiter "," without any spaces)
     * @param part - filtering by part of certificate name or description
     * @return @return {@link CollectionModel} of {@link CertificateDto} DTO of entity objects from DB
     */
    @Override
    @GetMapping(produces = {"application/hal+json"})
    public CollectionModel<CertificateDto> getAll(@RequestParam(name = "page", required = false, defaultValue = "0")
                                                          int pageNum,
                                                  @RequestParam(name = "size", required = false, defaultValue = "10")
                                                          int size,
                                                  @RequestParam(name = "filter_by_tags") Optional<String> tags,
                                                  @RequestParam(name = "filter_by_part") Optional<String> part,
                                                  @RequestParam(name = "sort_by", required = false,
                                                          defaultValue = "asc(id)") String sort) {

        Specification<Certificate> specification = buildSpec(tags, part);
        Map<String, String> sorts = mapSorts(sort);
        Page<Certificate> page = service.findAll(specification, sorts, pageNum, size);
        List<CertificateDto> certificates = page.getContent()
                .stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());

        buildCertificateCollectionLinks(certificates);
        List<Link> links = buildPagination(page, CertificateController.class, tags, part, sort);
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

    private Specification<Certificate> buildSpec(Optional<String> tags, Optional<String> part) {
        CertificateSpecificationsBuilder builder = new CertificateSpecificationsBuilder();
        buildTagsSpec(tags, builder);
        buildPartSpec(part, builder);
        return builder.build();
    }

    private void buildTagsSpec(Optional<String> tags, CertificateSpecificationsBuilder builder) {
        if (tags.isPresent()) {
            Set<Tag> tagIdSet = parseTagIdParam(tags.get());
            builder.with(SearchCriteria.Operation.TAGS, tagIdSet);
        }
    }

    private void buildPartSpec(Optional<String> optionalPart, CertificateSpecificationsBuilder builder) {
        if (optionalPart.isPresent()) {
            String part = optionalPart.get();
            builder.with(SearchCriteria.Operation.PART, part);
        }
    }

    private Map<String, String> mapSorts(String sort) {
        String[] params = sort.split(DELIMITER);
        Map<String, String> sortMap = new HashMap<>();
        for (String param : params) {
            mapSortParam(param, sortMap);
        }
        return sortMap;
    }

    private void mapSortParam(String param, Map<String, String> sortMap) {
        if (param.matches(ASC + PARAM_PATTERN)) {
            String key = parseKey(param);
            sortMap.put(key, ASC);
        } else if (param.matches(DESC + PARAM_PATTERN)) {
            String key = parseKey(param);
            sortMap.put(key, DESC);
        }
    }

    private String parseKey(String param) {
        return param.replaceAll(LEFT_PATTERN, EMPTY)
                .replaceAll(RIGHT_PATTERN, EMPTY);
    }

    private Set<Tag> parseTagIdParam(String tagIdParam) {
        String[] params = tagIdParam.split(DELIMITER);
        return Arrays.stream(params)
                .map(Long::parseLong)
                .map(Tag::new)
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
