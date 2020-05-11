package com.cg.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.models.AuthenticationRequest;
import com.cg.models.AuthenticationResponse;
import com.cg.models.Emp;
import com.cg.models.Login;
import com.cg.repository.LoginRepository;
import com.cg.service.JwtUtil;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {

	@Autowired
	LoginRepository repos;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtil jwtTokenUtil;

//	@Autowired
//	Login login;

	@GetMapping(path = "/getall")
	public List<Login> getAll() {
		System.out.println("Inside getAll() of LoginController");
		return repos.findAll();
	}

	@PostMapping(path = "/add")
	public String empCred(@RequestBody Emp emp) {
		Login login = new Login();

		System.out.println(emp.getEmpId());

		login.setAdminId("" + emp.getEmpId() + "");

		login.setAdminPass(emp.getPassword());

		login.setRole("emp");

		repos.save(login);

		return "{\r\n" + "		\"msg\":\"Success\"\r\n" + "	}";
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {

		try {
			// below is an example of manually triggering authentication with credentials
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		// if authentication succeeds, then for that username extract details
		// Modified to generate token on the basis of UserModel rather than UserDetails
		final Login user = repos.findByAdminId(authenticationRequest.getUsername());

		// using the jwt utils library to generate jwt
		final String jwt = jwtTokenUtil.generateToken(user);
		// Modified to send in user rather than userDetails

		// returning jwt as json
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

}
