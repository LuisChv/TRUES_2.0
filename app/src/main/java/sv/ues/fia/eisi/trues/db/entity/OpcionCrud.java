package sv.ues.fia.eisi.trues.db.entity;

public class OpcionCrud {
    private String idOpcion;
    private String descOpcion;

    public OpcionCrud(String idOpcion, String desOpcion) {
        this.idOpcion = idOpcion;
        this.descOpcion = desOpcion;
    }

    public OpcionCrud() {
    }

    public String getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(String idOpcion) {
        this.idOpcion = idOpcion;
    }

    public String getDesOpcion() {
        return descOpcion;
    }

    public void setDesOpcion(String descOpcion) {
        this.descOpcion = descOpcion;
    }
}
