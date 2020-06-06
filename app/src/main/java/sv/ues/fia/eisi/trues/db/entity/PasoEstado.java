package sv.ues.fia.eisi.trues.db.entity;

public class PasoEstado {
    private int idUsuarioPaso;
    private int idPaso;
    private String descripcionPaso;
    private boolean estado;
    private float porcentaje;

    public PasoEstado() {
    }

    public PasoEstado(int idUsuarioPaso, int idPaso, String descripcionPaso, boolean estado, float porcentaje) {
        this.idUsuarioPaso = idUsuarioPaso;
        this.idPaso = idPaso;
        this.descripcionPaso = descripcionPaso;
        this.estado = estado;
        this.porcentaje = porcentaje;
    }

    public int getIdUsuarioPaso() {
        return idUsuarioPaso;
    }

    public void setIdUsuarioPaso(int idUsuarioPaso) {
        this.idUsuarioPaso = idUsuarioPaso;
    }

    public int getIdPaso() {
        return idPaso;
    }

    public void setIdPaso(int idPaso) {
        this.idPaso = idPaso;
    }

    public String getDescripcionPaso() {
        return descripcionPaso;
    }

    public void setDescripcionPaso(String descripcionPaso) {
        this.descripcionPaso = descripcionPaso;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(float porcentaje) {
        this.porcentaje = porcentaje;
    }
}
