package com.erdal.security.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.erdal.domin.User;


// 
public class UserDetailImpl implements UserDetails{
	

	private String name;
	
	private String password;
	
	private List<GrantedAuthority> grantedAuthorities;  // kullanilacak yani authantice edilecek enum listleri
	
	
	//Burada constructor kullanarak User details icin alan methodlari burda User clasini parametre vererek user detailsle implement ettik
	public UserDetailImpl(User user) {
		 name=user.getName();
	        password=user.getPassword();
	        grantedAuthorities= Arrays.stream(user.getRoles().split(","))// birden cok role oldugu icin bu metodu kullanipop rolleri belirleyecegiz
	                .map(SimpleGrantedAuthority::new)// .map(role->new SimpleGrantedAuthority(role)) map bolumunu boylede kullanabiliriz
	                .collect(Collectors.toList());
		
	}

	 @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return grantedAuthorities;
	}


	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
	
		return true;
	}

}
