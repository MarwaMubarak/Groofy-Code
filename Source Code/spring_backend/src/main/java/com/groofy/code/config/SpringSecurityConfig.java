//package com.groofy.code.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//public class SpringSecurityConfig {
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http.httpBasic(withDefaults());
////        return http.build();
////    }
//
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//        User admin = (User) User.withUsername("admin")
//                .password(encoder.encode("password"))
//                .roles("USER").build();
//        return new InMemoryUserDetailsManager(admin);
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        // Encoder method used to encode the password
//        return new BCryptPasswordEncoder();
//    }
//}