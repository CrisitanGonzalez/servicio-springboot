package com.prueba.usuario.config;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class JwtConfiguration {
    private static final String JWT_ALGORITHM = "cl.prueba.jwt.algorithm";
    private static final String JWT_SECRET = "cl.prueba.jwt.secret";
    private static final String JWT_ISSUER = "cl.prueba.jwt.issuer";
    private static final String EXCEPTION_1 = "=the secrets must be have 32 characters";
    private static final String EXCEPTION_2 = "=not found jwt issue";

    @Bean
    public SignatureAlgorithm jwtAlgorithm(Environment env) {
        String strAlgorithm = env.getProperty(JWT_ALGORITHM, String.class, "NULL");
        return SignatureAlgorithm.forName(strAlgorithm);
    }

    @Bean
    public JwtParser jwtParser(Environment env) throws Exception {
        String jwtSecretString = env.getProperty(JWT_SECRET, String.class, "NULL");
        if (jwtSecretString.length() < 32) {
            throw new Exception(JWT_SECRET + EXCEPTION_1);
        }
        String jwtIssuerString = env.getProperty(JWT_ISSUER, String.class, "");
        if (jwtIssuerString.isEmpty()) {
            throw new Exception(JWT_ISSUER + EXCEPTION_2);
        }
        return Jwts.parser().requireIssuer(jwtIssuerString).setSigningKey(jwtSecretString.getBytes());
    }

    @Bean
    public JwtBuilder jwtBuilder(Environment env) throws Exception {
        String jwtSecretString = env.getProperty(JWT_SECRET, String.class, "NULL");
        if (jwtSecretString.length() < 32) {
            throw new Exception(JWT_SECRET + EXCEPTION_1);
        }
        String jwtIssuerString = env.getProperty(JWT_ISSUER, String.class, "");
        if (jwtIssuerString.isEmpty()) {
            throw new Exception(JWT_ISSUER + EXCEPTION_2);
        }
        return Jwts.builder()
                .setIssuer(jwtIssuerString)
                .signWith(jwtAlgorithm(env), jwtSecretString.getBytes());
    }
}
