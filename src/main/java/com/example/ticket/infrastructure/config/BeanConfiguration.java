package com.example.ticket.infrastructure.config;

import com.example.ticket.application.TicketService;
import com.example.ticket.domain.port.in.ConsultarEstadoTicketUseCase;
import com.example.ticket.domain.port.in.RegistrarTicketUseCase;
import com.example.ticket.domain.port.out.NotificarTicketPort;
import com.example.ticket.domain.port.out.TicketRepositoryPort;
import com.example.ticket.infrastructure.adapter.out.notification.TicketNotificationAdapter;
import com.example.ticket.infrastructure.adapter.out.persistence.FileTicketRepositoryAdapter;
import com.example.ticket.infrastructure.adapter.out.persistence.SpringDataTicketRepository;
import com.example.ticket.infrastructure.adapter.out.persistence.TicketPersistenceAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean("dbTicketRepository")
    public TicketRepositoryPort dbTicketRepository(SpringDataTicketRepository repository) {
        return new TicketPersistenceAdapter(repository);
    }

    @Bean("txtTicketRepository")
    public TicketRepositoryPort txtTicketRepository() {
        return new FileTicketRepositoryAdapter();
    }

    @Bean
    public NotificarTicketPort notificarTicketPort() {
        return new TicketNotificationAdapter();
    }

    @Bean("dbTicketService")
    public TicketService dbTicketService(
            @Qualifier("dbTicketRepository") TicketRepositoryPort ticketRepositoryPort,
            NotificarTicketPort notificarTicketPort) {
        return new TicketService(ticketRepositoryPort, notificarTicketPort);
    }

    @Bean("txtTicketService")
    public TicketService txtTicketService(
            @Qualifier("txtTicketRepository") TicketRepositoryPort ticketRepositoryPort,
            NotificarTicketPort notificarTicketPort) {
        return new TicketService(ticketRepositoryPort, notificarTicketPort);
    }

    @Bean("registrarTicketDbUseCase")
    public RegistrarTicketUseCase registrarTicketDbUseCase(
            @Qualifier("dbTicketService") TicketService ticketService) {
        return ticketService;
    }

    @Bean("consultarEstadoTicketDbUseCase")
    public ConsultarEstadoTicketUseCase consultarEstadoTicketDbUseCase(
            @Qualifier("dbTicketService") TicketService ticketService) {
        return ticketService;
    }

    @Bean("registrarTicketTxtUseCase")
    public RegistrarTicketUseCase registrarTicketTxtUseCase(
            @Qualifier("txtTicketService") TicketService ticketService) {
        return ticketService;
    }

    @Bean("consultarEstadoTicketTxtUseCase")
    public ConsultarEstadoTicketUseCase consultarEstadoTicketTxtUseCase(
            @Qualifier("txtTicketService") TicketService ticketService) {
        return ticketService;
    }
}