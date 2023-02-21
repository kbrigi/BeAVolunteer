package edu.bbte.beavolunteerbackend.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Blob;

@Data
@Entity
@Table(name = "DOMAIN")
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DOMAIN_ID")
    private Long domainId;

    @Column(name = "DOMAIN_NAME", nullable = false)
    private String domainName;

    @Column(name = "DOMAIN_IMG")
    @Lob
    private Blob domainImg;

    public void reset(){
        domainName = null;
        domainImg = null;
    }
}
