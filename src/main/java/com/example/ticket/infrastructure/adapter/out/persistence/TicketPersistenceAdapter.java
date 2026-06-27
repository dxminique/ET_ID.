package com.example.ticket.infrastructure.adapter.out.persistence;

import com.example.ticket.domain.model.Ticket;
import com.example.ticket.domain.port.out.TicketRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TicketPersistenceAdapter implements TicketRepositoryPort {

    private final SpringDataTicketRepository repository;

    public TicketPersistenceAdapter(SpringDataTicketRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ticket guardar(Ticket ticket) {
        TicketEntity entity = new TicketEntity();
        entity.setTitulo(ticket.getTitulo());
        entity.setDescripcion(ticket.getDescripcion());
        entity.setEstado(ticket.getEstado());

        TicketEntity saved = repository.save(entity);

        return new Ticket(
                saved.getId(),
                saved.getTitulo(),
                saved.getDescripcion(),
                saved.getEstado()
        );
    }

    @Override
    public Optional<Ticket> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entity -> new Ticket(
                        entity.getId(),
                        entity.getTitulo(),
                        entity.getDescripcion(),
                        entity.getEstado()
                ));
    }
}