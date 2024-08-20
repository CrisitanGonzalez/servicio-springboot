package com.prueba.usuario.validators;

import com.prueba.usuario.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroValidatorHelper implements ConstraintValidator<RegistroValidator, User> {
    @Autowired
    private Environment env;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public boolean isValid(User user, ConstraintValidatorContext ctx)
    {
        Matcher matcher = EMAIL_PATTERN.matcher(user.getEmail());
        if(!matcher.matches()){
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate("El correo no cumple con el formato correcto")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }
        Pattern passwordPattern =  Pattern.compile(env.getProperty("cl.prueba.expresionpassword"));
        Matcher matcher2 = passwordPattern.matcher(user.getPassword());
        if(!matcher2.matches()){
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate("La password no cumple con el formato requerido")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}

