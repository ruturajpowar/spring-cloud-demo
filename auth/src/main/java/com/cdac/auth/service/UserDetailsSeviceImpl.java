package com.cdac.auth.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsSeviceImpl implements UserDetailsService {
	
	@Autowired
	BCryptPasswordEncoder encoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//hard coding the users.All passwords must be encoded
		final List<AppUser> users=Arrays.asList(
				new AppUser(1,"user",encoder.encode("user"),"USER"),
				new AppUser(2,"admin",encoder.encode("admin"),"ADMIN")
				);
		
		for(AppUser appUser : users) {
			if(appUser.getUsername().equals(username)) {
				//Remember that spring needs role to be in this format : "ROLE_"+userRole (i.e. "ROLE_ADMIN")
				//so,we need to set it to that format,so we can verify and compare roles (i.e. hasRole("ADMIN"))
				List<GrantedAuthority> grantedAuthorities=AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_"+appUser.getRole());
				
				//User class is provided by spring and represents the model class for user to be returned by UserDetailsService
				//And used by auth manager to verify and check user authentication.
				
				return new User(appUser.getUsername(),appUser.getPassword(),grantedAuthorities);
				
			}
		}
		
		return null;
	}
	
	

	//A temporary class represent the user saved in database

	 class AppUser{
		
		private Integer id;
		
		private String username,password;
		
		private String role;

		public AppUser(Integer id, String username, String password, String role) {
			super();
			this.id = id;
			this.username = username;
			this.password = password;
			this.role = role;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

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

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}
		
		
		
	}

}

