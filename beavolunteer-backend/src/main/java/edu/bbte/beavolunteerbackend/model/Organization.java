package edu.bbte.beavolunteerbackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.sql.Blob;
import java.util.Collection;

@EqualsAndHashCode(callSuper=false)
@Entity
@Data
@Table(name = "ORGANIZATION")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
//@OnDelete(action = OnDeleteAction.CASCADE)
public class Organization extends User {

    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    @Pattern(regexp = "^[0][7][0-9]{8}$")
    @Column(name = "PHONE_NR", length = 10)
    private String phoneNr;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LOGO")
    @Lob
    private Blob logo;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "WEBSITE")
    private String website;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    private Collection<Project> projects;

//    @MapsId
//    @OneToOne
//    @JoinColumn(name = "id")
//    private User user;

}
