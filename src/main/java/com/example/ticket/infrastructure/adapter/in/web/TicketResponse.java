package com.example.ticket.infrastructure.adapter.in.web;

import com.example.ticket.domain.model.EstadoTicket;

public class TicketResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private EstadoTicket estado;

    public TicketResponse(Long id, String titulo, String descripcion, EstadoTicket estado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public EstadoTicket getEstado() {
        return estado;
    }
}