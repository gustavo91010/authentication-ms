package com.ajudaqui.authenticationms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class PageController {

    @GetMapping("/register/auth2")
  public String showRegisterForm(@RequestParam String email, @RequestParam String name, Model model) {
    model.addAttribute("email", email);
    model.addAttribute("name", name);
    return "register"; // nome do template Thymeleaf sem extensão
  }
}
