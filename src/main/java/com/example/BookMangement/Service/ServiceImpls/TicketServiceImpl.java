package com.example.BookMangement.Service.ServiceImpls;

import com.example.BookMangement.Entity.Ticket;
import com.example.BookMangement.Repository.TicketRepository;
import com.example.BookMangement.Service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * TicketServiceImpl
 *
 * @author xuanl
 * @version 01-00
 * @since 5/21/2024
 */
@Service
@Slf4j
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Override
    public List<Ticket> getAllTicketsByMemberId(long memberId) {
        return ticketRepository.findByMemberId(memberId);
    }

    @Override
    public int getTotalByMemberId(Long memberId) {
        return ticketRepository.getTotalByMemberId(memberId);
    }

    @Override
    public Optional<Integer> getTotalBookOverdueByMemberId(Long memberId) {
        return ticketRepository.getTotalBookByMemberId(memberId);
    }

    @Override
    public Ticket getTicketById(Long id) {

        try {
            Optional<Ticket> ticket = ticketRepository.findById(id);
            return ticket.orElseThrow(() -> new RuntimeException("Book category not found with id: " + id));
        } catch (Exception ex) {
            log.error("TicketServiceImpl_getTicketById_Error :", ex);
            return null;
        }
    }

    @Override
    public long getTotalTickets() {
        return ticketRepository.count();
    }


}
