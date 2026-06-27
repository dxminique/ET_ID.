package com.example.ticket.infrastructure.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;

import com.example.ticket.domain.model.EstadoTicket;
import com.example.ticket.domain.model.Ticket;
import com.example.ticket.domain.port.in.ConsultarEstadoTicketUseCase;
import com.example.ticket.domain.port.in.RegistrarTicketUseCase;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final RegistrarTicketUseCase registrarTicketDbUseCase;
    private final RegistrarTicketUseCase registrarTicketTxtUseCase;
    private final ConsultarEstadoTicketUseCase consultarEstadoTicketDbUseCase;

    public TicketController(
            @Qualifier("registrarTicketDbUseCase") RegistrarTicketUseCase registrarTicketDbUseCase,
            @Qualifier("registrarTicketTxtUseCase") RegistrarTicketUseCase registrarTicketTxtUseCase,
            @Qualifier("consultarEstadoTicketDbUseCase") ConsultarEstadoTicketUseCase consultarEstadoTicketDbUseCase
    ) {
        this.registrarTicketDbUseCase = registrarTicketDbUseCase;
        this.registrarTicketTxtUseCase = registrarTicketTxtUseCase;
        this.consultarEstadoTicketDbUseCase = consultarEstadoTicketDbUseCase;
    }
    // Endpoint para registrar ticket en base de datos
    @PostMapping("/db")
    public ResponseEntity<TicketResponse> registrarDb(@RequestBody CrearTicketRequest request) {

        Ticket ticket = registrarTicketDbUseCase.registrar(
                request.getTitulo(),
                request.getDescripcion()
        );

        TicketResponse response = new TicketResponse(
                ticket.getId(),
                ticket.getTitulo(),
                ticket.getDescripcion(),
                ticket.getEstado()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/txt")
    public ResponseEntity<TicketResponse> registrarTxt(@RequestBody CrearTicketRequest request) {

        Ticket ticket = registrarTicketTxtUseCase.registrar(
                request.getTitulo(),
                request.getDescripcion()
        );

        TicketResponse response = new TicketResponse(
                ticket.getId(),
                ticket.getTitulo(),
                ticket.getDescripcion(),
                ticket.getEstado()
        );

        return ResponseEntity.ok(response);
    }

    // Fix: manejo de ticket inexistente
    @GetMapping("/{id}/estado")
    public ResponseEntity<String> consultarEstado(@PathVariable Long id) {
        EstadoTicket estado = consultarEstadoTicketDbUseCase.consultarEstado(id);
        return ResponseEntity.ok(estado.name());
    }
}
