package edu.bbte.beavolunteerbackend.controller.dto.incoming;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VolunteerDTO extends UserDTO{
    Integer age;
    String description;
    String gender;
    String phoneNr;
    Boolean volunteered;
}
