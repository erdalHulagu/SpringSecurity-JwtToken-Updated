package com.erdal.security.jwt;

import java.io.IOException;import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.erdal.security.service.UserDetailServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


// Burada elimizde bulunan tokeni extract edecegiz yani AuthenticationMAnagerin tanimasini saglayacagiz
@Component
public class JwtAuthFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader=request.getHeader("Authorization");
		
		String token= null;
		String userName= null;
		
		if (authHeader!=null && authHeader.startsWith("Bearer ")) {
			 token=authHeader.substring(7);
			userName=jwtService.extractUsername(token);
			
		}
		if (userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
		UserDetails userDetails=	userDetailServiceImpl.loadUserByUsername(userName);
            if (jwtService.validateToken(token, userDetails)) {
			UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
			
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			
		}
            
		}
		
		filterChain.doFilter(request, response);
		
	}

}
