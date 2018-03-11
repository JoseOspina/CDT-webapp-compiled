package cdt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;

@Component
public class Auth0Management {
	
	@Value("${auth0.issuer}")
    String issuer;
	
	@Value("${auth0.audience}")
    String audience;
	
	@Value("${auth0.secret}")
    String secret;
	
	@Autowired
	Environment env;
	
	@Autowired
	AuthAPI authAPI; 
	
	@Bean
	public AuthAPI authAPI() {
		return new AuthAPI(issuer, audience, secret);
	}
	
	@Bean
	public ManagementAPI managementAPI() throws Auth0Exception {
		String token = "";
		
		AuthRequest authRequest = authAPI.requestToken("https://" + issuer +"/api/v2/");
		
		TokenHolder holder = authRequest.execute();
	    token = holder.getAccessToken();
	    return new ManagementAPI(issuer, token);
		
	}
	
}
