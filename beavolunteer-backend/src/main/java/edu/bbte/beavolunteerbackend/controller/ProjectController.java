package edu.bbte.beavolunteerbackend.controller;

import com.google.gson.Gson;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.ProjectInDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.ProjectOutDTO;
import edu.bbte.beavolunteerbackend.service.ProjectService;
import edu.bbte.beavolunteerbackend.service.UserService;
import edu.bbte.beavolunteerbackend.util.JWTToken;
import edu.bbte.beavolunteerbackend.validator.exception.BusinessException;
import edu.bbte.beavolunteerbackend.validator.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
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
import java.util.Map;

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
        if (name != null) {
            return projectService.getProjectByName(name);
        }
        else if (id != null) {
            return projectService.getProjectById(id);
        }
        return null;
    }

    @GetMapping("/owned")
    public List<ProjectOutDTO> getProjectsByToken(@RequestHeader("Authorization") String jwttoken) throws BusinessException {
        String token= jwttoken.substring(17);
        token = token.substring(0,token.length()-2);
        Long userID = userService.getUserFromUsername(jwtToken.getUsernameFromToken(token)).getId();
        return projectService.getProjectByOwnerId(userID);
    }

    @GetMapping("/nr")
    public Integer getProjectsNr() {
        return projectService.getNr();
    }

    @GetMapping("/all")
    public List<ProjectOutDTO> getProjects() {
        return projectService.getAll();
    }

    @GetMapping("/search/{name}")
    public List<ProjectOutDTO> getProjectsBySearch(@PathVariable String name) {
        return projectService.getProjectBySearch(name);
    }

    @GetMapping("/phone/{name}")
    public String getPhoneByProjectName(@PathVariable String name) {
        return projectService.getPhoneByProjectName(name);
    }

    @GetMapping("/domain/{domain}")
    public List<ProjectOutDTO> getProjectsByDomain(@PathVariable String domain) {
        return projectService.getProjectDTOsByDomain(domain);
    }

    @GetMapping("/domain/{domain}/filter")
    public List<ProjectOutDTO> getProjectsByDomainAndFilter(@PathVariable String domain,
                                                            @RequestParam Map<String, String> filterParams) {
        return projectService.getProjectByDomainAndFilter(domain, filterParams);
    }

    @GetMapping("/filter")
    public List<ProjectOutDTO> getFilteredProjects(@RequestParam Map<String, String> filterParams) {
        return projectService.getProjectsFiltered(filterParams);
    }

    @GetMapping(value = "/image/{name}")
    public ResponseEntity<byte[]> getProjectImage(@PathVariable String name) throws SQLException {
        if (projectService.getProjectByName(name) == null) {
            throw  new NotFoundException();
        }
        byte[] product = projectService.getProjectImg(name);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(product);
    }

    @DeleteMapping("/{name}")
    public void deleteProject(@PathVariable String name) throws BusinessException {
        projectService.delete(name);
    }

    @PostMapping("/deactivate/{name}")
    public void deactivateProject(@PathVariable String name) throws BusinessException {
        projectService.setExpiredProject(name);
    }

    @PostMapping(value = "/update/{name}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateProject(@PathVariable String name, @RequestPart String project, @RequestPart("file") MultipartFile file,
                              @RequestHeader("Authorization") String jwttoken) throws SQLException, IOException {
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
                projectService.updateProject(projectDTO, blob, userID, name);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
            return new ResponseEntity<>(HttpStatus.OK);
    }
}