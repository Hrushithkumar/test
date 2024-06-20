package org.nitya.software.RealEstate.config;

import org.nitya.software.RealEstate.security.JwtRequestFilter;
import org.nitya.software.RealEstate.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/authenticate").permitAll()
                .antMatchers( "/register").permitAll()
                .antMatchers("/admin/employee/register").permitAll()
                .antMatchers( "/employee/contacts").permitAll()
//                .antMatchers("/", "/static/**", "/images/**", "/main.html", "/custquoteslist.html","/empusermanagement.html",
//                        "/uploadhome.html","/deletehomes.html","/custcontactslist.html","/uploadproject.html",
//                        "/empsalgenerator.html", "/deleteprojects.html", "/servicerequestform.html", "/custservicereqlist.html",
//                        "/custquoteslist.html", "/header.html", "/Footer.html").permitAll()
                .antMatchers("/", "/static/**", "/images/**", "/css/**", "/js/**", "/uploads/**", "/includes/**").permitAll()
                .antMatchers("/*.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    System.out.println("Access Denied: " +  accessDeniedException.getMessage());
                    response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied");
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    System.out.println("Authentication Denied: " + authException.getMessage());
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
                })
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**" , "/js/**", "/index.html", "/images/**", "/uploads/**", "/static/**", "/includes/**");
    }

}