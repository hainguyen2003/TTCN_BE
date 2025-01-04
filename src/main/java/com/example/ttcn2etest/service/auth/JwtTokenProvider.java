package com.example.ttcn2etest.service.auth;

import com.example.ttcn2etest.constant.ErrorCodeDefs;
import com.example.ttcn2etest.exception.JwtTokenInvalid;
import com.example.ttcn2etest.model.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "XAUTHOR";
    @Value("${jwt.jwtSecret}")
    private String jwtSecret;
    @Value("${jwt.jwtExpirationMs}")
    private Long jwtExpirationMs;
    @Value("${jwt.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationsMs;

    @PostConstruct
    public void init() {
        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
        log.info("jwt secret: {}", jwtSecret);
    }

    public String generateTokenWithAuthorities(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getUserInfoFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        if (claims.get(AUTHORITIES_KEY) != null && claims.get(AUTHORITIES_KEY) instanceof String authoritiesStr && !Strings.isBlank(authoritiesStr)) {
            Collection authorities = Arrays.stream(authoritiesStr.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
        } else {
            return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", new ArrayList<>());
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        throw new JwtTokenInvalid(ErrorCodeDefs.getErrMsg(ErrorCodeDefs.TOKEN_INVALID));
    }


}
