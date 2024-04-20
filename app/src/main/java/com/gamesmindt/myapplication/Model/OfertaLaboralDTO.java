package com.gamesmindt.myapplication.Model;

import java.io.Serializable;

public class OfertaLaboralDTO implements Serializable {
    private Integer id;
    private double salario;
    private String fechaCierre;
    private String fechaPublicacion;
    private String cargo;
    private String tiempo;
    private String experiencia;
    private String fechaApertura;
    private String areaConocimiento;
    private EstadoOferta estado;
    private String nombreEmpresa;
    private String tipo;
    private String fotoPortada;

    public OfertaLaboralDTO(Integer id, double salario, String fechaCierre, String fechaPublicacion, String cargo, String tiempo, String experiencia, String fechaApertura, String areaConocimiento, EstadoOferta estado, String nombreEmpresa, String tipo, String fotoPortada) {
        this.id = id;
        this.salario = salario;
        this.fechaCierre = fechaCierre;
        this.fechaPublicacion = fechaPublicacion;
        this.cargo = cargo;
        this.tiempo = tiempo;
        this.experiencia = experiencia;
        this.fechaApertura = fechaApertura;
        this.areaConocimiento = areaConocimiento;
        this.estado = estado;
        this.nombreEmpresa = nombreEmpresa;
        this.tipo = tipo;
        this.fotoPortada = fotoPortada;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(String fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(String fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public String getAreaConocimiento() {
        return areaConocimiento;
    }

    public void setAreaConocimiento(String areaConocimiento) {
        this.areaConocimiento = areaConocimiento;
    }

    public EstadoOferta isEstado() {
        return estado;
    }

    public void setEstado(EstadoOferta estado) {
        this.estado = estado;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFotoPortada() {
        return fotoPortada;
    }

    public void setFotoPortada(String fotoPortada) {
        this.fotoPortada = fotoPortada;
    }
}
