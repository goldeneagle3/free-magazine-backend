package com.serbest.magazine.backend.security.jwt;

import com.serbest.magazine.backend.entity.Author;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${magazine.app.accessTokenSecret}")
    private String accessTokenSecret;

    @Value("${magazine.app.accessTokenExpirationMs}")
    private int accessTokenExpirationMs;

    @Value("${magazine.app.jwtRefreshCookieName}")
    private String jwtRefreshCookie;

    // Access Token
    public String generateAccessToken(UserDetailsImpl userDetails) {
        return generateAccessTokenFromJWT(userDetails.getUsername());
    }

    public String generateAccessToken(Author author) {
        return generateAccessTokenFromJWT(author.getUsername());
    }

    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return generateCookie(jwtRefreshCookie, refreshToken, "/api/auth/refreshToken");
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtRefreshCookie);
    }

    public ResponseCookie getCleanJwtRefreshCookie() {
        ResponseCookie cookie = ResponseCookie
                .from(jwtRefreshCookie, null)
                .build();
        return cookie;
    }


    private Key generateAccessTokenKey(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(accessTokenSecret)
        );
    }

    public String getUserNameFromJwtToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(generateAccessTokenKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        return username;
    }
    public boolean validateAccessToken(String accessToken) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(generateAccessTokenKey())
                    .build()
                    .parse(accessToken);
            return true;
        } catch (MalformedJwtException ex) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
    }

    public String generateAccessTokenFromJWT(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + accessTokenExpirationMs))
                .signWith(generateAccessTokenKey())
                .compact();
    }

    private ResponseCookie generateCookie(String name, String value, String path) {
        ResponseCookie cookie = ResponseCookie.from(name, value).path(path).maxAge(24 * 60 * 60).httpOnly(false).secure(true).build();
        return cookie;
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

}
