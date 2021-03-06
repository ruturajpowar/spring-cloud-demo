package com.cdac.auth.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.cdac.common.security.JwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	//we use auth manager to validate the user credentials
	private AuthenticationManager authManager;
	
	private final JwtConfig jwtConfig;
	
	
	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
		this.authManager = authManager;
		this.jwtConfig = jwtConfig;
		
		//By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
		//In our case , we use "/auth" . so we need to override defaults
		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
		
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,HttpServletResponse response) {
		
		try {
			//1. Get credentials from request			
			UserCrendentials creds=new ObjectMapper().readValue(request.getInputStream(), UserCrendentials.class);
			
			//2.Create auth object (contains credentials) which will be used by auth manager.
			UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(creds.getUsername(),creds.getPassword(),Collections.emptyList());
			
			//3. Authentication manager authenticate user,and use UserDetailsServiceImpl::loadUserByUsername() method to load the user.
			return authManager.authenticate(authToken);
			
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	//Upon successful authentication,generate token
	//the 'auth' passed to successfulAuthentication() is the current authenticated user.
    @Override
    protected void successfulAuthentication(HttpServletRequest request,HttpServletResponse response,FilterChain chain,Authentication auth) {
    	Long now=System.currentTimeMillis();
    	String token=Jwts.builder().setSubject(auth.getName())
    			//convert to list of Strings.
    			//This is important because it affetcs the way we get them back in the gateway.
    			.claim("authorities", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
    			.setIssuedAt(new Date(now))
    			.setExpiration(new Date(now + jwtConfig.getExpiration() * 1000)) //in milisecond
    			.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
    			.compact();
    	
    	//Add token to header
    	response.addHeader(jwtConfig.getHeader(), token);
    	
    }





	//A temporary class just to represent the user crendentials
	private static class UserCrendentials{
		
		private String username,password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		
		
		
	}

}
