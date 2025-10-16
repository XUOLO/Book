package com.example.BookMangement.Service.ServiceImpls;

import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Repository.BookRepository;
import com.example.BookMangement.Service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * BookServiceImpl
 *
 * @author xuanl
 * @version 01-00
 * @since 5/14/2024
 */
@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;


    @Override
    public Book getBookById(Long id) {
        try {
            Optional<Book> book = bookRepository.findById(id);
            return book.orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        } catch (Exception ex) {
            log.error("BookServiceImpl_getBookById_Error :", ex);
            return null;
        }
    }

    @Override
    public List<Book> searchBook(String keyword) {
        if(keyword!=null){
            return bookRepository.findAll(keyword);
        }
        return bookRepository.findAll();
    }

    @Override
    public long getTotalBooks() {
        return bookRepository.count();
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }


    @Override
    public Page<Book> getBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAll(pageable);
    }

}
