package edu.bbte.beavolunteerbackend.controller;

import com.google.gson.Gson;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.DomainDTO;
import edu.bbte.beavolunteerbackend.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;

@CrossOrigin("*")
@RestController
@RequestMapping("/domain")
public class DomainController extends Controller{
    @Autowired
    private DomainService domainService;

    @PostMapping(value = "/save", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveDomain(@RequestPart String domain, @RequestPart("file") MultipartFile file) throws SQLException, IOException {
        Blob blob = prepareImage(file);
        if (blob == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Gson gson = new Gson();
            DomainDTO domainDTO = gson.fromJson(domain, DomainDTO.class);

            domainService.saveDomain(domainDTO, blob);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public Collection<DomainDTO> getDomains() {
        return domainService.getAll();
    }

    @GetMapping(value = "/image/{id}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) throws SQLException {
        byte[] product = domainService.getImage(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        domainService.remove(id);
    }

}
