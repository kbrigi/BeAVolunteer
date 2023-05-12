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
import org.slf4j.Marker;
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
    private ProjectDomainRepository projectDomainRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private DomainService domainService;

    @Autowired
    private UserRepository userRepository;

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

    public void delete(String name) throws BusinessException {
        Project project = projectRepository.getByName(name);
        if (project != null) {
            projectRepository.updateExpired(project.getProjectId(), new Date());
        }
        else
            throw new BusinessException("Project name does not exist!");
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

    public byte[] getImage(String name) throws SQLException {
        Blob projectImg = projectRepository.getByName(name).getProjectImg();
        return getImg(projectImg);
    }

    public void saveProjectDomain(List<DomainDTO> domains, Project project) {
        for ( DomainDTO d : domains ) {
            projectDomainRepository.insertProjectDomain(project.getProjectId(), domainRepository.findByName(d.getDomain_name()).getDomainId());
        }
    }

    public ProjectOutDTO createAndSetDomainsForDTO(Project project) {
        ProjectOutDTO projectOutDTO = ProjectMapper.projectToDTO(project);
        List <Long> domain_ids =  projectDomainRepository.findDomainsByProject(project.getProjectId());
        projectOutDTO.setDomains(domainService.getDomains(domain_ids));
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
        Long domainIdsByName = domainRepository.findByName(domainName).getDomainId();
        List<Long> projectIdsByDomainType = projectDomainRepository.getProjectsByDomain(domainIdsByName);
        List<ProjectOutDTO> projectsByDomain = new ArrayList<>();
        for (Long projectId: projectIdsByDomainType) {
            projectsByDomain.add(createAndSetDomainsForDTO(projectRepository.getById(projectId)));
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
}
