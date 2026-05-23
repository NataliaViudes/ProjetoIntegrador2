package pi2.example.back_end.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JWTUtil {
    private static final String SECRET = "SISTEMA_PI2_CHAVE_SUPER_SECRETA_2025_123456";
    private static final long EXPIRATION = 1000 * 60 * 60 * 24;

    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static String gerarToken(String login,String cargo,Integer nivel) {
        return Jwts.builder()
                .setSubject(login)
                .claim("cargo", cargo)
                .claim("nivel", nivel)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims validarToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getLogin(String token) {
        return validarToken(token).getSubject();
    }

    public static String getCargo(String token) {
        return validarToken(token).get("cargo", String.class);
    }
}