package com.prueba.usuario.Helper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class RestBadRequestBuilder {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_VND_SPRING_VALIDATION_JSON = "application/vnd.prueba.spring-validation+json";
    private static final Pattern jacksonPathReferencePattern = Pattern.compile("\\[([^\\]]+)\\]");
    HashMap<String, List<String>> errors;

    public RestBadRequestBuilder() {
        errors = new HashMap<String, List<String>>();
    }


    public RestBadRequestBuilder addError(String field, String msg) {
        if (errors.containsKey(field)) {
            errors.get(field).add(msg);
        } else {
            errors.put(field, Arrays.asList(msg));
        }
        return this;
    }


    public ResponseEntity<BadRequestBody> buildBadRequest() {
        BadRequestBody body = new BadRequestBody();
        body.setErrors(errors);
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE_HEADER, APPLICATION_VND_SPRING_VALIDATION_JSON);
        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }
}
