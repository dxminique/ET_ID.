package com.example.ticket.infrastructure.adapter.out.notification;

import com.example.ticket.domain.model.Ticket;
import com.example.ticket.domain.port.out.NotificarTicketPort;

public class TicketNotificationAdapter implements NotificarTicketPort {

    @Override
    public void notificarNuevoTicket(Ticket ticket) {
        System.out.println("Simulando envío de mail / mensaje / whatsapp");
        System.out.println("Ticket registrado con ID: " + ticket.getId());
    }
}