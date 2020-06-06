package sv.ues.fia.eisi.trues.db.entity;

public class Tramite {
    private int idTramite;
    private int idFacultad;
    private String nombreTramite;

    public Tramite(int idTramite, int idCategoria, int idFacultad, String nombreTramite) {
        this.idTramite = idTramite;
        this.idFacultad = idFacultad;
        this.nombreTramite = nombreTramite;
    }

    public Tramite() {
    }

    public int getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(int idTramite) {
        this.idTramite = idTramite;
    }

    public int getIdFacultad() {
        return idFacultad;
    }

    public void setIdFacultad(int idFacultad) {
        this.idFacultad = idFacultad;
    }

    public String getNombreTramite() {
        return nombreTramite;
    }

    public void setNombreTramite(String nombreTramite) {
        this.nombreTramite = nombreTramite;
    }
}
