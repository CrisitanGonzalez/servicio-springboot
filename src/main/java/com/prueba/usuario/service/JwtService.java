package com.prueba.usuario.service;

import com.prueba.usuario.Helper.RestBadRequestBuilder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
@Log4j2
public class JwtService {

    static final long ONE_MINUTE_IN_MILLIS = 60000;
    static final long MINUTES = 60;

    private final JwtBuilder jwtBuilder;
    private final JwtParser jwtParser;

    public JwtService(JwtBuilder jwtBuilder, JwtParser jwtParser) {
        this.jwtBuilder = jwtBuilder;
        this.jwtParser = jwtParser;
    }


    public String emitToken(Map<String, Object> claims) {
        Calendar date = Calendar.getInstance();
        Date createAt = new Date(date.getTimeInMillis());
        Date expiration = new Date(date.getTimeInMillis() + (ONE_MINUTE_IN_MILLIS * MINUTES));
        return jwtBuilder
                .setIssuedAt(createAt)
                .setExpiration(expiration)
                .addClaims(claims)
                .compact();
    }

    public boolean isSignedJwt(String jwt) {
        try {
            jwtParser.parse(jwt);
            return true;
        } catch (JwtException ex) {
            log.warn("is not possible validate jwt", ex);
            return false;
        }
    }

    public Claims getJwtClaims(String jwt) {

        Object body = jwtParser.parse(jwt.substring(7)).getBody();

        return (Claims) body;
    }

    public ResponseEntity<?> validate(String authorization) {
        try {
            if (authorization.length() < 8 || !authorization.startsWith("Bearer ")) {
                return new RestBadRequestBuilder()
                        .addError("authorization", "Is necessary Authorization Bearer.")
                        .buildBadRequest();
            } else {

                if (!isSignedJwt(authorization.substring(7))) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
            return null;
        } catch (Exception ex) {
            log.error("unauthorized", ex);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}

