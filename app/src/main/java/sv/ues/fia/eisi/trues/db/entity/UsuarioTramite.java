package sv.ues.fia.eisi.trues.db.entity;

public class UsuarioTramite {
    private Integer idUsuarioT;
    private float progreso;
    private String usuario;
    private Integer idTramite;

    public UsuarioTramite(Integer id, float p, String user,Integer idTramite){
        this.idUsuarioT=id;
        this.progreso=p;
        this.idTramite = idTramite;
        this.usuario=user;
    }

    public UsuarioTramite(){ }

    public Integer getIdUsuarioT(){return this.idUsuarioT;}

    public void setIdUsuarioT(Integer id){ this.idUsuarioT=id;}

    public float getProgreso(){return this.progreso;}

    public void setProgreso(float p){this.progreso=p;}

    public String getUsuario(){ return this.usuario;}

    public void setUsuario(String u){this.usuario=u;}

    public Integer getIdTramite(){ return this.idTramite;}

    public void setIdTramite(Integer i){this.idTramite=i;}
}
