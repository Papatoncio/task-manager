package com.papatoncio.taskapi.security;

import com.papatoncio.taskapi.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(User user) {
        List<String> roles = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())).map(GrantedAuthority::getAuthority)
                .toList();
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationTime);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId().toString())
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return parseAll(token).getBody().getSubject();
    }

    public List<String> extractRoles(String token) {
        var roles = (List<?>) parseAll(token).getBody().get("roles");
        return roles.stream().map(Object::toString).toList();
    }

    public boolean isValid(String token, UserDetails user) {
        return user.getUsername().equals(extractUsername(token)) && !isExpired(token);
    }

    private boolean isExpired(String token) {
        return parseAll(token).getBody().getExpiration().before(new Date());
    }

    private Jws<Claims> parseAll(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
    }
}
