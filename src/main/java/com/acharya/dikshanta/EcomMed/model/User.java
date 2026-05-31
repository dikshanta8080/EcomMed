package com.acharya.dikshanta.EcomMed.model;

import com.acharya.dikshanta.EcomMed.utils.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "unique_email", columnNames = {"email"})
})
public class User extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
}
