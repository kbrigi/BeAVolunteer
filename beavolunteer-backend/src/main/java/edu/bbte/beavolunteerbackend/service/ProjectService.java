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
import edu.bbte.beavolunteerbackend.model.repository.ProjectRepository;
import edu.bbte.beavolunteerbackend.model.repository.UserRepository;
import edu.bbte.beavolunteerbackend.validator.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectService  extends ImgService  {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveProject(ProjectInDTO projectDTO, Blob img, Long userID) throws BusinessException {
        if (projectRepository.checkName(projectDTO.getProject_name()) == 0) {
            projectRepository.saveAndFlush(prepareProject(projectDTO, userID, img));
        } else {
            throw new BusinessException("Project name must be unique");
        }
    }

    public void delete(String name) throws BusinessException {
        Project project = projectRepository.getByName(name);
        if (project != null) {
            projectRepository.delete(project);
        }
        else
            throw new BusinessException("Project name does not exist!");
    }

    public void setExpiredProject(String name) throws BusinessException {
        Project project = projectRepository.getByName(name);
        if (project != null) {
            projectRepository.updateExpired(project.getProjectId(), new Date());
        }
        else
            throw new BusinessException("Project name does not exist!");
    }

    public List<ProjectOutDTO> getAll() {
        List<Project> projects = projectRepository.findAll();
        return ProjectMapper.projectsToDTO(projects);
    }

    public ProjectOutDTO getProjectById(Long id) {
        return ProjectMapper.projectToDTO(projectRepository.getById(id));
    }

    public ProjectOutDTO getProjectByName(String name) {
        Project project = projectRepository.getByName(name);
        if (project != null) {
            return createAndSetDomainsForDTO(project);
        }
        return null;
    }

    public List<ProjectOutDTO> getProjectByOwnerId(Long userID) throws BusinessException {
        User user = userRepository.getById(userID);
        if (user != null) {
            List<Project> projects =  projectRepository.getByOwner(userID);
            List<ProjectOutDTO> allProjects = new ArrayList<>();
            for (Project p : projects) {
                allProjects.add(createAndSetDomainsForDTO(p));
            }
            return allProjects;
        }
        else
            throw new BusinessException("User does not exist!");
    }

    public byte[] getProjectImg(String name) throws SQLException {
        Blob projectImg = projectRepository.getByName(name).getProjectImg();
        return getImg(projectImg);
    }

    public ProjectOutDTO createAndSetDomainsForDTO(Project project) {
        ProjectOutDTO projectOutDTO = ProjectMapper.projectToDTO(project);
        projectOutDTO.setDomains(DomainMapper.domainsToDTO(project.getDomains()));
        return projectOutDTO;
    }
//  role = states if owner is a role or an owner name
    public List<ProjectOutDTO> getProjectDTOsByOwner(String owner, Boolean role) {
        List<Project> projectsByOwner;
        if (role) {
            List<Long> userIdsByRole = userRepository.findByRole(owner);
            projectsByOwner = projectRepository.getProjectsByOwner(userIdsByRole);
        }
        else {
            Long orgIdByName = userRepository.findByUsername(owner).getId();
            projectsByOwner = projectRepository.getProjectsByOwner(Collections.singletonList(orgIdByName));
        }

        List<ProjectOutDTO> projectsByOwnerRole = new ArrayList<>();
        for (Project project: projectsByOwner) {
            projectsByOwnerRole.add(createAndSetDomainsForDTO(project));
        }
        return projectsByOwnerRole;
    }

    public List<ProjectOutDTO> getProjectDTOsByDomain(String domainName) {
        List<ProjectOutDTO> projectsByDomain = new ArrayList<>();
        for (Project project: projectRepository.findAll()) {
            for (Domain domain: project.getDomains()) {
                if (Objects.equals(domain.getDomainName(), domainName)) {
                    projectsByDomain.add(createAndSetDomainsForDTO(project));
                    break;
                }
            }
        }
        return projectsByDomain;
    }

    public List<ProjectOutDTO> getProjectsFiltered(Map<String, String> filterParams) {
        List<ProjectOutDTO> finalProjectsDTO = new ArrayList<>();
        if (filterParams.get("owner") != null && filterParams.get("owner").length() != 2) {
            finalProjectsDTO.addAll(getProjectDTOsByOwner(filterParams.get("owner"), true));
        }
        if (filterParams.get("domain") != null) {
            String[] domainNames = filterParams.get("domain").split("\\s*,\\s*");
            List<ProjectOutDTO> domainProjectsDTO = new ArrayList<>();
            for (String domainName: domainNames) {
                domainProjectsDTO.addAll(getProjectDTOsByDomain(domainName));
            }
            if (finalProjectsDTO.isEmpty()) {
                finalProjectsDTO.addAll(domainProjectsDTO);
            }
            else {
                finalProjectsDTO = finalProjectsDTO.stream().filter(domainProjectsDTO::contains).collect(Collectors.toList());
            }
        }
        if (filterParams.get("orgs") != null && !Objects.equals(filterParams.get("owner"), "ORGANIZATION")) {
            String[] orgNames = filterParams.get("orgs").split("\\s*,\\s*");
            List<ProjectOutDTO> orgsProjectsDTO = new ArrayList<>();
            for (String orgName: orgNames) {
                orgsProjectsDTO.addAll(getProjectDTOsByOwner(orgName, false));
            }
            if (finalProjectsDTO.isEmpty()) {
                finalProjectsDTO.addAll(orgsProjectsDTO);
            }
            else {
                finalProjectsDTO = finalProjectsDTO.stream().filter(orgsProjectsDTO::contains).collect(Collectors.toList());
            }
        }
        return finalProjectsDTO;
    }

    public List<ProjectOutDTO> getProjectByDomainAndFilter(String domain, Map<String, String> filterParams) {
        List<ProjectOutDTO> projectDomainDTOs = getProjectDTOsByDomain(domain);
        List<ProjectOutDTO> projectFilteredDTOs = getProjectsFiltered(filterParams);
        return projectDomainDTOs.stream().filter(projectFilteredDTOs::contains).collect(Collectors.toList());

    }

    public Project prepareProject(ProjectInDTO projectDTO, Long userID, Blob img){
        Project project = ProjectMapper.projectDTOToEntity(projectDTO);
        project.setProjectImg(img);
        project.setOwnerId(userID);
        Set<Domain> domains = new HashSet<>();
        for (DomainDTO domainDTO: projectDTO.getDomains()) {
            domains.add(domainRepository.findByName(domainDTO.getDomain_name()));
        }
        project.setDomains(domains);
        return project;
    }

    public void updateProject(ProjectInDTO projectDTO, Blob img, Long userID, String name) throws BusinessException {
        if (projectRepository.checkName(name) != 0) {
            Project updatedProject = prepareProject(projectDTO, userID, img);
            updatedProject.setProjectId(projectRepository.getByName(name).getProjectId());
            projectRepository.save(updatedProject);
        } else {
            throw new BusinessException("Project name does not exist");
        }

    }
}
