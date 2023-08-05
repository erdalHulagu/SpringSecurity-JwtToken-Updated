package com.erdal.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.erdal.security.jwt.JwtAuthFilter;
import com.erdal.security.service.UserDetailServiceImpl;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // bu method ben metod bazli calisacam diyor
public class SecurityConfig {
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	@Bean
	public UserDetailsService userDetailsService() {
		
//		
//		//Spring securitynin kendi methodu olan UserDetails'ten uretilmis  ve yine 
//		//Security nin User bilgisin bagli userName pasword ve role islemleri direkt yapilabiliniyor
//		
//		//burada admin olusturduk
//		UserDetails admin=User.withUsername("Erdal") 
//				                     .password(passwordEncoder.encode("aylaadem233"))// burada altta BCryptPasswordEncoder() tureyen metodun passvord encoderini kullanip kullanmak istedigimiz passwordu verdik
//				                     .roles("ADMIN")
//				                     .build();
//		
//		//burada normal kullanici User olusturduk ismini kendi ayarladigimiz enam rollerine gore verebiliriz bize kalmis yani
//		UserDetails user=User.withUsername("Katie")
//                .password(passwordEncoder.encode("aylaadem233"))
//                .roles("USER")
//                .build();
//		
//		return new InMemoryUserDetailsManager(admin,user);
		
		return new UserDetailServiceImpl();
	}
	



	// bu methoddada authentike etmek istedigimz end pointleri hallediyoruz
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	return	http.csrf().disable()
		            .authorizeHttpRequests()
		            .requestMatchers("/jwt/authenticate/","/users/new")
		            .permitAll()
		            .and()
	                .authorizeHttpRequests().requestMatchers("/users/**")
	                .authenticated().and()
	                .sessionManagement()
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	                .and()
	                .authenticationProvider(authenticationProvider())
	                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
	                .build();
	    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setUserDetailsService(userDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		
		return authenticationConfiguration.getAuthenticationManager();
		
	}

}
