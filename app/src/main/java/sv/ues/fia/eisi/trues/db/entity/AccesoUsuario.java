package sv.ues.fia.eisi.trues.db.entity;

public class AccesoUsuario {
    private int idAccesoUsuario;
    private String usuario;
    private String idOpotion;

    public AccesoUsuario() {
    }

    public AccesoUsuario(String usuario, String idOpotion) {
        this.usuario = usuario;
        this.idOpotion = idOpotion;
    }

    public int getIdAccesoUsuario() {
        return idAccesoUsuario;
    }

    public void setIdAccesoUsuario(int idAccesoUsuario) {
        this.idAccesoUsuario = idAccesoUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIdOpotion() {
        return idOpotion;
    }

    public void setIdOpotion(String idOpotion) {
        this.idOpotion = idOpotion;
    }
}
