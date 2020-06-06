package sv.ues.fia.eisi.trues.db.entity;

public class Ubicacion {
    private int idUbicacion;
    private float longitud;
    private float latidud;
    private float altitud;
    private String componenteTematica;

    public Ubicacion(int idUbicacion, int longitud, int latidud, int altitud, String componenteTematica) {
        this.idUbicacion = idUbicacion;
        this.longitud = longitud;
        this.latidud = latidud;
        this.altitud = altitud;
        this.componenteTematica = componenteTematica;
    }

    public Ubicacion() {
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public float getLatidud() {
        return latidud;
    }

    public void setLatidud(float latidud) {
        this.latidud = latidud;
    }

    public float getAltitud() {
        return altitud;
    }

    public void setAltitud(float altitud) {
        this.altitud = altitud;
    }

    public String  getComponenteTematica() {
        return componenteTematica;
    }

    public void setComponenteTematica(String componenteTematica) {
        this.componenteTematica = componenteTematica;
    }

}
