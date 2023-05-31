package edu.bbte.beavolunteerbackend.service;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.DomainDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.OrganizationDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.VolunteerDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.OrganizationOutDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.ProjectOutDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.UserOutDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.VolunteerOutDTO;
import edu.bbte.beavolunteerbackend.controller.mapper.DomainMapper;
import edu.bbte.beavolunteerbackend.controller.mapper.ProjectMapper;
import edu.bbte.beavolunteerbackend.controller.mapper.UserMapper;
import edu.bbte.beavolunteerbackend.model.*;
import edu.bbte.beavolunteerbackend.model.repository.*;
import edu.bbte.beavolunteerbackend.model.Role;
import edu.bbte.beavolunteerbackend.util.JWTToken;
import edu.bbte.beavolunteerbackend.validator.UserValidator;
import edu.bbte.beavolunteerbackend.validator.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static edu.bbte.beavolunteerbackend.controller.mapper.UserMapper.*;

@Slf4j
@Service
public class UserService extends ImgService {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private JWTToken jwToken;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public void saveUser(VolunteerDTO userDTO) throws BusinessException {
        User user = DTOToUser(userDTO);
        UserValidator.errorList.clear();
        userValidator.validate(user);
        if (UserValidator.errorList.isEmpty()) {
            Set<Domain> domains = new HashSet<>();
            for (DomainDTO domainDTO: userDTO.getDomains()) {
                domains.add(domainRepository.findByName(domainDTO.getDomain_name()));
            }
            Volunteer volunteer = volunteerDTOToVolunteer(userDTO);
            volunteer.setDomains(domains);
            volunteer.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            volunteerRepository.saveAndFlush(volunteer);
        } else {
            throw new BusinessException(UserValidator.errorList.toString());
        }
    }

//check if login data(email+password) is correct
    public String matchUser(String email, String password) throws BusinessException {
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new BusinessException("This user does not exist");
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("Incorrect password");
        }
        return jwToken.generateToken(user);
    }

    public void saveOrg(OrganizationDTO organizationDTO, Blob file) throws BusinessException {
        User user = DTOToUser(organizationDTO);
        UserValidator.errorList.clear();
        userValidator.validate(user);
        if (UserValidator.errorList.isEmpty()) {
            Organization org = orgDTOToUser(organizationDTO);
            Set<Domain> domains = new HashSet<>();
            for (DomainDTO domainDTO: organizationDTO.getDomains()) {
                domains.add(domainRepository.findByName(domainDTO.getDomain_name()));
            }
            org.setLogo(file);
            org.setDomains(domains);
            org.setPassword(passwordEncoder.encode(organizationDTO.getPassword()));
            organizationRepository.saveAndFlush(org);
        } else {
            throw new BusinessException(UserValidator.errorList.toString());
        }
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public List<OrganizationOutDTO> getAllOrg() {
        List<Organization> orgs = organizationRepository.findAll();
        List<OrganizationOutDTO> organizationOutDTOS = new ArrayList<>();
        for (Organization org: orgs) {
            organizationOutDTOS.add(createAndSetDomainsForDTO(org));
        }
        return organizationOutDTOS;
    }

    public OrganizationOutDTO getOrgById(Long id) {
        Organization org = organizationRepository.getById(id);
        return organizationToDTO(org, userRepository.getById(org.getId()));
    }

    public byte[] getImage(String username) throws SQLException {
        Blob projectImg = organizationRepository.getById(userRepository.findByUsername(username).getId()).getLogo();
        return getImg(projectImg);
    }

    public User getUserFromUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public OrganizationOutDTO getOrgByUsername(String username) throws BusinessException {
        Long id = getUserFromUsername(username).getId();
        if (id != null) {
            return createAndSetDomainsForDTO(organizationRepository.getById(id));
        }
        else {
            throw new BusinessException("Username does not exist!");
        }
    }

    public VolunteerOutDTO getVolunteerByUsername(String username) {
        User user = userRepository.findByUsername(username);
        Volunteer volunteer = volunteerRepository.getById(user.getId());
        return UserMapper.volunteerToDTO(volunteer, user);
    }

    public void deleteOrg(String name) throws BusinessException {
        User user = userRepository.findByUsername(name);
        if (user != null) {
            List<Project> projects = projectRepository.getByOwner(user.getId());
            projectRepository.deleteAll(projects);
            userRepository.deleteById(user.getId());
        }
        else
            throw new BusinessException("Username does not exist!");
    }

    public UserOutDTO getRole(String username) throws BusinessException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return userToDTO(user);
        }
        else
            throw new BusinessException("Username does not exist!");
    }

    public OrganizationOutDTO createAndSetDomainsForDTO(Organization org) {
        OrganizationOutDTO organizationOutDTO = UserMapper.organizationToDTO(org, userRepository.getById(org.getId()));
        List<DomainDTO> domains =  DomainMapper.domainsToDTO(org.getDomains());
        organizationOutDTO.setDomains(domains);
        return organizationOutDTO;
    }

    public List<ProjectOutDTO> setFavouriteProject(Long userID, String projectName) {
        Volunteer volunteer = volunteerRepository.getById(userID);
        List<Project> favourites = volunteer.getFavouriteProj();
        favourites.add(projectRepository.getByName(projectName));
        volunteer.setFavouriteProj(favourites);
        volunteerRepository.saveAndFlush(volunteer);
        return ProjectMapper.projectsToDTO(favourites);
    }

    public List<ProjectOutDTO> removeFavouriteProject(Long userID, String projectName) {
        Volunteer volunteer = volunteerRepository.getById(userID);
        List<Project> favourites = volunteer.getFavouriteProj();
        favourites.remove(projectRepository.getByName(projectName));
        volunteer.setFavouriteProj(favourites);
        volunteerRepository.saveAndFlush(volunteer);
        return ProjectMapper.projectsToDTO(favourites);
    }

    public List<ProjectOutDTO> getFavouriteProject(Long userID) {
        return ProjectMapper.projectsToDTO(volunteerRepository.getById(userID).getFavouriteProj());
    }

    public List<ProjectOutDTO> getSortedFavouriteProjects() {
        HashMap<Project, Integer> allProjects = new HashMap<Project, Integer>();

        List<Long> volunteers = userRepository.findByRole("USER");
        for (Long userId: volunteers) {
            List<Project> projects = volunteerRepository.getById(userId).getFavouriteProj();
            for(Project project: projects) {
                Integer count = allProjects.get(project);
                if(count == null) {
                    allProjects.put(project, 1);
                }
                else {
                    allProjects.put(project,count + 1);
                }
            }
        }
        for(Project project: projectRepository.findAll()) {
            allProjects.putIfAbsent(project, 0);
        }
        HashMap<Project, Integer> sortedProjects = allProjects.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        List<Project> allSortedProjects = new ArrayList<>(sortedProjects.keySet());
        return ProjectMapper.projectsToDTO(allSortedProjects);
    }

    public void updateVolunteer(String name, VolunteerOutDTO volunteerDTO) {
        Volunteer volunteer = UserMapper.volunteerExtendedDTOToVolunteer(volunteerDTO);
        User user = userRepository.findByUsername(name);
        if (volunteerDTO.getDomains() != null ) {
            Set<Domain> domains = new HashSet<>();
            for (DomainDTO domainDTO : volunteerDTO.getDomains()) {
                domains.add(domainRepository.findByName(domainDTO.getDomain_name()));
            }
            volunteer.setDomains(domains);
        }
        else {
            volunteer.setDomains(volunteerRepository.getById(user.getId()).getDomains());
        }
        if (volunteerDTO.getPassword() != null) {
            volunteer.setPassword(passwordEncoder.encode(volunteerDTO.getPassword()));
        }
        else {
            volunteer.setPassword(volunteerRepository.getById(user.getId()).getPassword());
        }
        volunteer.setId(userRepository.findByUsername(name).getId());
//        log.info(volunteer);
        volunteerRepository.save(volunteer);
    }

    public void updateOrg(String name, OrganizationDTO orgDTO, Blob blob) {
        User user = userRepository.findByUsername(name);
        Organization old_org = organizationRepository.getById(user.getId());
        Organization organization = UserMapper.orgDTOToUser(orgDTO);
//        img
        if (blob == null) {
            organization.setLogo(old_org.getLogo());
        }
        else {
            organization.setLogo(blob);
        }
//        domains
        if (organization.getDomains() != null) {
            Set<Domain> domains = new HashSet<>();
            for (DomainDTO domainDTO: orgDTO.getDomains()) {
                domains.add(domainRepository.findByName(domainDTO.getDomain_name()));
            }
            organization.setDomains(domains);
        }
        else {
            organization.setDomains(old_org.getDomains());
        }
//        password
        if (organization.getPassword() != null) {
            organization.setPassword(passwordEncoder.encode(organization.getPassword()));
        }
        else {
            organization.setPassword(old_org.getPassword());
        }
        organization.setId(old_org.getId());
        organizationRepository.save(organization);

    }
}
