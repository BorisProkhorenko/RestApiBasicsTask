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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class GiftCertificateController {

    private final GiftCertificateService service;
    private final ObjectMapper objectMapper;

    public GiftCertificateController(GiftCertificateService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @GetMapping(value = "/certificates/{id}")
    public @ResponseBody
    GiftCertificate getCertificateById(@PathVariable Long id) {
        return service.getCertificateById(id);
    }

    @GetMapping(value = "/certificates")
    public @ResponseBody
    List<GiftCertificate> getAllCertificates() {
        return service.getAllCertificates();
    }

    @PostMapping(value = "/certificates/{name}/{description}/{price}/{duration}")
    public @ResponseBody
    GiftCertificate createCertificate(@PathVariable String name, @PathVariable String description,
                                      @PathVariable double price, @PathVariable int duration) {
        GiftCertificate certificate = new GiftCertificate(name, description, price, duration);
        return service.createCertificate(certificate);
    }

    @DeleteMapping(value = "/certificates/{id}")
    public @ResponseBody
    void deleteCertificate(@PathVariable Long id) {
        service.deleteCertificate(id);
    }

    @DeleteMapping(value = "/certificates/{id}/{tagId}")
    public @ResponseBody
    void removeTag(@PathVariable Long id, @PathVariable Long tagId) {
        service.removeTag(id, tagId);
    }

    @PostMapping(value = "/certificates//{id}/{tagId}")
    public @ResponseBody
    void addTag(@PathVariable Long id, @PathVariable Long tagId) {
        service.addTag(id, tagId);
    }


    @PatchMapping(path = "/certificates/{id}", consumes = "application/json-patch+json")
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


    @GetMapping(value = {"/certificates/params"})
    public @ResponseBody
    List<GiftCertificate> getCertificatesWithParams(@RequestParam("id") Optional<String> tagId,
                                                    @RequestParam("part") Optional<String> part,
                                                    @RequestParam("nameSort") Optional<String> nameSort,
                                                    @RequestParam("descriptionSort") Optional<String> descriptionSort) {
        return service.getCertificatesWithParams(tagId, part,
                nameSort, descriptionSort);
    }


}
