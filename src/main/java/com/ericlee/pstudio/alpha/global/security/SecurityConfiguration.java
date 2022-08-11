package com.ericlee.pstudio.alpha.global.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // static resources
        http.authorizeRequests()
                .antMatchers("/static/**").permitAll();

        // development
        http.authorizeRequests()
                .antMatchers("/templates/**").permitAll();
    }
}
