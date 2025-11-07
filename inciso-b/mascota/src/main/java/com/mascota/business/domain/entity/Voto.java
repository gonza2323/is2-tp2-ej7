package com.mascota.business.domain.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
public class Voto {
    
    @Id
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Temporal(TemporalType.TIMESTAMP)
    private Date respuesta;
    @ManyToOne
    private Mascota mascota1;
    @ManyToOne
    private Mascota mascota2;
    private boolean eliminado;

    public Voto() {
    }

    public Voto(String id, Date fecha, Date respuesta, Mascota mascota1, Mascota mascota2) {
        this.id = id;
        this.fecha = fecha;
        this.respuesta = respuesta;
        this.mascota1 = mascota1;
        this.mascota2 = mascota2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Date respuesta) {
        this.respuesta = respuesta;
    }

    public Mascota getMascota1() {
        return mascota1;
    }

    public void setMascota1(Mascota mascota1) {
        this.mascota1 = mascota1;
    }

    public Mascota getMascota2() {
        return mascota2;
    }

    public void setMascota2(Mascota mascota2) {
        this.mascota2 = mascota2;
    }

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}
    
    
    
}

