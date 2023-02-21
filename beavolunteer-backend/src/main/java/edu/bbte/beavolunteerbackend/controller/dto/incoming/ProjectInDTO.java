package edu.bbte.beavolunteerbackend.controller.dto.incoming;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectInDTO {
    String project_name;
    String project_description;
    String project_img;
    Date expiration_date;
}
