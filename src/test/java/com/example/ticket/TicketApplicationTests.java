package com.example.ticket;

import com.example.ticket.domain.model.EstadoTicket;
import com.example.ticket.domain.model.Ticket;
import com.example.ticket.domain.port.out.NotificarTicketPort;
import com.example.ticket.domain.port.out.TicketRepositoryPort;
import com.example.ticket.application.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketApplicationTests {

    @Mock
    private TicketRepositoryPort ticketRepositoryPort;

    @Mock
    private NotificarTicketPort notificarTicketPort;

    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketService(ticketRepositoryPort, notificarTicketPort);
    }

    @Test
    void registrarTicket_debeRetornarTicketConEstadoAbierto() {
        Ticket ticketGuardado = new Ticket(1L, "Error en login", "El usuario no puede iniciar sesión", EstadoTicket.ABIERTO);
        when(ticketRepositoryPort.guardar(any(Ticket.class))).thenReturn(ticketGuardado);

        Ticket resultado = ticketService.registrar("Error en login", "El usuario no puede iniciar sesión");

        assertNotNull(resultado);
        assertEquals(EstadoTicket.ABIERTO, resultado.getEstado());
        assertEquals("Error en login", resultado.getTitulo());
        verify(notificarTicketPort, times(1)).notificarNuevoTicket(any(Ticket.class));
    }

    @Test
    void consultarEstado_debeRetornarEstadoCorrecto() {
        Ticket ticket = new Ticket(1L, "Bug crítico", "Falla en producción", EstadoTicket.ABIERTO);
        when(ticketRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(ticket));

        EstadoTicket estado = ticketService.consultarEstado(1L);

        assertEquals(EstadoTicket.ABIERTO, estado);
    }

    @Test
    void consultarEstado_ticketInexistente_debeLanzarExcepcion() {
        when(ticketRepositoryPort.buscarPorId(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> ticketService.consultarEstado(99L));
        assertEquals("Ticket no encontrado", ex.getMessage());
    }

    @Test
    void registrarTicket_debeGuardarEnRepositorio() {
        Ticket ticketGuardado = new Ticket(2L, "Lentitud", "Sistema lento", EstadoTicket.ABIERTO);
        when(ticketRepositoryPort.guardar(any(Ticket.class))).thenReturn(ticketGuardado);

        ticketService.registrar("Lentitud", "Sistema lento");

        verify(ticketRepositoryPort, times(1)).guardar(any(Ticket.class));
    }
}
