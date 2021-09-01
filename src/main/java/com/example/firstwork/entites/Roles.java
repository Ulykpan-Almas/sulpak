package com.example.firstwork.entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "t_roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Roles implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "role")
    private String role;


}
