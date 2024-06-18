package com.unla.grupo19.utils;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.unla.grupo19.entities.UserRole;
import com.unla.grupo19.repositories.IUserRepository;
import com.unla.grupo19.repositories.IUserRoleRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class Runners  implements CommandLineRunner{

    @Autowired
    private final IUserRoleRepository rolRepository;

    @Autowired
    private final IUserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {
        // TODO Auto-generated method stub
        if(this.rolRepository.count() == 0) {
            this.rolRepository.saveAll(
                    Arrays.asList(new UserRole(Roles.ROLE_ADMIN, LocalDateTime.now(), LocalDateTime.now()), new UserRole(Roles.ROLE_AUDITOR, LocalDateTime.now(), LocalDateTime.now())));
        }
        if(this.userRepository.count()==0) {
            this.userRepository.saveAll(Arrays.asList(new com.unla.grupo19.entities.User("admin", new BCryptPasswordEncoder().encode("1111"), true, LocalDateTime.now(), LocalDateTime.now(),this.rolRepository.findAll())));
        }
    }

}