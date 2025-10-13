package com.example.BookMangement.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ContactController
 *
 * @author loctx
 * @version 01-00
 * @since 10/13/2025
 */
@Controller
@RequestMapping("/contact")
public class ContactController {

    @GetMapping()
    public String showListEmployee(Model model, HttpSession session) {

        return "contact";
    }
}
