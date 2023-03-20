package edu.bbte.beavolunteerbackend.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @NotBlank(message = "username should not be blank")
    @Size(min = 5, message = "username should be at least 3 chars")
    @Size(max = 15, message = "username should be at most 15 chars")
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @NotBlank(message = "email should not be blank")
    @Email(message = "not a valid email address")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", length  = 64, nullable = false)
    private String password;

//    @Transient
    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
    private Role role;

}
