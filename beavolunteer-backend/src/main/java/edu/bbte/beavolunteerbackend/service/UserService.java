package edu.bbte.beavolunteerbackend.service;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.DomainDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.OrganizationDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.VolunteerDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.OrganizationOutDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.ProjectOutDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.UserOutDTO;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void saveUser(VolunteerDTO userDTO) throws BusinessException {
        User user = DTOToUser(userDTO);
        UserValidator.errorList.clear();
        userValidator.validate(user);
        log.info(UserValidator.errorList.toString());
        if (UserValidator.errorList.isEmpty()) {
            user.setRole(Role.USER);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            User savedUser = userRepository.saveAndFlush(user);

            Volunteer volunteer = volunteerDTOToVolunteer(userDTO, savedUser.getId());
            volunteerRepository.insertVolunteer(volunteer.getId(), volunteer.getSurname(),
                    volunteer.getFirstName(), volunteer.getPhoneNr(), volunteer.getDescription(),
                    volunteer.getAge(), volunteer.getVolunteered(), String.valueOf(volunteer.getGender()));
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
            Organization org = orgDTOToUser(organizationDTO, null);
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

    public void deleteOrg(String name) throws BusinessException {
        User user = userRepository.findByUsername(name);
        if (user != null) {
            List<Project> projects = projectRepository.getByOwner(user.getId());
            for (Project project: projects) {
                projectRepository.delete(project);
            }
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

    public void setFavouriteProject(String username, String projectName) {
        Volunteer volunteer = volunteerRepository.getById(userRepository.findByUsername(username).getId());
        Set<Project> favourites = volunteer.getFavouriteProj();
        favourites.add(projectRepository.getByName(projectName));
        volunteer.setFavouriteProj(favourites);
    }

    public void removeFavouriteProject(String username, String projectName) {
        Volunteer volunteer = volunteerRepository.getById(userRepository.findByUsername(username).getId());
        Set<Project> favourites = volunteer.getFavouriteProj();
        favourites.remove(projectRepository.getByName(projectName));
        volunteer.setFavouriteProj(favourites);
    }

    public List<ProjectOutDTO> getFavouriteProject(String username) {
        return ProjectMapper.projectsToDTO((List<Project>) volunteerRepository.getById(userRepository.findByUsername(username).getId()).getFavouriteProj());
    }
}
