package edu.bbte.beavolunteerbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "VOLUNTEER")
//@OnDelete(action = OnDeleteAction.CASCADE)@PrimaryKeyJoinColumn(name="EMPLOYEE_ID")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class Volunteer extends User {
//    @Id
//    @Column(unique = true, nullable = false)
//    private Long id;

    @Pattern(regexp = "^[A-Z][a-z]{2,}$")
    @Column(name = "SURNAME", nullable = false)
    private String surname;

    @Pattern(regexp = "^[A-Z][a-z]{2,}$")
    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Pattern(regexp = "^[0][7][0-9]{8}$")
    @Column(name = "PHONE_NR", length = 10)
    private String phoneNr;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "AGE")
    private Integer age;

    @Column(name = "VOLUNTEERED")
    private Boolean volunteered;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER")
    private Gender gender;

    @JsonIgnore
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Collection<Project> projects;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<Project> favouriteProj = new HashSet<>();
}
