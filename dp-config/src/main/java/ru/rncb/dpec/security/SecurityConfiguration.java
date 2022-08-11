package ru.rncb.dpec.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.GET,"/handbook/**").hasAnyRole("ADMIN", "EDITOR", "VISITOR")
                .and().authorizeRequests().antMatchers(HttpMethod.GET,"/permissions", "/permissions/**").hasAnyRole("ADMIN", "EDITOR", "VISITOR")
                .and().authorizeRequests().antMatchers(HttpMethod.GET,"/systems", "/systems/**").hasAnyRole("ADMIN", "EDITOR", "VISITOR")
                .and().authorizeRequests().antMatchers(HttpMethod.PUT,"/handbook/**").hasAnyRole("ADMIN", "EDITOR")
                .and().authorizeRequests().antMatchers(HttpMethod.PUT,"/permissions", "/permissions/**").hasAnyRole("ADMIN", "EDITOR")
                .and().authorizeRequests().antMatchers(HttpMethod.PUT,"/systems", "/systems/**").hasAnyRole("ADMIN", "EDITOR")
                .and().authorizeRequests().antMatchers(HttpMethod.POST,"/handbook/**").hasAnyRole("ADMIN", "EDITOR")
                .and().authorizeRequests().antMatchers(HttpMethod.POST,"/permissions", "/permissions/**").hasAnyRole("ADMIN", "EDITOR")
                .and().authorizeRequests().antMatchers(HttpMethod.POST,"/systems", "/systems/**").hasAnyRole("ADMIN", "EDITOR")
                .and().authorizeRequests().antMatchers(HttpMethod.DELETE,"/handbook/**").hasAnyRole("ADMIN", "EDITOR")
                .and().authorizeRequests().antMatchers(HttpMethod.DELETE,"/permissions", "/permissions/**").hasAnyRole("ADMIN", "EDITOR")
                .and().authorizeRequests().antMatchers(HttpMethod.DELETE,"/systems", "/systems/**").hasAnyRole("ADMIN", "EDITOR")
                .and().authorizeRequests().antMatchers("/users", "/users/**").hasAnyRole("ADMIN")
                .and().authorizeRequests().antMatchers("/menu").authenticated()
                .and().authorizeRequests().antMatchers("/page/login", "/css/**", "/js/**", "/actuator/*").permitAll()
                .and().formLogin().loginPage("/page/login").defaultSuccessUrl("/menu").failureUrl("/error/login")
                .and().rememberMe();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
