package com.gamesmindt.myapplication.Model;
import java.time.LocalDate;

public class Graduado {
    private Integer id;
    private Usuario usuario;
    private Ciudad ciudad;
    private String anioGraduacion;
    private String emailPersonal;
    private String estadoCivil;
    private String rutaPdf;
    private String urlPdf;

    public Graduado(Integer id) {
        this.id = id;
    }

    public Graduado(Integer id, Usuario usuario, Ciudad ciudad, String anioGraduacion, String emailPersonal, String estadoCivil, String rutaPdf, String urlPdf) {
        this.id = id;
        this.usuario = usuario;
        this.ciudad = ciudad;
        this.anioGraduacion = "asd";
        this.emailPersonal = emailPersonal;
        this.estadoCivil = estadoCivil;
        this.rutaPdf = rutaPdf;
        this.urlPdf = urlPdf;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public String getanioGraduacion() {
        return anioGraduacion;
    }

    public void setanioGraduacion(String anioGraduacion) {
        this.anioGraduacion = anioGraduacion;
    }

    public String getEmailPersonal() {
        return emailPersonal;
    }

    public void setEmailPersonal(String emailPersonal) {
        this.emailPersonal = emailPersonal;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getRutaPdf() {
        return rutaPdf;
    }

    public void setRutaPdf(String rutaPdf) {
        this.rutaPdf = rutaPdf;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }
}
