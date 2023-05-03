package edu.bbte.beavolunteerbackend.service;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.DomainDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.OrganizationDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.VolunteerDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.OrganizationOutDTO;
import edu.bbte.beavolunteerbackend.controller.mapper.DomainMapper;
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
import java.util.List;
import java.util.Optional;

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
    private OrganizationDomainRepository organizationDomainRepository;

    @Autowired
    private VolunteerDomainRepository volunteerDomainRepository;

    @Autowired
    private JWTToken jwToken;

    public void saveUser(VolunteerDTO userDTO) throws BusinessException {
        User user = DTOToUser(userDTO);
        UserValidator.errorList.clear();
        userValidator.validate(user);
        log.info(UserValidator.errorList.toString());
        if (UserValidator.errorList.isEmpty()) {
            user.setRole(Role.USER);
            User savedUser = userRepository.saveAndFlush(user);

            Volunteer volunteer = volunteerDTOToVolunteer(userDTO, savedUser.getId());
            volunteerRepository.insertVolunteer(volunteer.getId(), volunteer.getSurname(),
                    volunteer.getFirstName(), volunteer.getPhoneNr(), volunteer.getDescription(),
                    volunteer.getAge(), volunteer.getVolunteered(), String.valueOf(volunteer.getGender()));
            saveVolunteerDomain(userDTO.getDomains(), savedUser.getId());
        } else {
            throw new BusinessException(UserValidator.errorList.toString());
        }
    }

//check if login data(email+password) is correct
    public String matchUser(String email, String password) throws BusinessException {
        User user = userRepository.matchEmail(email);

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
        log.info(UserValidator.errorList.toString());
        if (UserValidator.errorList.isEmpty()) {
            user.setRole(Role.ORGANIZATION);
            User savedUser = userRepository.saveAndFlush(user);

            Organization org = orgDTOToUser(organizationDTO, savedUser.getId());
            org.setLogo(file);
            organizationRepository.insertOrg(org.getId(), org.getAddress(),
                    org.getDescription(), org.getPhoneNr(), org.getLogo(), org.getWebsite());

            saveOrganizationDomain(organizationDTO.getDomains(), org);
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
            OrganizationOutDTO orgDTO = UserMapper.organizationToDTO(org, userRepository.getById(org.getId()));

            List <DomainDTO> domainDTOS = getDomains(org.getId());
            orgDTO.setDomains(domainDTOS);
            organizationOutDTOS.add(orgDTO);
        }
        return organizationOutDTOS;
    }

    public OrganizationOutDTO getOrganization(Long id) {
        Organization org = organizationRepository.getById(id);
        return organizationToDTO(org, userRepository.getById(org.getId()));
    }

    public byte[] getImage(String username) throws SQLException {
        Blob projectImg = organizationRepository.getById(userRepository.matchUser(username).getId()).getLogo();
        return getImg(projectImg);
    }

    public void saveOrganizationDomain(List<DomainDTO> domains, Organization org) {
        for ( DomainDTO d : domains ) {
            organizationDomainRepository.saveAndFlush(
                    new OrganizationDomain(domainRepository.findByName(d.getDomain_name()), org));
        }
    }

    public void saveVolunteerDomain(List<DomainDTO> domains, Long userId) {
        for ( DomainDTO d : domains ) {
            volunteerDomainRepository.insertVolunteerDomain(userId, domainRepository.findByName(d.getDomain_name()).getDomainId());
        }
    }

    public User getUserFromUsername(String username) {
        return userRepository.matchUser(username);
    }

    public OrganizationOutDTO getOrg(String username) throws BusinessException {
        Long id = getUserFromUsername(username).getId();
        if (id != null) {
            OrganizationOutDTO organizationOutDTO =  organizationToDTO(organizationRepository.getById(id), userRepository.getById(id));
            organizationOutDTO.setDomains(getDomains(id));
            return organizationOutDTO;
        }
        else {
            throw new BusinessException("Error: this user does not exist!");
        }
    }

    public List <DomainDTO> getDomains(Long id) {
        List <Long> domain_ids =  organizationDomainRepository.findDomainsByOrg(id);
        List <DomainDTO> domainDTOS = new ArrayList<>();
        for (Long did : domain_ids) {
            Optional<Domain> domain = domainRepository.findById(did);
            if (domain.isPresent()) {
                DomainDTO domainDTO = DomainMapper.domainToDTO(domain.get());
                domainDTOS.add(domainDTO);
            }
        }
        return domainDTOS;
    }
}
