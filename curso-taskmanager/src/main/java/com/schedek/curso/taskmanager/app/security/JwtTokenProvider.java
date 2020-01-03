/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.security;

/**
 *
 * @author Dan Nguyen
 */
import com.schedek.curso.ejb.entities.User;
import io.jsonwebtoken.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class JwtTokenProvider {

    private static final Logger LOGGER = Logger.getLogger(JwtTokenProvider.class.getName());
    private final String jwtBearer = "CursoBearer ";
    private final String jwtSecret = "CursoTaskManagerSecretKey";
    private final Long jwtExpirationInMs = TimeUnit.DAYS.toMillis(10);

    public String generateToken(User user) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    
    public static void main(String[] args) {
        String x= Jwts.builder()
                .setSubject(Long.toString(210))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + TimeUnit.DAYS.toMillis(10)))
                .signWith(SignatureAlgorithm.HS512, "CursoTaskManagerSecretKey")
                .compact();
        System.out.println(x);
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            LOGGER.log(Level.SEVERE, "Invalid JWT signature", ex.getMessage());
        } catch (MalformedJwtException ex) {
            LOGGER.log(Level.SEVERE, "Invalid JWT token", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            LOGGER.log(Level.SEVERE, "Expired JWT token", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            LOGGER.log(Level.SEVERE, "Unsupported JWT token", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.SEVERE, "JWT claims string is empty.", ex.getMessage());
        }
        return false;
    }

    public String filterToken(String authToken) {
        return authToken.startsWith(jwtBearer)
                ? authToken.substring(12, authToken.length())
                : null;
    }
}
