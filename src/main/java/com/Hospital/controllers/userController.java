package com.Hospital.controllers;

import com.Hospital.models.userModel;
import com.Hospital.services.userService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class userController {

    private final userService userService;

    public userController(userService userService) {
        this.userService = userService;
    }

    // ================= LOGIN =================

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String name,
                        @RequestParam String password,
                        HttpSession session,
                        Model model,
                        RedirectAttributes redirectAttributes) {

        userModel user = userService.login(name, password);

        if (user == null) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getName());
        session.setAttribute("role", user.getRole());

        // SweetAlert success flag
        redirectAttributes.addFlashAttribute("loginSuccess", true);

        return "redirect:/dashboard";
    }

    // ================= REGISTER =================

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String password,
                           @RequestParam String role,
                           Model model) {

        if (userService.existsByName(name)) {
            model.addAttribute("error", "User already exists");
            return "register";
        }

        userModel user = new userModel();
        user.setName(name);
        user.setPassword(password);
        user.setRole(role);

        userService.register(user);

        model.addAttribute("success",
                "Account created successfully. Please login.");

        return "register";
    }

    // ================= DASHBOARD =================

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        if (session.getAttribute("role") == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("role", session.getAttribute("role"));

        return "dashboard";
    }

    // ================= HOME =================

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ================= LOGOUT =================

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }
}
