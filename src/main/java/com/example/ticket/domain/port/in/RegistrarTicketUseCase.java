package com.example.ticket.domain.port.in;
import com.example.ticket.domain.model.Ticket;

public interface RegistrarTicketUseCase {
     Ticket registrar(String titulo, String descripcion);
}
