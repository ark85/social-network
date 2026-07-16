package io.github.ark85.study.socialnetwork.service;

import io.github.ark85.study.socialnetwork.configuration.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    private final UserService userService;

    private final SecretKey secretKey;
    private final Duration accessTokenTtl;

    public JwtService(UserService userService, JwtProperties jwtProperties) {
        this.userService = userService;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
        this.accessTokenTtl = jwtProperties.getAccessTokenTtl();
    }

    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(Date.from(Instant.now().plus(accessTokenTtl)))
                .signWith(secretKey)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }

    public boolean isJwtValid(Claims claims) {
        userService.loadUserByUsername(claims.getSubject());
        return claims.getExpiration().after(Date.from(Instant.now()));
    }
}
