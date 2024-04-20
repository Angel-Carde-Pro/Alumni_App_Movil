package com.gamesmindt.myapplication.Model;

public class EmpresaDTO {
    private Integer id;
    private String empresario;
    private Ciudad ciudad;
    private SectorEmpresarial sectorEmpresarial;
    private String ruc;
    private String nombre;
    private String tipoEmpresa;
    private String razonSocial;
    private String area;
    private String sitioWeb;
    private String estado;

    private String ubicacion;

    public EmpresaDTO(Integer id, String empresario, Ciudad ciudad, SectorEmpresarial sectorEmpresarial, String ruc, String nombre, String tipoEmpresa, String razonSocial, String area, String sitioWeb, String estado, String ubicacion) {
        this.id = id;
        this.empresario = empresario;
        this.ciudad = ciudad;
        this.sectorEmpresarial = sectorEmpresarial;
        this.ruc = ruc;
        this.nombre = nombre;
        this.tipoEmpresa = tipoEmpresa;
        this.razonSocial = razonSocial;
        this.area = area;
        this.sitioWeb = sitioWeb;
        this.estado = estado;
        this.ubicacion = ubicacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmpresario() {
        return empresario;
    }

    public void setEmpresario(String empresario) {
        this.empresario = empresario;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public SectorEmpresarial getSectorEmpresarial() {
        return sectorEmpresarial;
    }

    public void setSectorEmpresarial(SectorEmpresarial sectorEmpresarial) {
        this.sectorEmpresarial = sectorEmpresarial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(String tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}