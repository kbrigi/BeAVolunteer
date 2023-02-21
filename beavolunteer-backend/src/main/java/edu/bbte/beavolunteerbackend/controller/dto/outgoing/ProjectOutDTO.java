package edu.bbte.beavolunteerbackend.controller.dto.outgoing;

import edu.bbte.beavolunteerbackend.model.Organization;
import edu.bbte.beavolunteerbackend.model.Volunteer;
import lombok.Data;

import java.sql.Blob;
import java.util.Date;

@Data
public class ProjectOutDTO {
    String project_name;
    String project_description;
    String project_img;
    Date creation_date;
    Date expiration_date;
    Organization organization;
    Volunteer owner;
}
