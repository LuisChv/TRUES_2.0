package sv.ues.fia.eisi.trues.db.entity;

public class PersonalUbicacion {
    private int idPersonalUbicacion;
    private int idPersonal;
    private int idUbicacion;

    public PersonalUbicacion(int idPersonalUbicacion, int idPersonal, int idUbicacion) {
        this.idPersonalUbicacion = idPersonalUbicacion;
        this.idPersonal = idPersonal;
        this.idUbicacion = idUbicacion;
    }

    public PersonalUbicacion() {
    }

    public int getIdPersonalUbicacion() {
        return idPersonalUbicacion;
    }

    public void setIdPersonalUbicacion(int idPersonalUbicacion) {
        this.idPersonalUbicacion = idPersonalUbicacion;
    }

    public int getIdPersonal() {
        return idPersonal;
    }

    public void setIdPersonal(int idPersonal) {
        this.idPersonal = idPersonal;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }
}
