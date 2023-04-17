package ru.javawebinar.topjava.web.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.EmailValidator;
import ru.javawebinar.topjava.util.UsersUtil;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestController extends AbstractUserController {

    private final EmailValidator emailValidator;

    static final String REST_URL = "/rest/admin/users";

    public AdminRestController(EmailValidator emailValidator) {
        this.emailValidator = emailValidator;
    }

    @Override
    @GetMapping
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return super.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody User user, BindingResult result) {
        emailValidator.validate(UsersUtil.asTo(user), result);
        if (result.hasErrors()) {
            throw new DataIntegrityViolationException("User with this email already exists");
        }
        User created = super.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user, @PathVariable int id, BindingResult result) {
        emailValidator.validate(UsersUtil.asTo(user), result);
        if (result.hasErrors()) {
            throw new DataIntegrityViolationException("User with this email already exists");
        }
        super.update(user, id);
    }

    @Override
    @GetMapping("/by-email")
    public User getByMail(@RequestParam String email) {
        return super.getByMail(email);
    }

    @GetMapping("/{id}/with-meals")
    public User getWithMeals(@PathVariable int id) {
        return super.getWithMeals(id);
    }

    @Override
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        super.enable(id, enabled);
    }
}