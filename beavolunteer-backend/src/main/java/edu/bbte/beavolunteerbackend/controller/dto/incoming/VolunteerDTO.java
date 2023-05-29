package edu.bbte.beavolunteerbackend.controller.dto.incoming;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class VolunteerDTO extends UserDTO{
    String surname;
    String firstname;
    Integer age;
    String description;
    String gender;
    String phoneNr;
    Boolean volunteered;
    Set<DomainDTO> domains;
}
