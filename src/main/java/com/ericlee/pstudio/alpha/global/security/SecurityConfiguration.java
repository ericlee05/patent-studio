package com.ericlee.pstudio.alpha.global.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 인증 관련
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/auth/**").permitAll();
        http.authorizeRequests().antMatchers("/sign-up").anonymous();
        http.formLogin().loginPage("/sign-in").permitAll();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        // static resources
        http.authorizeRequests()
                .antMatchers("/static/**").permitAll();

        // development
        http.authorizeRequests()
                .antMatchers("/development/**").permitAll();

        // other access
        http.authorizeRequests().anyRequest().authenticated();
    }
}
