package com.example.firstwork.service.impl;

import com.example.firstwork.entites.Roles;
import com.example.firstwork.entites.Users;
import com.example.firstwork.repositories.RoleRepository;
import com.example.firstwork.repositories.UserRepository;
import com.example.firstwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users myUser = userRepository.findByEmail(s);

        if (myUser != null){
            User secUser = new User(myUser.getEmail(), myUser.getPassword(), myUser.getRoles());
            return secUser;
        }


        throw new UsernameNotFoundException("User Not Found");
    }

    @Override
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

//    public boolean deleteUser(Long userId) {
//        Users user = userRepository.getOne(userId);
//        if (user != null){
//            userRepository.delete(user);
//            return true;
//        }
//        return false;
//    }


    @Override
    public Users editUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    public boolean saveUser(Users user) {
        Users userFromDb = userRepository.findByEmail(user.getEmail());
        if (userFromDb != null){
            return false;
        }

        List<Roles> roles = new ArrayList<>();
        roles.add(roleRepository.findByRole("ROLE_USER"));
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Roles addRole(Roles roles) {
        return roleRepository.save(roles);
    }

    @Override
    public Roles getRoleByName(String role) {
        return roleRepository.findByRole(role);
    }

    @Override
    public List<Roles> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Roles getRole(Long id) {
        return roleRepository.getOne(id);
    }

    @Override
    public Users getUserById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public void deleteUser(Users user) {
        userRepository.delete(user);
    }
}
