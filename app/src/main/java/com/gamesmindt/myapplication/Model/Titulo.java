package com.gamesmindt.myapplication.Model;

import java.util.Date;

public class Titulo {
    public int id;
    public String nombreTitulo;
    public String tipo;
    public String numRegistro;
    public Date fechaEmision;
    public Date fechaRegistro;
    public String nivel;
    public String institucion;
    public String nombreCarrera;
    public String cedula;

    public Titulo(int id, String nombreTitulo, String tipo, String numRegistro, Date fechaEmision, Date fechaRegistro, String nivel, String institucion, String nombreCarrera, String cedula) {
        this.id = id;
        this.nombreTitulo = nombreTitulo;
        this.tipo = tipo;
        this.numRegistro = numRegistro;
        this.fechaEmision = fechaEmision;
        this.fechaRegistro = fechaRegistro;
        this.nivel = nivel;
        this.institucion = institucion;
        this.nombreCarrera = nombreCarrera;
        this.cedula = cedula;
    }
}
