package edu.bbte.beavolunteerbackend.controller.mapper;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.ProjectInDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.ProjectOutDTO;
import edu.bbte.beavolunteerbackend.model.Project;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class ProjectMapper {

    public static Project projectDTOToEntity(ProjectInDTO projectDTO) {
        Project project = new Project();
        project.setProjectName(projectDTO.getProject_name());
        project.setProjectDescription(projectDTO.getProject_description());
        project.setCreationDate(new Date(System.currentTimeMillis()));
        project.setExpirationDate(new Date(projectDTO.getExpiration_date().getTime()));
        return project;
    }

    public static List<ProjectOutDTO> projectsToDTO(List<Project> proj) {
        return proj.stream().map(ProjectMapper::projectToDTO).collect(Collectors.toList());
    }

    public static ProjectOutDTO projectToDTO(Project proj) {
        ProjectOutDTO projectOutDTO = new ProjectOutDTO();
        projectOutDTO.setProject_name(proj.getProjectName());
        projectOutDTO.setProject_description(proj.getProjectDescription());
        projectOutDTO.setProject_img("http://localhost:8080/proj/image/" + proj.getProjectName());
        projectOutDTO.setCreation_date(proj.getCreationDate());
        projectOutDTO.setExpiration_date(proj.getExpirationDate());
        projectOutDTO.setOwner(UserMapper.userToDTO(proj.getOwner()));
        projectOutDTO.setDomains(DomainMapper.domainsToDTO(proj.getDomains()));
        return projectOutDTO;
    }
}
