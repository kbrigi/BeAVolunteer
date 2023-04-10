package edu.bbte.beavolunteerbackend.service;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.DomainDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.OrganizationDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.VolunteerDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.OrganizationOutDTO;
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
import java.util.List;

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
            volunteerRepository.insertVolunteer(volunteer.getId(), volunteer.getPhoneNr(), volunteer.getDescription(),
                    volunteer.getAge(), volunteer.getVolunteered(), String.valueOf(volunteer.getGender()));
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
//        return "ok";
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

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public OrganizationOutDTO getOrganization(Long id) {
        Organization org = organizationRepository.getById(id);
        return organizationToDTO(org, userRepository.getById(org.getId()));
    }

    public byte[] getImage(Long id) throws SQLException {
        Blob projectImg = organizationRepository.getById(id).getLogo();
        return getImg(projectImg);
    }

    public void saveOrganizationDomain(List<DomainDTO> domains, Organization org) {
//        OrganizationDomain orgDomain = new OrganizationDomain();
        for ( DomainDTO d : domains ) {
//            orgDomain.setDomain(domainRepository.findByName(d.getDomain_name()));
//            orgDomain.setOrganization(org);
            organizationDomainRepository.saveAndFlush(
                    new OrganizationDomain(domainRepository.findByName(d.getDomain_name()), org));
        }
    }
}
