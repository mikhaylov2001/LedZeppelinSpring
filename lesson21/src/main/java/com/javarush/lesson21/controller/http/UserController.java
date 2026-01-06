package com.javarush.lesson21.controller.http;

import com.javarush.lesson21.entity.Role;
import com.javarush.lesson21.entity.User;
import com.javarush.lesson21.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@SessionAttributes({"currentUser"})
public class UserController {

    private final UserService userService;


    @GetMapping("/th")
    public String th(){
        throw new RuntimeException("ho-ho!!!");
    }

    @GetMapping()
    public ModelAndView showAllUsers(ModelAndView view, Principal principal) {
        view.addObject("users", userService.findAll());
        view.setViewName("userpage");
        view.addObject("roles", Role.values());
        view.addObject("principalName", principal.getName());
        return view;
    }


    @GetMapping("/{id}")
    public ModelAndView showOneUserAndUsers(ModelAndView view, @PathVariable("id") Long id) {
        Optional<User> optionalUser = userService.get(id);
        if (optionalUser.isPresent()) {
            view.addObject("user", optionalUser.get());
            view.addObject("users", userService.findAll());
        }
        view.addObject("roles", Role.values());
        view.setViewName("userpage");
        return view;
    }

    @PostMapping()
    public String processNewUserOrLogin(User user,
                                        HttpSession session,
                                        @RequestParam(required = false, name = "createUser") String createUser) {
        if (Objects.nonNull(createUser)) {
            user = userService.save(user);
            return "redirect:/users/%d".formatted(user.getId());
        } else {
            log.info(" user {} login ", user);
            session.setAttribute("currentUser", user);
            return "redirect:/users";
        }
    }


    @PostMapping("/{id}")
    public String updateOrDeleteUser(User user,
                                     @RequestParam(required = false, name = "deleteUser") String deleteUser) {
        if (Objects.nonNull(deleteUser)) {
            userService.delete(user);
            return "redirect:/users";
        } else {
            userService.save(user);
            return "redirect:/users/%d".formatted(user.getId());
        }
    }
}
