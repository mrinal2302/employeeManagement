package com.example.employeemanagement.config;

import com.example.employeemanagement.entity.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.employeemanagement.repository.EmployeeRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/login", "/register", "/css/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/employees", true)
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Employee employee = employeeRepository.findByEmail(username);
            if (employee != null) {
                return User.withUsername(employee.getEmail())
                        .password(employee.getPassword())
                        .build();
            } else {
                throw new UsernameNotFoundException("Employee not found with email: " + username);
            }
        };
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
