package edu.bbte.beavolunteerbackend.controller.mapper;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.OrganizationDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.UserDTO;
import edu.bbte.beavolunteerbackend.controller.dto.incoming.VolunteerDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.OrganizationOutDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.UserOutDTO;
import edu.bbte.beavolunteerbackend.model.Organization;
import edu.bbte.beavolunteerbackend.model.User;
import edu.bbte.beavolunteerbackend.model.Volunteer;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static <T extends UserDTO> User DTOToUser(T userDTO) {
        User user = new User();
        user.setUserName(userDTO.getUser());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        return user;
    }

    public static Volunteer volunteerDTOToVolunteer(VolunteerDTO userDTO, Long id) {
        Volunteer volunteer = new Volunteer();
//not sure if needed
        volunteer.setId(id);
        volunteer.setAge(userDTO.getAge());
        volunteer.setDescription(userDTO.getDescription());
        volunteer.setGender(userDTO.getGender());
        volunteer.setPhoneNr(userDTO.getPhoneNr());
        volunteer.setVolunteered(userDTO.getVolunteered());
        return volunteer;
    }

    public static Organization orgDTOToUser(OrganizationDTO organizationDTO, Long id) {
        Organization org = new Organization();
        //needed or auto?
        org.setId(id);
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
        orgDTO.setUser(userToDTO(user));
        orgDTO.setAddress(org.getAddress());
        orgDTO.setDescription(org.getDescription());
        orgDTO.setWebsite(org.getWebsite());
        orgDTO.setPhoneNr(org.getPhoneNr());
        orgDTO.setLogo("http://localhost:8080/org/image/" + org.getUserName());
        return orgDTO;
    }
}
