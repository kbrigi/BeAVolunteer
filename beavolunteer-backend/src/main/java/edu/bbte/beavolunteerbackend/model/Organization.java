package edu.bbte.beavolunteerbackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.sql.Blob;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<Domain> domains = new HashSet<>();
}
