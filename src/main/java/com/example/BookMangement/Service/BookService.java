package com.example.BookMangement.Service;

import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Entity.BookCategory;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * BookService
 *
 * @author xuanl
 * @version 01-00
 * @since 5/10/2024
 */

public interface BookService {


    public Book getBookById(Long id) ;

    public List<Book> searchBook(String keyword);
    public long getTotalBooks();

    List<Book> getAll();
}
