package edu.bbte.beavolunteerbackend.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "VOLUNTEER_DOMAIN")
public class VolunteerDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VolunteerDomainId")
    private Long volunteerDomainId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VOLUNTEER_ID")
    private Volunteer volunteer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOMAIN_ID")
    private Domain domain;

}
