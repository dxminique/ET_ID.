package com.example.ticket.infrastructure.adapter.out.persistence;



import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTicketRepository extends JpaRepository<TicketEntity, Long> {
}