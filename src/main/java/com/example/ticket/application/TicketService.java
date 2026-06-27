package com.example.ticket.application;

import com.example.ticket.domain.model.Ticket;
import com.example.ticket.domain.model.EstadoTicket;
import com.example.ticket.domain.port.in.RegistrarTicketUseCase;
import com.example.ticket.domain.port.in.ConsultarEstadoTicketUseCase;
import com.example.ticket.domain.port.out.TicketRepositoryPort;
import com.example.ticket.domain.port.out.NotificarTicketPort;


public class TicketService implements RegistrarTicketUseCase, ConsultarEstadoTicketUseCase{

    private final TicketRepositoryPort ticketRepositoryPort;
    private final NotificarTicketPort notificarTicketPort;

    public TicketService(TicketRepositoryPort ticketRepositoryPort,
                         NotificarTicketPort notificarTicketPort) {
        this.ticketRepositoryPort = ticketRepositoryPort;
        this.notificarTicketPort = notificarTicketPort;
    }
    @Override
    public Ticket registrar(String titulo, String descripcion) {
        Ticket ticket = new Ticket(null, titulo, descripcion, EstadoTicket.ABIERTO);
        Ticket ticketGuardado = ticketRepositoryPort.guardar(ticket);
        notificarTicketPort.notificarNuevoTicket(ticketGuardado);
        return ticketGuardado;
    }

    @Override
    public EstadoTicket consultarEstado(Long ticketId) {
        Ticket ticket = ticketRepositoryPort.buscarPorId(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        return ticket.getEstado();
    }
}
