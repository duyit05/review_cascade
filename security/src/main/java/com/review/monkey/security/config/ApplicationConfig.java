package com.review.monkey.security.config;

import com.review.monkey.security.enums.Roles;
import com.review.monkey.security.model.User;
import com.review.monkey.security.repository.UserRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@Slf4j
public class ApplicationConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    // WHEN THE FIRST START APPLICATION THIS CLASS WILL START
    @Bean
    public  ApplicationRunner applicationRunner (UserRespository userRespository) {

        return args -> {
            if(userRespository.findByUsername("admin").isEmpty()){
                HashSet<String> roles = new HashSet<>();
                roles.add(Roles.ADMIN.name());
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRespository.save(user);
                log.info("ADMIN USER HAS BEEN CREATED WITH DEFAULT PASSWORD : admin , PLEASE CHANGE IT !");
            }
        };
    }

}
