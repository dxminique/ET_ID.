package com.example.ticket.domain.model;
import lombok.Getter;
import lombok.Setter;

public class Ticket {
    private Long id;
    private String titulo;
    private String descripcion;
    private EstadoTicket estado;

    public Ticket(Long id, String titulo, String descripcion,EstadoTicket estado )
    {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public long getId(){
        return id;
    }

    public String getTitulo(){
    return titulo;
    }

    public String getDescripcion(){
        return descripcion;

    }

    public EstadoTicket getEstado(){
        return estado;
    }

    public void setId(long id) {
        this.id = id;
    }
}

