package com.cg.service;
//copy for authorization

import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cg.models.Login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

	// This is one of the biggest security issues that has even brought down big
	// corporations
	// But still YouTubers ask you to do it this way
	// Obviously with a disclaimer that you shouldn't do this
	// But still do not show you the proper way :((
//		private String SECRET_KEY = "secret";

	@Value("{jwt.key}")
	private String SECRET_KEY;

	private Claims claims;

	public String generateToken(Login user) {
		// I have modified userDetails to user

		return Jwts.builder() // creates an empty jwt
				.claim("usr", user.getAdminId()) // adds key value pair to the json payload. This specific entries
													// mentions the user
													// which can be used by other service to know that this is a user
													// that has been
													// authenticated
				// you can add other claims that you want to be persistent through microservice
				// to know about authorization
				.claim("rls", user.getRole()) // this will add authority value to the request
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) /// used to track expiration
																							/// date
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY) // explicitly mentioning algorithm, and generating the
																// hash
				.compact(); // removing blank spaces, blank lines, etc.etc.,
	}

	// Unfortunately JWT is stateless
	// So I cannot log things out
	// That's why we need expiration date to automatically invalidate a JWT

	private Claims extractAllClaims(String token) {
		this.claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		return this.claims;
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String extractUsername(String token) {
		extractAllClaims(token);
		return this.claims.get("usr").toString();
	}

	public Date extractExpiration(String token) {

		return extractClaim(token, Claims::getExpiration);
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

	}

}
