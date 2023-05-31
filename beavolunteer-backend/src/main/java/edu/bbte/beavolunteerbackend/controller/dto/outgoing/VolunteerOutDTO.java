package edu.bbte.beavolunteerbackend.controller.dto.outgoing;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.DomainDTO;
import lombok.Data;

import java.util.Set;

@Data
public class VolunteerOutDTO {
    String user;
    String email;
    String password;
    String surname;
    String firstname;
    Integer age;
    String description;
    String gender;
    String phoneNr;
    Boolean volunteered;
    Set<DomainDTO> domains;
}
