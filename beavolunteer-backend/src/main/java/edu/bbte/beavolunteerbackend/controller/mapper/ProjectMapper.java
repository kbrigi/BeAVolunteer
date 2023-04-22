package edu.bbte.beavolunteerbackend.controller.mapper;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.DomainDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.ProjectInDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.ProjectOutDTO;
import edu.bbte.beavolunteerbackend.model.Project;
import edu.bbte.beavolunteerbackend.model.repository.ProjectDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectMapper {

    public static Project projectDTOToEntity(ProjectInDTO projectDTO) {
        Project project = new Project();
        project.setProjectName(projectDTO.getProject_name());
        project.setProjectDescription(projectDTO.getProject_description());
        project.setCreationDate(new Date());
        project.setExpirationDate(projectDTO.getExpiration_date());
        return project;
    }

    public static List<ProjectOutDTO> projectsToDTO(List<Project> proj) {
        return proj.stream().map(ProjectMapper::projectToDTO).collect(Collectors.toList());
    }

    public static ProjectOutDTO projectToDTO(Project proj) {
        ProjectOutDTO projectOutDTO = new ProjectOutDTO();
        projectOutDTO.setProject_name(proj.getProjectName());
        projectOutDTO.setProject_description(proj.getProjectDescription());
        projectOutDTO.setProject_img("http://localhost:8080/proj/image/" + proj.getProjectId());
        projectOutDTO.setCreation_date(proj.getCreationDate());
        projectOutDTO.setExpiration_date(proj.getExpirationDate());
        projectOutDTO.setOwner(UserMapper.userToDTO(proj.getOwner()));
//        List<DomainDTO> domains = projectDomainRepository.findDomainsByProject(proj);
//        projectOutDTO.setDomains(domains);
        return projectOutDTO;
    }
}
