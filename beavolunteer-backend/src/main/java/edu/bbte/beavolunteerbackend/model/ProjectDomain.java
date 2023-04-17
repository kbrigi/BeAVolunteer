package edu.bbte.beavolunteerbackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper=false)
@Entity
@Data
@Table(name = "PROJECT_DOMAIN")
public class ProjectDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_domain_id")
    private Long projectDomainId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOMAIN_ID")
    private Domain domain;

    public ProjectDomain(Domain domain, Project project) {
        this.domain = domain;
        this.project = project;
    }

    public ProjectDomain() {

    }
}
