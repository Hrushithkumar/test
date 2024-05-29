package org.nitya.software.RealEstate.config;

import org.nitya.software.RealEstate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
//                .antMatchers("/users/login", "/users", "/projects/view", "/projects/upload", "/projects/images/**", "/projects",
//                         "/images/**", "/projects/categories",
//                        "/projects/**").permitAll()
                .antMatchers("/sample.html", "/login.html", "/users/login", "/static/**", "/users", "/project.html",
                        "/uploadproject.html", "/projects/view", "/projects/upload", "/projects/images/**", "/projects",
                        "/uploads/**", "/services.html", "/images/**", "/projects/categories",
                        "/projects/**", "/deleteproject.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/sample.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/services.html", true)
                .permitAll()
                .and()
                .logout()
                .permitAll();

        http.headers().frameOptions().sameOrigin(); // For H2 console
    }

    @Override
    public void configure(WebSecurity webSecurity){
        webSecurity.ignoring()
                .antMatchers("/resources/**", "/static/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
