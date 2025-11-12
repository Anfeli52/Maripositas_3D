//package com.andres.mariposita3d;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
            // Temporarily disabled to avoid interfering with login development
            // All requests are allowed without authentication
//            .csrf().disable()
//            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//            .formLogin().disable()
//            .httpBasic().disable();

//        return http.build();
//    }
//}

