package com.javarush.lesson17.controller.rest;

import com.javarush.lesson17.entity.User;
import com.javarush.lesson17.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.InputMismatchException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/restapi/v1/users")
public class UserRestController {

    private final UserService userService;

    //READ
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        return userService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public User get(@PathVariable Long id) {
        return userService
                .get(id)
                .orElseThrow(this::notFound);
    }

    //WRITING
    //CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        try {
            return userService.save(user);
        } catch (Exception e) {
            log.error("create user={}", user, e);
            throw badRequest(e);
        }
    }

    //UPDATE
    @PutMapping("/{id}") //or @PatchMapping (if only part data update)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User update(@PathVariable Long id, @RequestBody User user) {
        if (id.equals(user.getId()))
            return userService.save(user);
        else {
            log.error("update with incorrect id={}", id);
            throw badRequest(new InputMismatchException("incorrect id"));
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        try {
            userService.deleteById(id);
        } catch (Exception e) {
            log.error("delete id={}", id, e);
            throw badRequest(e);
        }
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    private ResponseStatusException badRequest(Exception e) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
}
