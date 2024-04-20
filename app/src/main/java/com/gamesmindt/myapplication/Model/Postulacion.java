package com.gamesmindt.myapplication.Model;

public class Postulacion {
    private Integer id;
    private OfertaDetalle ofertaLaboral;
    private String estado;
    private String fechaPostulacion;
    private String fechaActualizacion;
    private String fechaCancelacion;

    public Postulacion(Integer id,OfertaDetalle ofertaLaboral, String estado, String fechaPostulacion, String fechaActualizacion, String fechaCancelacion) {
        this.id = id;
        this.ofertaLaboral = ofertaLaboral;
        this.estado = estado;
        this.fechaPostulacion = fechaPostulacion;
        this.fechaActualizacion = fechaActualizacion;
        this.fechaCancelacion = fechaCancelacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OfertaDetalle getOfertaLaboral() {
        return ofertaLaboral;
    }

    public void setOfertaLaboral(OfertaDetalle ofertaLaboral) {
        this.ofertaLaboral = ofertaLaboral;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaPostulacion() {
        return fechaPostulacion;
    }

    public void setFechaPostulacion(String fechaPostulacion) {
        this.fechaPostulacion = fechaPostulacion;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(String fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }
}
