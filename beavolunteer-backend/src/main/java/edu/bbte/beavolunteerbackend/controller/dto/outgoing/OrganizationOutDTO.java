package edu.bbte.beavolunteerbackend.controller.dto.outgoing;

import lombok.Data;

@Data
public class OrganizationOutDTO {
    UserOutDTO user;
    String description;
    String address;
    String website;
    String logo;
    String phoneNr;
}
