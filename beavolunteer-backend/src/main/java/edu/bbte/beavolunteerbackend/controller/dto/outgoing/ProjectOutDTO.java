package edu.bbte.beavolunteerbackend.controller.dto.outgoing;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.DomainDTO;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class ProjectOutDTO {
    String project_name;
    String project_description;
    String project_img;
    Date creation_date;
    Date expiration_date;
    UserOutDTO owner;
    List<DomainDTO> domains;
}
