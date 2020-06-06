package sv.ues.fia.eisi.trues.db.entity;

public class ActividadTramite {
    private int idActividadTramite;
    private int idActividad;
    private int idTramite;

    public ActividadTramite() {
    }

    public ActividadTramite(int idActividadTramite, int idActividad, int idTramite) {
        this.idActividadTramite = idActividadTramite;
        this.idActividad = idActividad;
        this.idTramite = idTramite;
    }

    public int getIdActividadTramite() {
        return idActividadTramite;
    }

    public void setIdActividadTramite(int idActividadTramite) {
        this.idActividadTramite = idActividadTramite;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public int getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(int idTramite) {
        this.idTramite = idTramite;
    }
}
