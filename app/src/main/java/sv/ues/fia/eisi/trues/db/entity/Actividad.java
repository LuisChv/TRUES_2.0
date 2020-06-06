package sv.ues.fia.eisi.trues.db.entity;

import java.util.Date;

public class Actividad {
    private Integer idActividad;
    private Integer idFacultad;
    private String nombreActividad;
    private String inicio;
    private String fin;

    public Actividad(Integer idActividad, Integer idFacultad, String nombreActividad, String inicio, String fin) {
        this.idActividad = idActividad;
        this.idFacultad = idFacultad;
        this.nombreActividad = nombreActividad;
        this.inicio = inicio;
        this.fin = fin;
    }

    public Actividad() {
    }

    public Integer getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(Integer idActividad) {
        this.idActividad = idActividad;
    }

    public Integer getIdFacultad() {
        return idFacultad;
    }

    public void setIdFacultad(Integer idFacultad) {
        this.idFacultad = idFacultad;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }
}
