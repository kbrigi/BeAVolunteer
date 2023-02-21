package edu.bbte.beavolunteerbackend.controller.dto.outgoing;

import edu.bbte.beavolunteerbackend.util.Role;
import lombok.Data;

@Data
public class UserOutDTO {
    String user;
    String email;
    Role role;
}
