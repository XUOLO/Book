package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Repository.BookCategoryRepository;
import com.example.BookMangement.Service.BookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ShopController
 *
 * @author loctx
 * @version 01-00
 * @since 10/13/2025
 */
@Controller
@RequestMapping("/shop-grid")
public class ShopController {
    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    @Autowired
    private BookService bookService;
    @GetMapping()
    public String showListEmployee(Model model, HttpSession session) {
        model.addAttribute("listBookCategories", bookCategoryRepository.findAll());
        // String name = (String) session.getAttribute("name");
        // model.addAttribute("name", name);
        // model.addAttribute("totalMember",memberService.countMember());
        List<Book> listProduct =  bookService.getAll();

        for (Book p : listProduct) {
            String ids = p.getBookCategories().stream()
                    .map(b -> "cat" + b.getId())
                    .collect(Collectors.joining(" "));
            p.setCategoryIds(ids); // ví dụ "cat1 cat2"
        }
        List<Book> bookNew = listProduct.stream()
                .filter(b -> "1".equals(b.getType()))
                .toList();

        List<Book> bookBest = listProduct.stream()
                .filter(b -> "2".equals(b.getType()))
                .toList();

        List<Book> bookLove = listProduct.stream()
                .filter(b -> "3".equals(b.getType()))
                .toList();
        model.addAttribute("listBookCategories", bookCategoryRepository.findAll());

        model.addAttribute("listProduct", listProduct);
        model.addAttribute("bookNew", bookNew);
        model.addAttribute("bookBest", bookBest);
        model.addAttribute("bookLove", bookLove);
        return "shop-grid";
    }
}
