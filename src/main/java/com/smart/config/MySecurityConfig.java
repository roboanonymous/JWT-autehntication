package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import com.smart.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

	@Autowired
	private CustomUserDetailsService CustomUserDetailsService;

	
	  @Bean 
	  public UserDetailsService UserDetailsService() { 
		  return CustomUserDetailsService; 
		  }
	  
	  @Bean 
	  public BCryptPasswordEncoder passwordEncoder() { 
		  return new BCryptPasswordEncoder();
		  }
	 

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider DaoAuthenticationProvider = new DaoAuthenticationProvider();
		DaoAuthenticationProvider.setUserDetailsService(this.UserDetailsService());
		DaoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

		return DaoAuthenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf()
			.disable()
			.cors()
			.disable()
			.authorizeHttpRequests()
			.requestMatchers("/token").permitAll()
		    .anyRequest().authenticated()
		    .and()
		    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authenticationProvider(authenticationProvider());

		DefaultSecurityFilterChain build = http.build();

		return build;
	}
}
