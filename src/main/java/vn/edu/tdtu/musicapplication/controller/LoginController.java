package vn.edu.tdtu.musicapplication.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String loginPage(HttpServletRequest request, Model model, Principal principal) {
        if (principal == null) {
            String message = null;
            HttpSession session = request.getSession(false);
            if (session != null) {
                AuthenticationException ex = (AuthenticationException) session.getAttribute(
                        WebAttributes.AUTHENTICATION_EXCEPTION
                );
                if (ex != null) {
                    message = ex.getMessage();
                }
            }
            model.addAttribute("errorMessage", message);
            return "login_register";
        }
        System.out.println(principal.getName());
        return "redirect:/home";
    }
}