package com.example.firstwork.service;

import com.example.firstwork.entites.Roles;
import com.example.firstwork.entites.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

    public interface UserService extends UserDetailsService {

        Users getUserByEmail(String email);
        boolean saveUser(Users user);
        Users editUser(Users user);
        Users getUserById(Long id);
        List<Users> getAllUsers();
        List<Roles> getAllRoles();
        Roles getRoleByName(String role);
        Roles addRole(Roles roles);
        Roles getRole(Long id);
        void deleteUser(Users user);

    }

