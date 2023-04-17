package ru.javawebinar.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.EmailValidator;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileUIController extends AbstractUserController {

    private final EmailValidator emailValidator;

    @Autowired
    public ProfileUIController(EmailValidator emailValidator) {
        this.emailValidator = emailValidator;
    }

    @GetMapping
    public String profile() {
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid UserTo userTo, BindingResult result, SessionStatus status) {
        emailValidator.validate(userTo, result);
        if (result.hasErrors()) {
            return "profile";
        } else {
            super.update(userTo, SecurityUtil.authUserId());
            SecurityUtil.get().setTo(userTo);
            status.setComplete();
            return "redirect:/meals";
        }
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model) {
        emailValidator.validate(userTo, result);
        if (result.hasErrors()) {
            model.addAttribute("register", true);
            return "profile";
        } else {
            super.create(userTo);
            status.setComplete();
            return "redirect:/login?message=app.registered&username=" + userTo.getEmail();
        }
    }
}