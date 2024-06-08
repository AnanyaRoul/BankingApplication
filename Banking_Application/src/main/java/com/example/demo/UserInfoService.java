package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
    private PasswordEncoder encoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserInfo user = userRepo.findByName(username).orElseThrow();
		UserDetails userDetails = User.withUsername(user.getName())
                .password(user.getPassword()) 
                .roles(user.getRole()) 
                .build(); 
		
		return userDetails;
	}
	
	 public String addUser(UserInfo userInfo) {
		 
	        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
	        userInfo.setRole("USER");
	        userRepo.save(userInfo);
	        
	        return "User Added Successfully";
	    }

}
