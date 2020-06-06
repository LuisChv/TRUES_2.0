package sv.ues.fia.eisi.trues.db.entity;

public class RequisitoTramite {
    private int idRequisitoTramite;
    private int idTramite;
    private int idRequisito;

    public RequisitoTramite(int idRequisitoTramite, int idTramite, int idRequisito) {
        this.idRequisitoTramite = idRequisitoTramite;
        this.idTramite = idTramite;
        this.idRequisito = idRequisito;
    }

    public RequisitoTramite() {
    }

    public int getIdRequisitoTramite() {
        return idRequisitoTramite;
    }

    public void setIdRequisitoTramite(int idRequisitoTramite) {
        this.idRequisitoTramite = idRequisitoTramite;
    }

    public int getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(int idTramite) {
        this.idTramite = idTramite;
    }

    public int getIdRequisito() {
        return idRequisito;
    }

    public void setIdRequisito(int idRequisito) {
        this.idRequisito = idRequisito;
    }
}
