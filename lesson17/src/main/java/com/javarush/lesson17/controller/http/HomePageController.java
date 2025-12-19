package com.javarush.lesson17.controller.http;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class HomePageController {

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String getHomePage(Principal principal) {
        return "redirect:/users";
    }
}
