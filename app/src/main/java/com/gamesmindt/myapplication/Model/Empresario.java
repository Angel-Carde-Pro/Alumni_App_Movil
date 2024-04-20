package com.gamesmindt.myapplication.Model;

public class Empresario {
    private Integer id;
    private boolean estado;
    private String puesto;
    private int anios;
    private Usuario usuario;
    private String email;
    private String descripcion;

    public Empresario(Integer id, boolean estado, String puesto, int anios, Usuario usuario, String email, String descripcion) {
        this.id = id;
        this.estado = estado;
        this.puesto = puesto;
        this.anios = anios;
        this.usuario = usuario;
        this.email = email;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public int getAnios() {
        return anios;
    }

    public void setAnios(int anios) {
        this.anios = anios;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
