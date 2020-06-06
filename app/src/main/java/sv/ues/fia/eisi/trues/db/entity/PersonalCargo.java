package sv.ues.fia.eisi.trues.db.entity;

public class PersonalCargo {
    private int idPersonalCargo, idCargo, idPersonal;

    public PersonalCargo() {
    }

    public PersonalCargo(int idPersonalCargo, int idCargo, int idPersonal) {
        this.idPersonalCargo = idPersonalCargo;
        this.idCargo = idCargo;
        this.idPersonal = idPersonal;
    }

    public int getIdPersonalCargo() {
        return idPersonalCargo;
    }

    public void setIdPersonalCargo(int idPersonalCargo) {
        this.idPersonalCargo = idPersonalCargo;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public int getIdPersonal() {
        return idPersonal;
    }

    public void setIdPersonal(int idPersonal) {
        this.idPersonal = idPersonal;
    }
}
