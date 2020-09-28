package com.whitedwaft.devservice.configs;

import com.whitedwaft.devservice.JwtTokenProvider;
import com.whitedwaft.devservice.services.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsService userDetailsService = mongoUserDetails();
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable().csrf().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .antMatchers("/logon**").permitAll().antMatchers("/register**").permitAll()
                .antMatchers("/welcome**").hasAuthority("ADMIN")
                .antMatchers("/start**").permitAll()
                .antMatchers("/git**").permitAll()
                .antMatchers("/clone**").permitAll()
                .antMatchers("/send_message**").permitAll()
                .antMatchers("/consume_message**").permitAll()
                .antMatchers("/get_history**").permitAll()
                .antMatchers("/log**").permitAll().anyRequest().authenticated().and().csrf()
                .disable().exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint()).and().
                cors().and()
                .x509()
                .subjectPrincipalRegex("CN=(.*?),")
                .userDetailsService(userDetailsService()).and()
                .apply(new JwtConfigurer(jwtTokenProvider))
        ;
//        http.authorizeRequests().antMatchers("/**").permitAll();
    }
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.httpBasic().disable().csrf().disable().sessionManagement()
//               .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
//                .antMatchers("/logon**").permitAll().antMatchers("/register**").permitAll()
//                .antMatchers("/welcome**").hasAuthority("ADMIN")
//                .antMatchers("/git**").permitAll()
//                .anyRequest().authenticated().and()
////                .cors().and()
////                    .apply(new JwtConfigurer(jwtTokenProvider)).and()
//                    .oauth2Login();
//        }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }


    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized");
    }

    @Bean
    public UserDetailsService mongoUserDetails() {
        return new MongoService();
    }
}
