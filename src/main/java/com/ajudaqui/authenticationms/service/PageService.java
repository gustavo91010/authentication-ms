package com.ajudaqui.authenticationms.service;

import org.springframework.stereotype.Service;

import org.springframework.ui.Model;

@Service
public class PageService {

  public String showRegisterForm(String application, String urlLogin, String urlRegister, String email, String name,
      Model model) {
    model.addAttribute("email", email);
    model.addAttribute("name", name);
    model.addAttribute("urlRegister", urlRegister);
    model.addAttribute("urlLogin", urlLogin);
    model.addAttribute("appName", application);
    return "register";
  }
}
