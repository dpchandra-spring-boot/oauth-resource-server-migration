package com.saddlepoint;

import com.saddlepoint.config.SimpleCORSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
@EnableResourceServer
@SpringBootApplication(scanBasePackages = {
    "com.saddlepoint.context",
    "com.saddlepoint.config",
    "com.saddlepoint"})
@EnableJpaRepositories(basePackages = {"com.saddlepoint.persistence.jpa",
    "com.saddlepoint.persistence.jpa.impl",
    "com.saddlepoint.persistence.procedures"})
@EntityScan({"com.saddlepoint.persistence.entity",
    "com.saddlepoint.persistence.openfire.entity"})
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${security.oauth2.client.accessTokenUri}")
    private String accessTokenUri;

    @Value("${security.oauth2.client.userAuthorizationUri}")
    private String userAuthorizationUri;

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    /**
     * The heart of our interaction with the resource; handles redirection for
     * authentication, access tokens, etc.
     *
     * @param oauth2ClientContext
     * @return
     */
    @Bean
    public OAuth2RestOperations restTemplate(OAuth2ClientContext oauth2ClientContext) {
        return new OAuth2RestTemplate(resource(), oauth2ClientContext);
    }

    private OAuth2ProtectedResourceDetails resource() {
        AuthorizationCodeResourceDetails resource = new AuthorizationCodeResourceDetails();
        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);
        resource.setAccessTokenUri(accessTokenUri);
        resource.setUserAuthorizationUri(userAuthorizationUri);
//        resource.setScope(Arrays.asList("read"));
        return resource;
    }

    @Bean
    public FilterRegistrationBean<SimpleCORSFilter> loggingFilter() {
        FilterRegistrationBean<SimpleCORSFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new SimpleCORSFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
