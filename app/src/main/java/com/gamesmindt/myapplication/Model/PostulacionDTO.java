package com.gamesmindt.myapplication.Model;

import com.gamesmindt.myapplication.activities.OfertasLaborales;

public class PostulacionDTO {
    private Integer id;
    private Integer graduado;
    private Integer ofertaLaboral;
    private String estado;

    public PostulacionDTO() {
    }

    public PostulacionDTO(Integer id, Integer graduado, Integer ofertaLaboral, String estado) {
        this.id = id;
        this.graduado = graduado;
        this.ofertaLaboral = ofertaLaboral;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGraduado() {
        return graduado;
    }

    public void setGraduado(Integer graduado) {
        this.graduado = graduado;
    }

    public Integer getOfertaLaboral() {
        return ofertaLaboral;
    }

    public void setOfertaLaboral(Integer ofertaLaboral) {
        this.ofertaLaboral = ofertaLaboral;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
