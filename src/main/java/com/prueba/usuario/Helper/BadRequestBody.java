package com.prueba.usuario.Helper;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class BadRequestBody {
    private HashMap<String, List<String>> errors;
}

