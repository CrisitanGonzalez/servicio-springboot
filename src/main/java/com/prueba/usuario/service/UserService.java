package com.prueba.usuario.service;

import com.prueba.usuario.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<Object> createUser(User user);
}
