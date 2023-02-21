package edu.bbte.beavolunteerbackend.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "ORGANIZATION_DOMAIN")
public class OrganizationDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_domain_id")
    private Long organizationDomainId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANIZATION_ID")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOMAIN_ID")
    private Domain domain;

    public OrganizationDomain(Domain domain, Organization org) {
        this.domain = domain;
        this.organization = org;
    }
}
