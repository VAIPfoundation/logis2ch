package com.sdc2ch.token.jwt.domain;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StopWatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.token.exceptions.InvalidJwtToken;
import com.sdc2ch.token.exceptions.JwtExpiredTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor(staticName="of")
public class RawAccessJwtToken implements JwtToken {
    private static Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);

    private final static String tokenIssuer = "http:

    private final String token;

    
    public Jws<Claims> parseClaims(String signingKey) {
    	StopWatch stopWatch = new StopWatch("honeymon");
    	stopWatch.start("Long type");


        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException | ArrayIndexOutOfBoundsException ex) {
            logger.error("Invalid JWT Token", ex);
            throw new InvalidJwtToken("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {


            HashMap<String, Object> tokenBodyMap = new HashMap<String, Object>();
        	String[] split_string = token.split("\\.");

        	String base64EncodedBody = split_string[1];


        	Base64 base64Url = new Base64(true);
        	String body = new String(base64Url.decode(base64EncodedBody));

        	try {
        		ObjectMapper mapper = new ObjectMapper();
        		tokenBodyMap = mapper.readValue(body, HashMap.class);

    			
                String username = (String)tokenBodyMap.get("sub");
                ArrayList<String> scopes = (ArrayList<String>)tokenBodyMap.get("scopes"); 
            	Claims claims = Jwts.claims().setSubject(username);
            	claims.put("scopes", scopes);

            	LocalDateTime currentTime = LocalDateTime.now();

            	
            	String newToken = Jwts.builder()
            			.setClaims(claims)
            			.setIssuer(tokenIssuer)
            			.setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
            			.setExpiration(Date.from(currentTime
            					.plusMinutes(tokenExpireMin)
            					.atZone(ZoneId.systemDefault()).toInstant()))

            			.signWith(SignatureAlgorithm.HS512, signingKey)
            			.compact();

                Jws<Claims> jwsClaims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(newToken);
                logger.info("JWT Token replace. username={}", username);
                return jwsClaims;
    		} catch (IOException e) {
    			throw new JwtExpiredTokenException(this, "JWT Token expired", expiredEx);
    		}

        }
    }

    @SuppressWarnings("unchecked")
	public static void main(String[] args) {

        System.out.println("token: "+token);

		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
			System.out.println(claims.getBody().getSubject());
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException | ArrayIndexOutOfBoundsException ex) {
            logger.error("Invalid JWT Token", ex);
            throw new InvalidJwtToken("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
        	logger.info("JWT Token is expired", expiredEx);
        	logger.info("-----JWT Token recreate-----");

            HashMap<String, Object> tokenBodyMap = new HashMap<String, Object>();
        	String[] split_string = token.split("\\.");

        	String base64EncodedBody = split_string[1];


        	Base64 base64Url = new Base64(true);
        	String body = new String(base64Url.decode(base64EncodedBody));

        	try {
        		ObjectMapper mapper = new ObjectMapper();
        		tokenBodyMap = mapper.readValue(body, HashMap.class);
    			System.out.println("bodyMap.toString()" + tokenBodyMap.toString());
    			System.out.println(tokenBodyMap.get("sub"));
    			System.out.println(tokenBodyMap.get("scopes"));


    			
                String username = (String)tokenBodyMap.get("sub");
                ArrayList<String> scopes = (ArrayList<String>)tokenBodyMap.get("scopes"); 
            	Claims claims = Jwts.claims().setSubject(username);
            	claims.put("scopes", scopes);

            	LocalDateTime currentTime = LocalDateTime.now();

            	
            	String newToken = Jwts.builder()
            			.setClaims(claims)
            			.setIssuer(tokenIssuer)
            			.setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
            			.setExpiration(Date.from(currentTime
            					.plusMinutes(tokenExpireMin)
            					.atZone(ZoneId.systemDefault()).toInstant()))
            			.signWith(SignatureAlgorithm.HS512, tokenSigningKey)
            			.compact();

                Jws<Claims> jwsClaims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(newToken);

                logger.info("JWT Token recreate. username={}, token={}", username, newToken);
                System.out.println("newToken: " + newToken);

    		} catch (IOException e) {
    			e.printStackTrace();
    		}

        }
	}
}
