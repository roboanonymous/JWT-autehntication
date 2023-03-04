package com.smart.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smart.helper.jwtUTIL;
import com.smart.model.JwtRequest;
import com.smart.model.JwtResponse;
import com.smart.service.CustomUserDetailsService;

@Controller
public class JWTcontroller {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService customeUserDetailsService;
	
	@Autowired
	private jwtUTIL jwUTIL;
	
	@RequestMapping(value = "/token", method = RequestMethod.POST)
	public ResponseEntity<?> generateToken(@RequestBody JwtRequest JwtRequest) throws Exception
	{
		System.out.println(JwtRequest);
		try {
			
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(JwtRequest.getUsername(), JwtRequest.getPassword()));
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Bad Credential");
		}
		
		// fine area
		UserDetails userDetails = this.customeUserDetailsService.loadUserByUsername(JwtRequest.getUsername());
		
		String token = this.jwUTIL.generateToken(userDetails);
		
		System.out.println("JWT  :  " + token);
		
		// {token :: value}
		
		return ResponseEntity.ok(new JwtResponse(token));
		
	}
	
	

}
