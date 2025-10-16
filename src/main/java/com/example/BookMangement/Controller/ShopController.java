package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Repository.BookCategoryRepository;
import com.example.BookMangement.Service.BookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/products")
    public String showListEmployee(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   Model model,
                                   HttpSession session) {

        model.addAttribute("listBookCategories", bookCategoryRepository.findAll());

        // Lấy danh sách phân trang
        Page<Book> bookPage = bookService.getBooks(page, size);
        List<Book> filteredBooks = bookService.getAll();
        List<Book> listProduct = bookPage.getContent();

        // Thêm ID danh mục
        for (Book p : filteredBooks) {
            String ids = p.getBookCategories().stream()
                    .map(b -> "cat" + b.getId())
                    .collect(Collectors.joining(" "));
            p.setCategoryIds(ids);
        }

        // Lọc các loại sách
        List<Book> bookNew = filteredBooks.stream()
                .filter(b -> b.getType() != null && b.getType().contains("0"))
                .toList();

        List<Book> bookDiscount = filteredBooks.stream()
                .filter(b -> b.getType() != null && b.getType().contains("3"))
                .toList();

        // Gán dữ liệu cho view
        model.addAttribute("listProduct", listProduct);
        model.addAttribute("bookNew", bookNew);
        model.addAttribute("bookDiscount", bookDiscount);
        model.addAttribute("totalProduct", bookPage.getTotalElements());
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "shop-grid";
    }




}
