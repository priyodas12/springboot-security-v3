package io.springboot.springbootsecurityv3.config;

import io.springboot.springbootsecurityv3.controller.ProductController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    public static Logger logger= LoggerFactory.getLogger(SpringSecurityConfig.class);

    @Autowired
    private PasswordConfig passwordConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//SecureUserRole.PRODUCT_OWNER.name(),SecureUserRole.TESTER.name(),
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/index.html","/css/*","/js/*").permitAll()//api whitelisting
                .antMatchers("/api/v3/products/{id}").hasRole(SecureUserRole.DEVELOPER.name())
                .antMatchers(HttpMethod.POST,"/api/v3/products/").hasAuthority(SecurePermission.PRODUCT_WRITE.name())
                .antMatchers(HttpMethod.GET,"/api/v3/products/{id}").hasAuthority(SecurePermission.PRODUCT_READ.name())
                .antMatchers("/api/v3/products/").hasRole(SecureUserRole.TESTER.name())
                .antMatchers(HttpMethod.PUT,"/api/v3/products/").hasAuthority(SecurePermission.PRODUCT_MODIFY.name())
                .antMatchers("/api/v3/products/{id}").hasRole(SecureUserRole.PRODUCT_OWNER.name())
                .antMatchers(HttpMethod.DELETE,"/api/v3/products/{id}").hasAuthority(SecurePermission.PRODUCT_DELETE.name())
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
                                    .password(passwordConfig.getPasswordEncoder().encode("abc123"))
                                    .authorities(SecureUserRole.DEVELOPER.getGrantedAuthority())
                                    .build();

        logger.info("abc encrypted password: {}, authorities: {}",abc.getPassword(),abc.getAuthorities());

        UserDetails mno= User.builder()
                                    .username("mno")
                                    .password(passwordConfig.getPasswordEncoder().encode("mno123"))
                                    .authorities(SecureUserRole.TESTER.getGrantedAuthority())
                                    .build();

        logger.info("mno encrypted password: {}, authorities: {}",mno.getPassword(),mno.getAuthorities());

        UserDetails xyz= User.builder()
                                    .username("xyz")
                                    .password(passwordConfig.getPasswordEncoder().encode("xyz123"))
                                    .authorities(SecureUserRole.PRODUCT_OWNER.getGrantedAuthority())
                                    .build();

        logger.info("xyz encrypted password: {}, authorities: {}",xyz.getPassword(),xyz.getAuthorities());


        return new InMemoryUserDetailsManager(abc,xyz,mno);
    }


}
