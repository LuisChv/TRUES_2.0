package sv.ues.fia.eisi.trues.db.entity;

public class Requisito {
    private int idRequisito;
    private String descripcion;

    public Requisito(int idRequisito, int idTramite, String descripcion) {
        this.idRequisito = idRequisito;
        this.descripcion = descripcion;
    }

    public Requisito() {
    }

    public int getIdRequisito() {
        return idRequisito;
    }

    public void setIdRequisito(int idRequisito) {
        this.idRequisito = idRequisito;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
