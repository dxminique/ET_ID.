package com.example.ticket.domain.port.in;
import com.example.ticket.domain.model.EstadoTicket;

public interface ConsultarEstadoTicketUseCase {
    EstadoTicket consultarEstado(Long ticketId);
}
