package com.epam.esm.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * This Controller provides public API for operations with {@link GiftCertificate} entity.
 * Uses {@link GiftCertificateService} to access Data Base through business-logic layer.
 * Uses {@link ObjectMapper} to map objects from {@link JsonPatch}
 *
 * @author Boris Prokhorenko
 * @see GiftCertificateService
 * @see GiftCertificate
 * @see ObjectMapper
 * @see JsonPatch
 */
@RestController
@RequestMapping(value = "/certificates")
public class GiftCertificateController {

    private final GiftCertificateService service;
    private final ObjectMapper objectMapper;

    public GiftCertificateController(GiftCertificateService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    /**
     *Method allows getting {@link GiftCertificate} from DB by its id
     *
     * @param id - primary key to search {@link GiftCertificate} entity object in DB
     * @return {@link GiftCertificate} entity object from DB
     */
    @GetMapping(value = "/{id}")
    public GiftCertificate getCertificateById(@PathVariable Long id) {
        return service.getCertificateById(id);
    }

    /**
     *Method allows getting all {@link GiftCertificate} entity objects from DB
     *
     * @return {@link List} of {@link GiftCertificate} entity objects from DB
     */
    @GetMapping(value = "/")
    public List<GiftCertificate> getAllCertificates() {
        return service.getAllCertificates();
    }

    /**
     * Method allows creating a new {@link GiftCertificate} entity object in DB
     *
     * @param name - name field of {@link GiftCertificate}
     * @param description - description field of {@link GiftCertificate}
     * @param price - price field of {@link GiftCertificate}
     * @param duration - duration field of {@link GiftCertificate}
     * @return {@link GiftCertificate} object, which you created
     */
    @PostMapping(value = "/{name}/{description}/{price}/{duration}")
    public GiftCertificate createCertificate(@PathVariable String name, @PathVariable String description,
                                      @PathVariable double price, @PathVariable int duration) {
        GiftCertificate certificate = new GiftCertificate(name, description, price, duration);
        return service.createCertificate(certificate);
    }

    /**
     * Method allows deleting {@link GiftCertificate} from DB by its id
     *
     * @param id - primary key to search {@link GiftCertificate} entity object in DB
     */
    @DeleteMapping(value = "/{id}")
    public void deleteCertificate(@PathVariable Long id) {
        service.deleteCertificate(id);
    }

    /**
     * Method allows removing {@link com.epam.esm.model.Tag} from {@link GiftCertificate} from DB by theirs id
     *
     * @param id - primary key to search {@link GiftCertificate} entity object in DB
     * @param tagId - primary key to search {@link com.epam.esm.model.Tag} entity object in DB
     */
    @DeleteMapping(value = "/{id}/{tagId}")
    public void removeTag(@PathVariable Long id, @PathVariable Long tagId) {
        service.removeTag(id, tagId);
    }

    /**
     * Method allows adding {@link com.epam.esm.model.Tag} to {@link GiftCertificate} in DB by theirs id
     *
     * @param id - primary key to search {@link GiftCertificate} entity object in DB
     * @param tagId - primary key to search {@link com.epam.esm.model.Tag} entity object in DB
     */
    @PostMapping(value = "/{id}/{tagId}")
    public GiftCertificate addTag(@PathVariable Long id, @PathVariable Long tagId) {
      return service.addTag(id, tagId);
    }

    /**
     * Method allows updating {@link GiftCertificate} info in DB by its id
     *
     * @param id - primary key to search {@link GiftCertificate} entity object in DB
     * @param patch - {@link JsonPatch} from PATCH Http method body which represents
     *              which fields will be updated
     * @return {@link ResponseEntity} ok if update succeeded and INTERNAL_SERVER_ERROR if not
     */
    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<GiftCertificate> updateCustomer(@PathVariable long id,
                                                          @RequestBody JsonPatch patch) {
        try {
            GiftCertificate certificate = service.getCertificateById(id);
            GiftCertificate certificatePatched = applyPatchToCustomer(patch, certificate);
            service.updateCertificate(certificatePatched);

            return ResponseEntity.ok(certificatePatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    private GiftCertificate applyPatchToCustomer(
            JsonPatch patch, GiftCertificate certificate) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(certificate, JsonNode.class));
        return objectMapper.treeToValue(patched, GiftCertificate.class);
    }

    /**
     * Method allows getting {@link GiftCertificate} objects from db filtered and/or sorted
     *
     * @param tagId - if presents filtering by tag id will be applied
     * @param part - if presents filtering by part of name and description will be applied
     * @param nameSort - if presents sorting(ASC/DESC) by name will be applied. If nameSort param
     *                 is different from "asc" or "desc" in ignored case sort will not be applied.
     * @param descriptionSort- if presents sorting(ASC/DESC) by name will be applied. If nameSort param
     *      *                 is different from "asc" or "desc" in ignored case sort will not be applied.
     * @return @return {@link List} of {@link GiftCertificate} entity objects from DB
     */
    @GetMapping(value = {"/params"})
    public List<GiftCertificate> getCertificatesWithParams(@RequestParam("tagId") Optional<String> tagId,
                                                    @RequestParam("part") Optional<String> part,
                                                    @RequestParam("nameSort") Optional<String> nameSort,
                                                    @RequestParam("descriptionSort") Optional<String> descriptionSort) {
        return service.getCertificatesWithParams(tagId, part,
                nameSort, descriptionSort);
    }


}
