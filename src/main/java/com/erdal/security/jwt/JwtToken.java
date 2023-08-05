package com.erdal.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erdal.request.AuthRequest;


@RestController
@RequestMapping("/jwts")
public class JwtToken {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/authenticate")
	public String authenticateToken(@RequestBody AuthRequest authRequest) {
		
	Authentication authentication=	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
		
		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getUserName());
		}else {
			throw new UsernameNotFoundException("invalid user request !");
		}
			
		
	}

}
