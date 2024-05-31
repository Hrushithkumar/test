package org.nitya.software.RealEstate.config;

import org.nitya.software.RealEstate.service.MyUserDetailsService;
import org.nitya.software.RealEstate.service.UserService;
import org.nitya.software.RealEstate.utils.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

//    @Autowired
//    J

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                /*.antMatchers("/users/login", "/users", "/projects/view", "/projects/upload", "/projects/images/**", "/projects",
//                         "/images/**", "/projects/categories",
//                        "/projects/**").permitAll()*/
//                .antMatchers("/sample.html", "/login.html", "/users/login", "/static/**", "/users", "/project.html",
//                        "/uploadproject.html", "/projects/view", "/projects/upload", "/projects/images/**", "/projects",
//                        "/uploads/**", "/services.html", "/images/**", "/projects/categories",
//                        "/projects/**", "/deleteproject.html").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/sample.html")
//                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/services.html", true)
//                .permitAll()
//                .and()
//               /*.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .addFilterBefore()*/
//                .logout()
//                .permitAll();
//
//        http.headers().frameOptions().sameOrigin(); // For H2 console
//    }
//
//    @Override
//    public void configure(WebSecurity webSecurity){
//        webSecurity.ignoring()
//                .antMatchers("/resources/**", "/static/**");
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter(){
//
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/authenticate").permitAll()
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .antMatchers("/sample.html", "/login", "/static/**", "/users", "/project.html",
                        "/uploadproject.html", "/projects/view", "/projects/upload", "/projects/images/**", "/projects",
                        "/uploads/**", "/images/**", "/projects/categories", "/services.html",
                        "/projects/**", "/deleteproject.html", "/register", "/index.html", "/css/**" , "/js/**").permitAll()
                //.antMatchers("/services.html").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/sample.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/services.html", true)
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    //protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                /*.antMatchers("/users/login", "/users", "/projects/view", "/projects/upload", "/projects/images/**", "/projects",
//                         "/images/**", "/projects/categories",
//                        "/projects/**").permitAll()*/
//                .antMatchers("/sample.html", "/login.html", "/users/login", "/static/**", "/users", "/project.html",
//                        "/uploadproject.html", "/projects/view", "/projects/upload", "/projects/images/**", "/projects",
//                        "/uploads/**", "/services.html", "/images/**", "/projects/categories",
//                        "/projects/**", "/deleteproject.html").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/sample.html")
//                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/services.html", true)
//                .permitAll()
//                .and()
//               /*.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .addFilterBefore()*/
//                .logout()
//                .permitAll();
//
//        http.headers().frameOptions().sameOrigin(); // For H2 console
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**" , "/js/**", "/index.html");
    }

}
