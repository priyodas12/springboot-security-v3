package io.springboot.springbootsecurityv3.config;

import io.springboot.springbootsecurityv3.controller.ProductController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    public static Logger logger= LoggerFactory.getLogger(SpringSecurityConfig.class);

    @Autowired
    private PasswordConfig passwordConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/","index","/css/*","/js/*")//api whitelisting
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    //to supply authentication details
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails abc= User.builder()
                                    .username("abc")
                                    .password(passwordConfig.getPasswordEncoder()
                                                            .encode("abc123"))
                                    .roles("DEV")
                                    .build();
        logger.info("encrypted password: {}, authorities: {}",abc.getPassword(),abc.getAuthorities());
        return new InMemoryUserDetailsManager(abc);
    }


}
