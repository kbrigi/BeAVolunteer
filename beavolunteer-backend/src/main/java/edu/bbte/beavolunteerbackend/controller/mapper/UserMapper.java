package edu.bbte.beavolunteerbackend.controller.mapper;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.OrganizationDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.UserDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.VolunteerDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.OrganizationOutDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.UserOutDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.VolunteerOutDTO;
import edu.bbte.beavolunteerbackend.model.*;
import edu.bbte.beavolunteerbackend.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    @Autowired
    private static UserRepository userRepository;

    public static <T extends UserDTO> User DTOToUser(T userDTO) {
        User user = new User();
        user.setUserName(userDTO.getUser());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        return user;
    }

    public static Volunteer volunteerDTOToVolunteer(VolunteerDTO userDTO) {
        Volunteer volunteer = new Volunteer();
        volunteer.setUserName(userDTO.getUser());
        volunteer.setEmail(userDTO.getEmail());
        volunteer.setRole(Role.USER);
        volunteer.setFirstName(userDTO.getFirstname());
        volunteer.setSurname(userDTO.getSurname());
        volunteer.setAge(userDTO.getAge());
        volunteer.setDescription(userDTO.getDescription());
        String gender = userDTO.getGender();
        volunteer.setGender(Gender.valueOf(gender));
        volunteer.setPhoneNr(userDTO.getPhoneNr());
        volunteer.setVolunteered(userDTO.getVolunteered());
        return volunteer;
    }

    public static Volunteer volunteerExtendedDTOToVolunteer(VolunteerOutDTO userDTO) {
        Volunteer volunteer = new Volunteer();
        volunteer.setUserName(userDTO.getUser());
        volunteer.setEmail(userDTO.getEmail());
        volunteer.setRole(Role.USER);
        volunteer.setFirstName(userDTO.getFirstname());
        volunteer.setSurname(userDTO.getSurname());
        volunteer.setAge(userDTO.getAge());
        volunteer.setDescription(userDTO.getDescription());
        volunteer.setPhoneNr(userDTO.getPhoneNr());
        volunteer.setVolunteered(userDTO.getVolunteered());
        return volunteer;
    }

    public static Organization orgDTOToUser(OrganizationDTO organizationDTO) {
        Organization org = new Organization();
        org.setUserName(organizationDTO.getUser());
        org.setRole(Role.ORGANIZATION);
        org.setEmail(organizationDTO.getEmail());
        org.setDescription(organizationDTO.getDescription());
        org.setPhoneNr(organizationDTO.getPhoneNr());
        org.setAddress(organizationDTO.getAddress());
        org.setWebsite(organizationDTO.getWebsite());
        return org;
    }

    public static UserOutDTO userToDTO(User user) {
        UserOutDTO userDTO = new UserOutDTO();
        userDTO.setUser(user.getUserName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public static List<UserOutDTO> usersToDTO(List<User> user) {
        return user.stream().map(UserMapper::userToDTO).collect(Collectors.toList());
    }


    public static OrganizationOutDTO organizationToDTO(Organization org, User user) {
        OrganizationOutDTO orgDTO = new OrganizationOutDTO();
        orgDTO.setUser(user.getUserName());
        orgDTO.setEmail(user.getEmail());
        orgDTO.setAddress(org.getAddress());
        orgDTO.setDescription(org.getDescription());
        orgDTO.setWebsite(org.getWebsite());
        orgDTO.setPhoneNr(org.getPhoneNr());
        orgDTO.setLogo("http://localhost:8080/org/image/" + org.getUserName());
        return orgDTO;
    }

    public static VolunteerOutDTO volunteerToDTO(Volunteer volunteer, User user) {
        VolunteerOutDTO volunteerDTO = new VolunteerOutDTO();
        volunteerDTO.setUser(user.getUserName());
        volunteerDTO.setEmail(user.getEmail());
        volunteerDTO.setSurname(volunteer.getSurname());
        volunteerDTO.setFirstname(volunteer.getFirstName());
        volunteerDTO.setDescription(volunteer.getDescription());
        volunteerDTO.setVolunteered(volunteer.getVolunteered());
        volunteerDTO.setPhoneNr(volunteer.getPhoneNr());
        volunteerDTO.setAge(volunteer.getAge());
        return volunteerDTO;
    }

}
