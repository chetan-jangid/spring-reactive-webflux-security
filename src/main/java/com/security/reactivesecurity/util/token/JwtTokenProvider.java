package com.security.reactivesecurity.util.token;

import com.security.reactivesecurity.exception.JwtTokenException;
import com.security.reactivesecurity.util.DateUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider extends TokenProvider {

    private static final String AUTHORITIES_KEY = "roles";
    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        String secret = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put(AUTHORITIES_KEY, authoritiesToString(authentication.getAuthorities()));
        Date issuedDate = DateUtils.currentUtilDate();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedDate)
                .setExpiration(getExpiryDate(issuedDate))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) throws JwtTokenException {
        Claims claims = tokenToJwsClaims(token).getBody();
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
                claims.get(AUTHORITIES_KEY).toString()
        )
                .stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public void validateToken(String token) throws JwtTokenException {
        Jws<Claims> claims = tokenToJwsClaims(token);
        if (claims.getBody().getExpiration().before(DateUtils.currentUtilDate())) {
            throw new JwtTokenException(JwtTokenException.tokenExpired());
        }
    }

    private Jws<Claims> tokenToJwsClaims(String token) throws JwtTokenException {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtTokenException(JwtTokenException.invalidJwtToken());
        }
    }

    private Date getExpiryDate(Date issuedDate) {
        return DateUtils.extendDateBy(issuedDate, jwtProperties.getValidity());
    }

    private String authoritiesToString(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
    }

}
