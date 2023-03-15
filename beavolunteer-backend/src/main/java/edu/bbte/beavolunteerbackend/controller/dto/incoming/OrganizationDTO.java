package edu.bbte.beavolunteerbackend.controller.dto.incoming;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrganizationDTO extends UserDTO{
    String description;
    String address;
    String website;
//    String logo;
    String phoneNr;
    List<DomainDTO> domains;
}
