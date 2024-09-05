package com.review.monkey.security.config;

import com.review.monkey.security.exception.AppException;
import com.review.monkey.security.exception.ErrorCode;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
// CONFIG TO AUTHORIZAION ON FUNCTION
@EnableMethodSecurity
public class SecurityConfig {
    protected String[] PUBLIC_ENDPOINTS = {
            "/user","/user/search-and-sort","/user/clean-cache",
            "/auth/log-in", "/auth/introspect" , "/auth/logout", "/auth/refresh-token"
    };

    @Autowired
    @Lazy
    private CustomeJwtDecoder customeJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                .anyRequest().authenticated());
        http.oauth2ResourceServer(oauth2 ->
                                  oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customeJwtDecoder)
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                          // CONFIG ERROR 401
                                          .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }


    // CUSTOM SCOPE_ TO ROLE_
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
