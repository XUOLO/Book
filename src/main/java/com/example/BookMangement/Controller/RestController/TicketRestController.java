package com.example.BookMangement.Controller.RestController;

import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Entity.TicketItem;
import com.example.BookMangement.Repository.BookRepository;
import com.example.BookMangement.Repository.UserRepository;
import com.example.BookMangement.Service.BookService;
import com.example.BookMangement.Service.ListTicketService;
import com.example.BookMangement.Service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TicketRestController
 *
 * @author xuanl
 * @version 01-00
 * @since 5/17/2024
 */
@RestController
@RequestMapping("/api/v1/list-ticket")
public class TicketRestController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private ListTicketService listTicketService;

    @GetMapping("/all-book")
    public List<Book> listAllBook(){
        return bookRepository.findByIsDeleteFalse();
    }


    @GetMapping("/itemCount")
    public int getCartItemCount() {
        return listTicketService.getCount();
    }
    @GetMapping("/remove/{id}")
    public void removeItem(@PathVariable("id") int id){
         listTicketService.remove(id);
    }




    @GetMapping("/update")
    public void updateItem(@RequestParam("bookId") Integer bookId, @RequestParam("quantity") Integer quantity, HttpServletRequest request) {
        listTicketService.update(bookId, quantity);
    }
    @GetMapping("/listChosen")
    public List<Book> listChosenItem() {
        List<Book> listChosen = new ArrayList<>();
        Collection<TicketItem> ticketItems = listTicketService.getAllListTicketItem();
        for (TicketItem ticketItem : ticketItems) {
            if (ticketItem.getId() != null) {
                Book book = bookRepository.findById(ticketItem.getId()).orElse(null);
                if (book != null && ticketItem.getQuantity() > 0) {
                    listChosen.add(book);
                }
            }
        }
        return listChosen;

    }


    @GetMapping("/search")
    public List<Book>  searchItem(@RequestParam("keyword") String keyword) {
    return bookService.searchBook(keyword);
    }


}
