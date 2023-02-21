package edu.bbte.beavolunteerbackend.controller;

import com.google.gson.Gson;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.ProjectInDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.ProjectOutDTO;
import edu.bbte.beavolunteerbackend.service.ProjectService;
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

@CrossOrigin("*")
@RestController
@RequestMapping("/proj")
public class ProjectController extends Controller{
    @Autowired
    private ProjectService projectService;

    @PostMapping(value = "/save", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveProject(@RequestPart String project, @RequestPart("file") MultipartFile file)
            throws SQLException, IOException {
        Blob blob = prepareImage(file);
        if (blob == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Gson gson = new Gson();
            ProjectInDTO projectDTO = gson.fromJson(project, ProjectInDTO.class);

            projectService.saveProject(projectDTO, blob);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("")
    public ProjectOutDTO getProject(@RequestParam(required = false) Long id,
                                        @RequestParam(required = false) String name) {
        if ( id == null ) {
            return projectService.getProjectByName(name);
        }
        return projectService.getProjectById(id);
    }

//    @GetMapping()
//    public ProjectOutDTO getProjectByName(@RequestParam String name) {
//        return projectService.getProjectByName(name);
//    }

    @GetMapping("/all")
    public List<ProjectOutDTO> getProjects() {
        return projectService.getAll();
    }

    @GetMapping(value = "/image/{id}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) throws SQLException {
        byte[] product = projectService.getImage(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(product);
    }

    //to do: connect org+vol on delete
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.delete(id);
    }
}