package edu.bbte.beavolunteerbackend.service;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.DomainDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.ProjectInDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.ProjectOutDTO;
import edu.bbte.beavolunteerbackend.controller.mapper.ProjectMapper;
import edu.bbte.beavolunteerbackend.model.Project;
import edu.bbte.beavolunteerbackend.model.ProjectDomain;
import edu.bbte.beavolunteerbackend.model.Role;
import edu.bbte.beavolunteerbackend.model.User;
import edu.bbte.beavolunteerbackend.model.repository.DomainRepository;
import edu.bbte.beavolunteerbackend.model.repository.ProjectDomainRepository;
import edu.bbte.beavolunteerbackend.model.repository.ProjectRepository;
import edu.bbte.beavolunteerbackend.model.repository.UserRepository;
import edu.bbte.beavolunteerbackend.util.JWTToken;
import edu.bbte.beavolunteerbackend.validator.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Service
public class ProjectService  extends ImgService  {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectDomainRepository projectDomainRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveProject(ProjectInDTO projectDTO, Blob img, Long userID) throws BusinessException {
        if (projectRepository.checkName(projectDTO.getProject_name()) == 0) {
            Project project = ProjectMapper.projectDTOToEntity(projectDTO);
            project.setProjectImg(img);
            User user = userRepository.getById(userID);
            if (user.getRole() == Role.USER){
                project.setOwnerId(userID);
            }
            else {
                project.setOrganizationId(userID);
            }
            projectRepository.insertProject(project.getCreationDate(), project.getExpirationDate(),
                    project.getProjectDescription(), project.getProjectImg(), project.getProjectName(), project.getOwnerId(), project.getOrganizationId());
            saveProjectDomain(projectDTO.getDomains(), project);
        } else {
            throw new BusinessException("Project name must be unique");
        }
    }

    public void delete(Long id) {
        projectRepository.delete(projectRepository.getById(id));
    }

    public List<ProjectOutDTO> getAll() {
        return ProjectMapper.projectsToDTO(projectRepository.findAll());
    }

    public ProjectOutDTO getProjectById(Long id) {
        return ProjectMapper.projectToDTO(projectRepository.getById(id));
    }

    public ProjectOutDTO getProjectByName(String name) {
        return ProjectMapper.projectToDTO(projectRepository.getByName(name));
    }

    public byte[] getImage(Long id) throws SQLException {
        Blob projectImg = projectRepository.getById(id).getProjectImg();
        return getImg(projectImg);
    }

    public void saveProjectDomain(List<DomainDTO> domains, Project project) {
        for ( DomainDTO d : domains ) {
            projectDomainRepository.saveAndFlush(
                    new ProjectDomain(domainRepository.findByName(d.getDomain_name()), project));
        }
    }
}
