package com.prueba.usuario.controller;

import com.prueba.usuario.model.Message;
import com.prueba.usuario.model.User;
import com.prueba.usuario.service.JwtService;
import com.prueba.usuario.service.UserService;
import com.prueba.usuario.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController()
@Api(tags = "Prueba", value="Prueba")
@CrossOrigin(origins ="*", allowedHeaders = "*")
@Log4j2
public class MainController {

    private final UserService userService;
    private final JwtService jwtService;

    public MainController(UserServiceImpl userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }


    @ApiOperation(value = "health-check", notes = "")
    @ApiResponses({@ApiResponse(code = 201, message = "correct", response = HttpStatus.class)})
    @GetMapping(path = "/api/health-check")
    public ResponseEntity<?> gtHealthCheck(){
        return ResponseEntity.ok("{\"status\":\"ok\"}");
    }

    @ApiOperation(value="Add User", notes="")
    @ApiResponses({@ApiResponse(code = 200, message = "correct", response = User.class),
            @ApiResponse(code = 409, message = "Error", response = Message.class)
    })
    @PostMapping(path = "/v1/create")
    public ResponseEntity<?> createUser(@RequestBody @Validated User user){
        return userService.createUser(user);
    }
}


