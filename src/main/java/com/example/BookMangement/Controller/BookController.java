package com.example.BookMangement.Controller;

import com.cloudinary.utils.ObjectUtils;
import com.example.BookMangement.Entity.Author;
import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Entity.BookCategory;
import com.example.BookMangement.Entity.BookImage;
import com.example.BookMangement.Repository.AuthorRepository;
import com.example.BookMangement.Repository.BookCategoryRepository;
import com.example.BookMangement.Repository.BookImageRepository;
import com.example.BookMangement.Repository.BookRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.BookService;
import com.example.BookMangement.Service.ServiceImpls.CloudinaryService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * BookController
 *
 * @author xuanl
 * @version 01-00
 * @since 5/10/2024
 */
@Controller
@RequestMapping("/book")
public class BookController {

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
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private BookImageRepository bookImageRepository;

    @GetMapping("/new-book")
    public String showNewEmployeeForm(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("book", new Book());
        model.addAttribute("listBookCategories", bookCategoryRepository.findAll());
        model.addAttribute("listAuthors", authorRepository.findAll());

        return "Book/new-book";
    }

    @GetMapping("/")
    public String showListBook(Model model, HttpSession session) {
        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        model.addAttribute("listBook", bookRepository.findByIsDeleteFalse());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Book/list-book";
    }



    @GetMapping("/edit-book/{id}")
    public String showUpdateBookForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        Book book = bookService.getBookById(id);
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        List<Author> authorList = authorRepository.findAll();

        model.addAttribute("book", book);
        model.addAttribute("updateDate", book.getUpdateDate());
        model.addAttribute("updateBy", book.getUpdateBy());
        model.addAttribute("authorList", authorList);
        model.addAttribute("bookCategoryList", bookCategoryList);
        String nameLogin = (String) session.getAttribute("name");
        model.addAttribute("name", nameLogin);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "Book/edit-book";
    }

    @GetMapping("/delete-book/{id}")
    public String deleteEmployee(@PathVariable(value = "id") long id) {
        Book book = bookService.getBookById(id);
        // book.setIsDelete(true);
        bookRepository.delete(book);
        return "redirect:/book/";
    }


    @PostMapping("/save-book")
    public String saveBook(HttpSession session, @RequestParam("image") MultipartFile imageFile,@RequestParam("images") List<MultipartFile> imageFiles, @ModelAttribute("book") Book book, BindingResult bindingResult, RedirectAttributes redirectAttributes,  Model model) throws IOException {


        String name = (String) session.getAttribute("name");
        model.addAttribute("name", name);
        book.setCreateDate(LocalDate.now());
        book.setCreateBy(name);
        book.setUpdateDate(LocalDate.now());
        book.setUpdateBy(name);
        book.setIsDelete(false);
        String imageUrl = cloudinaryService.uploadFile(imageFile);
        book.setImage(imageUrl); // Lưu URL vào DB

        List<BookImage> images = new ArrayList<>();
        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                String imagesUrl = cloudinaryService.uploadFile(file);
                BookImage pi = new BookImage();
                pi.setImage(imagesUrl);
                pi.setBook(book);
                images.add(pi);
            }
        }
        book.setImages(images);
        bookRepository.save(book);

        redirectAttributes.addFlashAttribute("newBookSuccess", "Success add new book !");
        return "redirect:/book/new-book";
    }

    @PostMapping("/edit-book/edit")
    public String editBook(HttpSession session, @ModelAttribute("book") Book book, BindingResult bindingResult, @RequestParam("bookCategories") List<Long> bookCategoryIds, RedirectAttributes redirectAttributes)  {
        String nameLogin = (String) session.getAttribute("name");
        book.setUpdateBy(nameLogin);
        book.setUpdateDate(LocalDate.now());
        book.setIsDelete(book.getIsDelete());
        book.clearBookCategories();
//        book.clearAuthors();

        List<BookCategory> bookCategories = bookCategoryRepository.findAllById(bookCategoryIds);
        book.setBookCategories(new HashSet<>(bookCategories));
        bookRepository.save(book);
        redirectAttributes.addFlashAttribute("editBookSuccess", "Success edit book !");
        return "redirect:/book/edit-book/" + book.getId();
    }


}
