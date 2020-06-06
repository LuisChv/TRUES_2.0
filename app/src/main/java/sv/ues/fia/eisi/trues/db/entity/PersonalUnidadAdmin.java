package sv.ues.fia.eisi.trues.db.entity;

public class PersonalUnidadAdmin {
    private int idPersonalAdmin, idPersona, idUnidad;

    public PersonalUnidadAdmin() {
    }

    public PersonalUnidadAdmin(int idPersonalAdmin, int idPersona, int idUnidad) {
        this.idPersonalAdmin = idPersonalAdmin;
        this.idPersona = idPersona;
        this.idUnidad = idUnidad;
    }

    public int getIdPersonalAdmin() {
        return idPersonalAdmin;
    }

    public void setIdPersonalAdmin(int idPersonalAdmin) {
        this.idPersonalAdmin = idPersonalAdmin;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public int getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(int idUnidad) {
        this.idUnidad = idUnidad;
    }
}
