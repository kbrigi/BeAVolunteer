package edu.bbte.beavolunteerbackend.model;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@Table(name = "PROJECT")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID")
    private Long projectId;

    @Column(name = "PROJECT_NAME", nullable = false, unique = true)
    private String projectName;

    @Column(name = "PROJECT_DESCRIPTION")
    private String projectDescription;

    @Column(name = "PROJECT_IMG")
    @Lob
    private Blob projectImg;

    @Column(name = "organization_id")
    private Long organizationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", insertable = false, updatable = false)
    private Organization organization;

    @Column(name = "owner_id")
    private Long ownerId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private Volunteer owner;

    @Column(nullable = false)
    private Date creationDate;

    @Column()
    private Date expirationDate;
}
