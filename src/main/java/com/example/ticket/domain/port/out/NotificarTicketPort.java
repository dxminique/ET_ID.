package com.example.ticket.domain.port.out;
import com.example.ticket.domain.model.Ticket;

public interface NotificarTicketPort {
    void notificarNuevoTicket(Ticket ticket);

}
