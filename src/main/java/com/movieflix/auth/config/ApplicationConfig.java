package com.movieflix.auth.config;

import com.movieflix.auth.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration  // Marks this class as a Spring configuration class (Java-based configuration).
public class ApplicationConfig {

    private final UserRepository userRepository;

    // Constructor injection of UserRepository, allowing the configuration class to access user data.
    public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean  // Declares a UserDetailsService bean for Spring to manage it in the application context.
    public UserDetailsService userDetailsService() {
        // Implements the UserDetailsService, which loads a user by username (in this case, email).
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        // Uses the UserRepository to search for the user by email. If not found, throws a UsernameNotFoundException.
    }

    @Bean  // Declares an AuthenticationProvider bean.
    public AuthenticationProvider authenticationProvider() {
        // Creates an instance of DaoAuthenticationProvider, which retrieves user details for authentication.
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        // Sets the UserDetailsService for the authentication provider to use when loading user details.
        authenticationProvider.setUserDetailsService(userDetailsService());
        // Sets the PasswordEncoder for the authentication provider to use when comparing passwords.
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean  // Declares an AuthenticationManager bean, which manages authentication across the application.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Retrieves the AuthenticationManager from the provided AuthenticationConfiguration.
        return config.getAuthenticationManager();
    }

    @Bean  // Declares a PasswordEncoder bean to handle password encoding in a secure manner.
    public PasswordEncoder passwordEncoder() {
        // Returns an instance of BCryptPasswordEncoder, a strong and widely used password hashing algorithm.
        return new BCryptPasswordEncoder();
    }
}

//package com.movieflix.auth.config;
//
//import com.movieflix.auth.repositories.UserRepository;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class ApplicationConfig {
//
//    private final UserRepository userRepository;
//
//    public ApplicationConfig(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> userRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
//    }
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService());
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
//
