package edu.bbte.beavolunteerbackend.controller.dto.incoming;

import edu.bbte.beavolunteerbackend.util.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VolunteerDTO extends UserDTO{
    Integer age;
    String description;
    Gender gender;
    String phoneNr;
    Boolean volunteered;
}
