package com.unforgettable.securitypart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unforgettable.securitypart.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Educator educator;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Student student;
}
