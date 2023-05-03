package edu.bbte.beavolunteerbackend.controller;

import com.google.gson.Gson;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.ProjectInDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.ProjectOutDTO;
import edu.bbte.beavolunteerbackend.service.ProjectService;
import edu.bbte.beavolunteerbackend.service.UserService;
import edu.bbte.beavolunteerbackend.util.JWTToken;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;

//TO DO
// Filter: get projects by given domains

@CrossOrigin("*")
@RestController
@RequestMapping("/proj")
@Slf4j
public class ProjectController extends Controller{
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTToken jwtToken;

    @PostMapping(value = "/save", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveProject(@RequestPart String project, @RequestPart("file") MultipartFile file,
                                              @RequestHeader("Authorization") String jwttoken)
            throws SQLException, IOException {
        Blob blob = prepareImage(file);
        if (blob == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Gson gson = new Gson();
            ProjectInDTO projectDTO = gson.fromJson(project, ProjectInDTO.class);

            String token= jwttoken.substring(17);
            token = token.substring(0,token.length()-2);
            Long userID = userService.getUserFromUsername(jwtToken.getUsernameFromToken(token)).getId();
            projectService.saveProject(projectDTO, blob, userID);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("")
    public ProjectOutDTO getProject(@RequestParam(required = false) Long id,
                                        @RequestParam(required = false) String name) {
        if ( id == null && name != null) {
            log.info(name);
            return projectService.getProjectByName(name);
        }
        return projectService.getProjectById(id);
    }

    @GetMapping("/all")
    public List<ProjectOutDTO> getProjects() {
        return projectService.getAll();
    }

    @GetMapping(value = "/image/{name}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable String name) throws SQLException {
        byte[] product = projectService.getImage(name);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(product);
    }

    //to do: connect org+vol on delete
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.delete(id);
    }
}