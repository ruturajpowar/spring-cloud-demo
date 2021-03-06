package com.cdac.auth.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cdac.common.security.JwtConfig;
import com.cdac.auth.security.JwtUsernameAndPasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtConfig jwtConfig;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		//make sure we use stateless session; session wont be used to store user's state
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		//handle exception
		.exceptionHandling().authenticationEntryPoint((req,rsp,e)->rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
		.and()
		
		//Add a filter to validate user crendentials and add token at response header.
		//whats the authenticationManager() ?
		//An object provided by WebSecurityConfigurerAdapter ,used to authenticate the user passing users credentials
		//The filter needs this auth manager to authenticate the user.
		.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(),jwtConfig))
		.authorizeRequests()
		//allow all post request
		.antMatchers(HttpMethod.POST,jwtConfig.getUri()).permitAll()
		//any other requests must be authenticated
		.anyRequest().authenticated();					
	}
	
	//Spring has UserDetailsService interface ,which can be overridded to provide our custom implementation which is used by auth manager to load user from database(or any other source)
	//In addition, we need to define the password encoder also.So,auth manager can compare and verify passwords.
	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public JwtConfig jwtConfig() {
        	return new JwtConfig();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

}
