package com.gamesmindt.myapplication.Model;

public class UsuarioDTO {
    private Integer id;
    private String nombreUsuario;
    private String clave;
    private String cedula;
    private String rol;
    private boolean estado;
    private String ruta_imagen;
    private String url_imagen;

    public UsuarioDTO(Integer id, String nombreUsuario, String clave, String cedula, String rol, boolean estado, String ruta_imagen, String url_imagen) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
        this.cedula = cedula;
        this.rol = rol;
        this.estado = estado;
        this.ruta_imagen = ruta_imagen;
        this.url_imagen = url_imagen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getRuta_imagen() {
        return ruta_imagen;
    }

    public void setRuta_imagen(String ruta_imagen) {
        this.ruta_imagen = ruta_imagen;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }
}
