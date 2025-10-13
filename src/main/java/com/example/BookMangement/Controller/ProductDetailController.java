package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.Author;
import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Entity.BookCategory;
import com.example.BookMangement.Repository.AuthorRepository;
import com.example.BookMangement.Repository.BookCategoryRepository;
import com.example.BookMangement.Repository.BookRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.BookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * ProductDetailController
 *
 * @author loctx
 * @version 01-00
 * @since 10/13/2025
 */
@Controller
@RequestMapping("/product")
public class ProductDetailController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookCategoryRepository bookCategoryRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping("/detail/{id}")
    public String showUpdateBookForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        Book book = bookService.getBookById(id);
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        model.addAttribute("book", book);
        model.addAttribute("bookCategoryList", bookCategoryList);
        return "shop-details";
    }
}
