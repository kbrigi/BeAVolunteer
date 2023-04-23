package edu.bbte.beavolunteerbackend.service;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.DomainDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.ProjectInDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.ProjectOutDTO;
import edu.bbte.beavolunteerbackend.controller.mapper.DomainMapper;
import edu.bbte.beavolunteerbackend.controller.mapper.ProjectMapper;
import edu.bbte.beavolunteerbackend.model.Domain;
import edu.bbte.beavolunteerbackend.model.Project;
import edu.bbte.beavolunteerbackend.model.User;
import edu.bbte.beavolunteerbackend.model.repository.DomainRepository;
import edu.bbte.beavolunteerbackend.model.repository.ProjectDomainRepository;
import edu.bbte.beavolunteerbackend.model.repository.ProjectRepository;
import edu.bbte.beavolunteerbackend.model.repository.UserRepository;
import edu.bbte.beavolunteerbackend.validator.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProjectService  extends ImgService  {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectDomainRepository projectDomainRepository;

    @Autowired
    private DomainRepository domainRepository;

    public void saveProject(ProjectInDTO projectDTO, Blob img, Long userID) throws BusinessException {
        if (projectRepository.checkName(projectDTO.getProject_name()) == 0) {
            Project project = ProjectMapper.projectDTOToEntity(projectDTO);
            project.setProjectImg(img);
            project.setOwnerId(userID);
            projectRepository.insertProject(project.getCreationDate(), project.getExpirationDate(),
                    project.getProjectDescription(), project.getProjectImg(), project.getProjectName(), project.getOwnerId());
            Project newProject = projectRepository.getByName(project.getProjectName());
            saveProjectDomain(projectDTO.getDomains(), newProject);
        } else {
            throw new BusinessException("Project name must be unique");
        }
    }

    public void delete(Long id) {
        projectRepository.delete(projectRepository.getById(id));
    }

    public List<ProjectOutDTO> getAll() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectOutDTO> projectsDTO = ProjectMapper.projectsToDTO(projects);
        for (ProjectOutDTO p: projectsDTO) {
            List <DomainDTO> domainDTOS = new ArrayList<>();
            Long project_id = projectRepository.getByName(p.getProject_name()).getProjectId();
            List <Long> domain_ids =  projectDomainRepository.findDomainsByProject(project_id);
            for (Long did : domain_ids) {
                Optional<Domain> domain = domainRepository.findById(did);
                if (domain.isPresent()) {
                    DomainDTO domainDTO = DomainMapper.domainToDTO(domain.get());
                    domainDTOS.add(domainDTO);
                }
            }
            p.setDomains(domainDTOS);
        }
        return projectsDTO;
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
            projectDomainRepository.insertProjectDomain(project.getProjectId(), domainRepository.findByName(d.getDomain_name()).getDomainId());
        }
    }
}
