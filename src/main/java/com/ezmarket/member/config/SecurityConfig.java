package com.ezmarket.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((authorizeHttpRequests)-> authorizeHttpRequests
                .requestMatchers("/items/*").permitAll()
                .requestMatchers("/css/**","/Js/**","/images/**").permitAll()
                .requestMatchers("/members/**","/").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        http.formLogin((auth)->auth
                        .loginPage("/members/login")
                        .usernameParameter("email")
                        .loginProcessingUrl("/members/loginProc")
                        .defaultSuccessUrl("/")
                        .permitAll()
                );

        http.sessionManagement((auth)-> auth
                // 동시접속 최대 로그인 수
                .maximumSessions(1)
                // 기존 세션 만료
                .maxSessionsPreventsLogin(false)
        );

        http.sessionManagement((auth)-> auth
                .sessionFixation().changeSessionId());

        http.logout((auth)-> auth
                .logoutUrl("/members/logout")
                .logoutSuccessUrl("/"));

        return http.build();
    }




}
