package com.prueba.usuario.service.impl;

import com.prueba.usuario.model.Message;

import com.prueba.usuario.model.User;
import com.prueba.usuario.repository.UserRepository;
import com.prueba.usuario.service.JwtService;
import com.prueba.usuario.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final Environment environment;
    private final JwtService jwtService;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(Environment environment, JwtService jwtService) {
        this.environment = environment;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public ResponseEntity<Object>  createUser(User user) {
        try {
            if(!isValidEmail(user)){
                return new ResponseEntity<>( Message.builder().text("formato email incorrecto").build(), HttpStatus.OK);
            }
            if(!isValidPassword(user)) {
                return new ResponseEntity<>( Message.builder().text("formato password incorrecto").build(), HttpStatus.OK);
            }
            if(!userRepository.findByEmail(user.getEmail()).isEmpty()){
                return new ResponseEntity<>( Message.builder().text("el correo ya existe en la base de datos").build(), HttpStatus.OK);
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("usuario", user.getName());
            user.setToken(jwtService.emitToken(claims));
            user.setCreated(Date.from(Instant.now()));
            user.setModified(Date.from(Instant.now()));
            user.setLast_login(Date.from(Instant.now()));
            user.setIsactive(true);
            user = userRepository.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>( Message.builder().text(ex.getMessage()).build(), HttpStatus.CONFLICT);
        }
    }

    public boolean isValidEmail(User user)
    {
        Matcher matcher = EMAIL_PATTERN.matcher(user.getEmail());
        return matcher.matches();
    }
    public boolean isValidPassword(User user){
        Pattern passwordPattern =  Pattern.compile(environment.getProperty("cl.prueba.expresionpassword"));
        Matcher matcher = passwordPattern.matcher(user.getPassword());
        return matcher.matches();
    }
}
