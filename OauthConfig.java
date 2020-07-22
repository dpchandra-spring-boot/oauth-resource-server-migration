package com.saddlepoint.config;

import javax.ws.rs.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@Configuration
public class OauthConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.oauth2.client.logoutUri}")
    private String logoutUri;
    
     @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/check","/token");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .antMatchers("/", "/login**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .clearAuthentication(true)
                .logoutUrl("/logout")
                .deleteCookies("SP_SESSION")
                .invalidateHttpSession(true)
                .logoutSuccessUrl(logoutUri)
                .and()
                .cors()
                .and()
                .csrf().disable();

    }

}
