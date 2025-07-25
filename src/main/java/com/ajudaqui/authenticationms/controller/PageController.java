// package com.ajudaqui.authenticationms.controller;

// import com.ajudaqui.authenticationms.service.PageService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// @Controller
// @RequestMapping("/login")
// public class PageController {
//   @Autowired
//   private PageService pageService;

//   @GetMapping("/register/auth2")
//   public String showRegisterForm(@RequestParam String email, @RequestParam String name, Model model) {
//     return pageService.showRegisterForm(email, name, model);
//   }
// }
