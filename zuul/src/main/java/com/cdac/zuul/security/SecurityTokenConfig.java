package com.cdac.zuul.security;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cdac.common.security.JwtConfig;




@Configuration
@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtConfig jwtConfig;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable()
		//make sure you use stateless session; session wont be used to store user's state
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		//handle authorized attempts
		.exceptionHandling().authenticationEntryPoint((req,rsp,e)->rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
		//Add a filter to to validate the tokens with every request
		.addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig),UsernamePasswordAuthenticationFilter.class)
		//authorization requests config
		.authorizeRequests()
		//allow who all are accessing 'auth' service
		.antMatchers(HttpMethod.POST,jwtConfig.getUri()).permitAll()
		//must be admin if trying to access admin area (authentication also required here)
		.antMatchers("/gallery"+"/admin/**").hasRole("ADMIN")
		//any other request must be authenticated
		.anyRequest().authenticated();
		
	}
	
	@Bean
	public JwtConfig jwtConfig() {
		return new JwtConfig();
	}

}
