package com.example.ticket.infrastructure.adapter.out.persistence;

import com.example.ticket.domain.model.Ticket;
import com.example.ticket.domain.port.out.TicketRepositoryPort;

import java.io.FileWriter;
import java.util.Optional;

public class FileTicketRepositoryAdapter implements TicketRepositoryPort {

    @Override
    public Ticket guardar(Ticket ticket) {
        try {
            FileWriter writer = new FileWriter("/app/tickets.txt", true);

            writer.write(
                    String.valueOf(ticket.getId()) + "," +
                            ticket.getTitulo() + "," +
                            ticket.getDescripcion() + "," +
                            ticket.getEstado() + "\n"
            );

            writer.close();

            return ticket;

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar ticket en TXT", e);
        }
    }

    @Override
    public Optional<Ticket> buscarPorId(Long id) {
        return Optional.empty();
    }
}