package sv.ues.fia.eisi.trues.db.entity;

/*idUsuarioPaso INTEGER NOT NULL PRIMARY KEY,
 usuario VARCHAR(7) NOT NULL,
 idPaso INTEGER NOT NULL*/
public class UsuarioPaso {
    private Integer idUsuarioPaso;
    private String usuario;
    private Integer idPaso;
    private boolean estado;

    public UsuarioPaso(Integer idUsuarioPaso, Integer idPaso, String usuario, boolean e){
        this.idUsuarioPaso=idUsuarioPaso;
        this.idPaso=idPaso;
        this.usuario=usuario;
        this.estado=e;
    }

    public UsuarioPaso(){ }
    public Integer getIdUsuarioPaso(){return this.idUsuarioPaso;}

    public void setIdUsuarioPaso(Integer id){ this.idUsuarioPaso=id;}

    public Integer getIdPaso(){return this.idPaso;}

    public void setIdPaso(Integer id){this.idPaso=id;}

    public String getUsuario(){return this.usuario;}

    public void setUsuario(String usuario){this.usuario=usuario;}

    public boolean getEstado(){ return this.estado;}

    public void setEstado(boolean e){ this.estado=e;}
}
