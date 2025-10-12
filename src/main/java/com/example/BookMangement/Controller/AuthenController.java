package com.example.BookMangement.Controller;

import com.example.BookMangement.Repository.RoleRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.BookService;
import com.example.BookMangement.Service.MemberService;
import com.example.BookMangement.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * @author xuanl
 * @version 01-00
 * @since 5/08/2024
 */
@Controller
public class AuthenController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MemberService memberService;
     @Autowired
    private BookService bookService;

    // Index page
    @GetMapping("/")
    public String indexPage(HttpSession session, Model model) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("totalMember",memberService.countMember());
        model.addAttribute("totalBooks",bookService.getTotalBooks());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "index";
    }

    // Not have permission page
    @GetMapping("/403")
    public String Page403(HttpSession session, Model model) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        return "Error/403";
    }

    // Login page
    @GetMapping("/login")
    public String loginPage(Principal principal) {
        boolean isAuthenticated = principal != null;
        if (isAuthenticated) {
            return "redirect:/";
        }
        return "Authen/login";
    }



}
