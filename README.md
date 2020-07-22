# oauth-resource-server-migration
This repository contains the details to convert a normal spring boot web application to a oauth2 resource server.

Step 1:

In pom.xml 
	Add below spring cloud version property
		<properties>
			<spring-cloud.version>Finchley.RELEASE</spring-cloud.version>
		</properties>
		
	Add below spring cloud oauth2 dependency
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-oauth2</artifactId>
		</dependency> 
		
	Add spring cloud dependency management
		<dependencyManagement>
			<dependencies>
				<dependency>
					<groupId>org.springframework.cloud</groupId>
					<artifactId>spring-cloud-dependencies</artifactId>
					<version>${spring-cloud.version}</version>
					<type>pom</type>
					<scope>import</scope>
				</dependency>
			</dependencies>
		</dependencyManagement>
    
Step 2:

	Add OuthConfig.java to config package
   
   
Step 3:
	
	Add SimpleCorsFilter.java to config package
	
Step 4:

	Add @EnableResourceServer to Application.java
	
	Add below part to the Application.java
	
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
   	    
Step 5:

	Add HealthCheckResource.java in resources package
