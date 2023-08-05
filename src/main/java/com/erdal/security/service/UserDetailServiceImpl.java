package com.erdal.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.erdal.domin.User;
import com.erdal.repository.UserRepository;

// UserDetailsService classina user repositoriden findby name iile kendi userimizi yani kim login olacaksa onu getirdik 
@Component
public class UserDetailServiceImpl implements UserDetailsService{
	
	@Autowired
   private	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	Optional<User>	user=userRepository.findByName(username);
	
	
		return  user.map(UserDetailImpl::new).orElseThrow(()->new UsernameNotFoundException("User not found" + username));
//				
		
		       // user.map(t->new UserDetailImpl(t)).orElseThrow(()->new UsernameNotFoundException("User not found" + username));  solede kullanabiliriz
		
		
	}


}
