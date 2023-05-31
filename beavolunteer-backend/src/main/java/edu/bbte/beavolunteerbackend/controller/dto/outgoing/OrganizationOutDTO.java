package edu.bbte.beavolunteerbackend.controller.dto.outgoing;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.DomainDTO;
import lombok.Data;

import java.util.List;

@Data
public class OrganizationOutDTO {
    String user;
    String email;
    String description;
    String address;
    String website;
    String logo;
    String phoneNr;
    List<DomainDTO> domains;
}
