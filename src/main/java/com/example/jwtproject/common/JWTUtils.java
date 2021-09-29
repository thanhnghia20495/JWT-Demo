package com.example.jwtproject.common;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.jwtproject.services.UserDetailImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JWTUtils {
	private static final Logger log = LoggerFactory.getLogger(JWTUtils.class);
	
	
	@Value("${bezkoder.app.jwtSecret}")
	private String jwtSecret;

	@Value("${bezkoder.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	public String generateJWTToken(Authentication authentication) {
		UserDetailImpl userDetailImpl = (UserDetailImpl) authentication.getPrincipal();
		Date currentDate = new Date();
		Date expirationDate = new Date(currentDate.getTime() + jwtExpirationMs);

		return Jwts.builder().setSubject(userDetailImpl.getUsername()).setIssuedAt(currentDate)
				.setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public String getUserNameFromJwtToken(String authToken) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody().getSubject();
	}
	
	public Boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			log.error("Invalid Jwt Signature: {}", e.getMessage());
		}catch (MalformedJwtException e) {
			log.error("Invalid Jwt Token: {}", e.getMessage());
		}catch (ExpiredJwtException e) {
			log.error("Jwt Token is expired: {}", e.getMessage());
		}catch (UnsupportedJwtException e) {
			log.error("Jwt Token is unsupported: {}", e.getMessage());
		}catch (IllegalArgumentException e) {
			log.error("Jwt Claim string is empty: {}", e.getMessage());
		}
		return false;
	}
}
