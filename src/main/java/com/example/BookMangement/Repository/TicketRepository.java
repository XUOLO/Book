package com.example.BookMangement.Repository;


import com.example.BookMangement.Entity.Ticket;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * TicketRepository
 *
 * @author xuanl
 * @version 01-00
 * @since 5/20/2024
 */

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByMemberId(Long memberId);
    @Query("SELECT SUM(t.total) FROM Ticket t WHERE t.member.id = ?1")
    int getTotalByMemberId(Long memberId);
    @Query("SELECT SUM(t.total) FROM Ticket t WHERE t.member.id = ?1 AND t.status = '3'")
    Optional<Integer> getTotalBookByMemberId(Long memberId);
    List<Ticket> findByIsDeleteFalse();

    @Override
    long count();

}
