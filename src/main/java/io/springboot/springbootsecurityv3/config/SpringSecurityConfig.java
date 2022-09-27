package io.springboot.springbootsecurityv3.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    public static Logger logger= LoggerFactory.getLogger(SpringSecurityConfig.class);

    @Autowired
    private PasswordConfig passwordConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //SecureUserRole.PRODUCT_OWNER.name(),SecureUserRole.TESTER.name(),
        http.cors().disable();
        http.csrf().disable();

        //AUTHORITY(PERMISSION) BASED AUTHORIZATION
        http.authorizeRequests()
                .antMatchers("/index.html","/css/*","/js/*").permitAll()//api whitelisting
                //.antMatchers(HttpMethod.POST).hasAnyAuthority(SecurePermission.PRODUCT_WRITE.getPermission())//while using @Preauthorized
                //.antMatchers(HttpMethod.GET).hasAnyAuthority(SecurePermission.PRODUCT_READ.getPermission())
                //.antMatchers(HttpMethod.PUT).hasAnyAuthority(SecurePermission.PRODUCT_UPDATE.getPermission())
                //.antMatchers(HttpMethod.DELETE).hasAnyAuthority(SecurePermission.PRODUCT_DELETE.getPermission())
                .antMatchers("/api/v3/products/**").hasAnyRole(SecureUserRole.DEVELOPER.name(),SecureUserRole.PRODUCT_OWNER.name(),SecureUserRole.TESTER.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
                //.and()
                //.exceptionHandling()
                //.accessDeniedPage("/accessDenied.html");



        /**ROLE BASED AUTHORIZATION
        http.authorizeRequests()
                .antMatchers("/index.html","/css/*","/js/*").permitAll()//api whitelisting
                .antMatchers(HttpMethod.POST).hasRole(SecureUserRole.DEVELOPER.name())
                .antMatchers(HttpMethod.GET).hasRole(SecureUserRole.DEVELOPER.name())
                .antMatchers(HttpMethod.PUT).hasRole(SecureUserRole.TESTER.name())
                .antMatchers(HttpMethod.DELETE).hasRole(SecureUserRole.PRODUCT_OWNER.name())
                .antMatchers("/api/v3/products/**").hasAnyRole(SecureUserRole.DEVELOPER.name(),SecureUserRole.PRODUCT_OWNER.name(),SecureUserRole.TESTER.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
        */

    }

    //to supply authentication.authorization details
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
