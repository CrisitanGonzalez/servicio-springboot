package com.prueba.usuario.service;

import com.prueba.usuario.model.Message;
import com.prueba.usuario.model.User;
import com.prueba.usuario.repository.UserRepository;
import com.prueba.usuario.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Environment environment;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("ValidPassword123");
        user.setName("Test User");


        when(environment.getProperty("cl.prueba.expresionpassword")).thenReturn("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$");


        when(jwtService.emitToken(any())).thenReturn("fake-jwt-token");
    }

    @Test
    public void testUserInitialization() {
        setUp();
        assertNotNull(user, "User should be initialized in setUp()");
        user.setEmail("another@example.com"); // Esto no debería lanzar NullPointerException
        assertEquals("another@example.com", user.getEmail());
    }

    @Test
    public void createUser_EmailFormatoIncorrecto() {
        setUp();
        user.setEmail("invalid-email");

        ResponseEntity<Object> response = userService.createUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("formato email incorrecto", ((Message) response.getBody()).getText());
    }

    @Test
    public void createUser_PasswordFormatoIncorrecto() {
        setUp();
        user.setPassword("12345");

        ResponseEntity<Object> response = userService.createUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("formato password incorrecto", ((Message) response.getBody()).getText());
    }

    @Test
    public void createUser_EmailYaExiste() {
        setUp();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        ResponseEntity<Object> response = userService.createUser(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("el correo ya existe en la base de datos", ((Message) response.getBody()).getText());
    }

    @Test
    public void createUser_Exito() {
        setUp();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        ResponseEntity<Object> response = userService.createUser(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
}
