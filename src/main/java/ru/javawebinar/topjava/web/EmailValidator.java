package ru.javawebinar.topjava.web;


import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasIdAndEmail;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;

@Component
public class EmailValidator implements Validator {

    public static final String EXCEPTION_DUPLICATE_EMAIL = "exception.user.duplicateEmail";

    private final UserRepository repository;

    private final HttpServletRequest request;

    public EmailValidator(UserRepository repository, HttpServletRequest request) {
        this.repository = repository;
        this.request = request;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        if (StringUtils.hasText(user.getEmail())) {
            User dbUser = repository.getByEmail(user.getEmail());
            if (dbUser != null) {
                if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {  // UPDATE
                    int dbId = dbUser.id();

                    // it is ok, if update ourself
                    if (user.getId() != null && dbId == user.id()) return;

                    // Workaround for update with user.id=null in request body
                    // ValidationUtil.assureIdConsistent called after this validation
                    if (SecurityUtil.safeGet() != null) {
                        String requestURI = request.getRequestURI();
                        if (requestURI.endsWith("/" + dbId) || (dbId == SecurityUtil.authUserId() && requestURI.contains("/profile")))
                            return;
                    }
                }
                errors.rejectValue("email", EXCEPTION_DUPLICATE_EMAIL, "");
            }
        }
    }
}
