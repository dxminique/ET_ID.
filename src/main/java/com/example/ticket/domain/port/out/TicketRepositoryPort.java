package com.example.ticket.domain.port.out;

import com.example.ticket.domain.model.Ticket;
import java.util.Optional;

public interface TicketRepositoryPort {
    Ticket guardar(Ticket ticket);
    Optional<Ticket> buscarPorId(Long id);
}
