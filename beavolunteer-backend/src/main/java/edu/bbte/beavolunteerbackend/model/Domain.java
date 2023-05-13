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
    @Column(name = "domain_id")
    private Long domainId;

    @Column(name = "DOMAIN_NAME", nullable = false)
    private String domainName;

    @Column(name = "DOMAIN_IMG")
    @Lob
    private Blob domainImg;

    public Domain(Long domainId, String domainName, Blob domainImg) {
        this.domainId = domainId;
        this.domainName = domainName;
        this.domainImg = domainImg;
    }

    public Domain() {
    }

    public void reset(){
        domainName = null;
        domainImg = null;
    }

}
