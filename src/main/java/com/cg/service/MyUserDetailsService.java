package com.cg.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cg.models.Login;
import com.cg.repository.LoginRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	// Using the UserRepo JPA repository along with UserModel to extract user
	// credentials from the mysql db
	@Autowired
	LoginRepository repo;

	Login user = new Login();

	// this function is called by the Authentication Manager using this service.
	// The code inside should extract username, password, and authorities,
	// create a UserDetails object from it using User() constructor,
	// this UserDetails object is used by Authentication manager to validate
	// credentials passed to the Authentication Manager
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		user = repo.findByAdminId(userName);

		// Just copy paste the next two lines
		List<GrantedAuthority> roleList = new ArrayList<GrantedAuthority>();
		roleList.add(new SimpleGrantedAuthority(user.getRole()));

		return new User(user.getAdminId(), user.getAdminPass(), roleList);
	}

}
