package com.erdal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // bu method ben metod bazli calisacam diyor
public class SecurityConfig {
	
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
		            .requestMatchers("/products/welcome","/users/new")
		            .permitAll()
		            .and()
		            .authorizeHttpRequests()
		            .requestMatchers("/users/**").permitAll()
		            .and().authorizeHttpRequests().requestMatchers("/products/**").authenticated().and()
		            .formLogin().and().build();
		
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

}
