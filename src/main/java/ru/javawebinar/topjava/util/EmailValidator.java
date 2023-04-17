package ru.javawebinar.topjava.util;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Optional;

@Component
public class EmailValidator implements Validator {

    private final UserService service;

    public EmailValidator(UserService service) {
        this.service = service;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserTo user = (UserTo) target;
        Optional<User> userInDb = service.findByEmail(user.getEmail().toLowerCase());
        if (userInDb.isPresent() && !userInDb.get().getEmail().equals(SecurityUtil.safeGet().getUserTo().getEmail())) {
            errors.rejectValue("email", "", "User with this email already exists");
        }
    }
}
