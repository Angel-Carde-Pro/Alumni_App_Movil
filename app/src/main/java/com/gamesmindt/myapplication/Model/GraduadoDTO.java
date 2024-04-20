package com.gamesmindt.myapplication.Model;
import java.util.Date;

public class GraduadoDTO {
    private Integer id;
    private String usuario;
    private String ciudad;
    private Date anioGraduacion;
    private String emailPersonal;
    private String estadoCivil;
    private String urlPdf;

    public GraduadoDTO() {

    }
    public GraduadoDTO(Integer id, String usuario, String ciudad, Date anioGraduacion, String emailPersonal, String estadoCivil, String rutaPdf, String urlPdf, Integer[] idOferta) {
        this.id = id;
        this.usuario = usuario;
        this.ciudad = ciudad;
        this.anioGraduacion = anioGraduacion;
        this.emailPersonal = emailPersonal;
        this.estadoCivil = estadoCivil;
        this.urlPdf = urlPdf;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
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

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }

    public Date getAnioGraduacion() {
        return anioGraduacion;
    }

    public void setAnioGraduacion(Date anioGraduacion) {
        this.anioGraduacion = anioGraduacion;
    }
}
