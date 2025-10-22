package com.example.movie.ticket.booking.controller;

import com.example.movie.ticket.booking.dto.UserDto;
import com.example.movie.ticket.booking.entity.User;
import com.example.movie.ticket.booking.service.UserLogInService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
public class AuthController {

    private UserLogInService userLogInService;

    @GetMapping({"/", "/index"})
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid @ModelAttribute("userDto") UserDto userDto,
                             RedirectAttributes redirectAttrs,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            String roles = "ROLE_USER";
            userLogInService.createUser(userDto ,roles);
            redirectAttrs.addFlashAttribute("registered", true);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        // load users from DB if needed
        model.addAttribute("message", "Welcome to Admin page!");
        return "admin"; // Thymeleaf template: user.html
    }

    @GetMapping("/user")
    public String usersPage(Model model) {
        // load users from DB if needed
        model.addAttribute("message", "Welcome to Users page!");
        return "user"; // Thymeleaf template: user.html
    }

    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String userEmail =userDetails.getUsername();
        UserDto userDto =userLogInService.findUserByEmail(userEmail);
        model.addAttribute("user", userDto);
        model.addAttribute("role", userDetails.getAuthorities().toString());
        return "profile";
    }

}


