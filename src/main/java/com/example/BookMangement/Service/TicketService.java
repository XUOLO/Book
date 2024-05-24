package com.example.BookMangement.Service;

import com.example.BookMangement.Entity.Ticket;

import java.util.List;
import java.util.Optional;

/**
 * TicketService
 *
 * @author xuanl
 * @version 01-00
 * @since 5/21/2024
 */
public interface TicketService {
    public List<Ticket> getAllTicketsByMemberId(long memberId);

    public int getTotalByMemberId(Long memberId);

    public Optional<Integer> getTotalBookOverdueByMemberId(Long memberId);
    public Ticket getTicketById(Long id) ;
    public long getTotalTickets();




}
