package sv.ues.fia.eisi.trues.db.entity;

public class Paso {
    private Integer idPaso;
    private Integer idPersonal;
    private Integer idTramite;
    private String descripcion;
    private float porcentaje;

    public Paso() {
    }

    public Paso(Integer idPaso, Integer idPersonal, Integer idTramite, String descripcion, float porcentaje) {
        this.idPaso = idPaso;
        this.idPersonal = idPersonal;
        this.idTramite = idTramite;
        this.descripcion = descripcion;
        this.porcentaje = porcentaje;
    }

    public Integer getIdPaso() {
        return idPaso;
    }

    public void setIdPaso(Integer idPaso) {
        this.idPaso = idPaso;
    }

    public Integer getIdPersonal() {
        return idPersonal;
    }

    public void setIdPersonal(Integer idPersonal) {
        this.idPersonal = idPersonal;
    }

    public Integer getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(Integer idTramite) {
        this.idTramite = idTramite;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(float porcentaje) {
        this.porcentaje = porcentaje;
    }
}
