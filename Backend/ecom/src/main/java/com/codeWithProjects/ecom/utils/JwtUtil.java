package com.codeWithProjects.ecom.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A7134743777217A25432A462D4A614E635266556A586E3272357538782F413F4428472B4B";

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)                                                    // Establece los claims personalizados
                .setSubject(userName)                                                 // Establece el subject (usuario)
                .setIssuedAt(new Date(System.currentTimeMillis()))                   // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Expira en 30 minutos
                .signWith(getSignKey(), SignatureAlgorithm.HS256)                    // Firma con algoritmo HS256
                .compact();                                                          // Genera el token final
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())    // Establece la clave para verificar la firma
                .build()                        // Construye el parser
                .parseClaimsJws(token)         // Parsea y verifica el token
                .getBody();                    // Obtiene los claims
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}