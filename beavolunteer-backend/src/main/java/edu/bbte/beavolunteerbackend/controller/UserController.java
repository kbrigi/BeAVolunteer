package edu.bbte.beavolunteerbackend.controller;

import com.google.gson.Gson;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.OrganizationOutDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.TokenOutDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.UserOutDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.OrganizationDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.UserDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.VolunteerDTO;
import edu.bbte.beavolunteerbackend.controller.mapper.UserMapper;
import edu.bbte.beavolunteerbackend.service.UserService;
import edu.bbte.beavolunteerbackend.validator.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

//TO DO
// - password resetting
// - user update
// - delete user! + connection

@CrossOrigin("*")
@RestController
@RequestMapping("/")
@Slf4j
public class UserController extends Controller{
    @Autowired
    private UserService userService;

    @PostMapping("/volunteer")
    public void saveVolunteer(@RequestBody @Valid VolunteerDTO volunteerDTO) {
        try {
            userService.saveUser(volunteerDTO);
            ResponseEntity.ok();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value = "/org", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveOrganization(@RequestPart(value = "organization") String organization,
                                                   @RequestPart(value = "file") MultipartFile file) throws SQLException, IOException {
        Blob blob = prepareImage(file);
        if (blob == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Gson gson = new Gson();
            OrganizationDTO orgDTO = gson.fromJson(organization, OrganizationDTO.class);

            userService.saveOrg(orgDTO, blob);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/org/all")
    public List<OrganizationOutDTO> getAllOrganization() {
        return userService.getAllOrg();
    }

    @GetMapping("/org/{name}")
    public OrganizationOutDTO getOrganization(@PathVariable String name) throws BusinessException {
        return userService.getOrg(name);
    }

    @GetMapping("/org/image/{username}")
    public ResponseEntity<byte[]> getImage(@PathVariable String username) throws SQLException {
        byte[] logo = userService.getImage(username);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(logo);
    }

    @GetMapping("/users")
    public List<UserOutDTO> getAllUsers() {
        return UserMapper.usersToDTO(userService.getAllUser());
    }

    @PostMapping(value = "/login")
    public ResponseEntity<TokenOutDTO> login(@RequestBody @Valid UserDTO userDTO) {
        try {
            String token = userService.matchUser(userDTO.getEmail(), userDTO.getPassword());
            return ResponseEntity.ok(new TokenOutDTO(token));
        } catch (BusinessException businessException) {
            log.error(businessException.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, businessException.getMessage());
        }
    }

}
