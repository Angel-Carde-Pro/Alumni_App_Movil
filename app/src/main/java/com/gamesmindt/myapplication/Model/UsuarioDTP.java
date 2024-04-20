package com.gamesmindt.myapplication.Model;

public class UsuarioDTP {
    private Integer id;

    private String clave;
    private String nombreUsuario;
    private boolean estado;
    private Rol rol;

    private String rutaImagen;
    private String urlImagen;
    private PersonaDTO persona;

    public UsuarioDTP(Integer id, String clave, String nombreUsuario, boolean estado, Rol rol, String rutaImagen, String urlImagen, PersonaDTO persona) {
        this.id = id;
        this.clave = clave;
        this.nombreUsuario = nombreUsuario;
        this.estado = estado;
        this.rol = rol;
        this.rutaImagen = rutaImagen;
        this.urlImagen = urlImagen;
        this.persona = persona;
    }

    public UsuarioDTP() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public PersonaDTO getPersona() {
        return persona;
    }

    public void setPersona(PersonaDTO persona) {
        this.persona = persona;
    }
}

