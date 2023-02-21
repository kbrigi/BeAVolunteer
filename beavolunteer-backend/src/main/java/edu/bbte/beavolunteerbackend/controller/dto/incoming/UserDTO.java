package edu.bbte.beavolunteerbackend.controller.dto.incoming;

import lombok.Data;

@Data
public class UserDTO {
    String user;
    String email;
    String password;
}
