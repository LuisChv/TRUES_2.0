package sv.ues.fia.eisi.trues.db.entity;
/*idUnidad INTEGER NOT NULL PRIMARY KEY,
 idFacultad INTEGER NOT NULL,
 nombreUnidadAdmin VARCHAR(64) NOT NULL*/
public class UnidadAdmin {
    private Integer idUAdmin;
    private Integer idFacultad;
    private String nombreUnidadAdmin;

    public UnidadAdmin(Integer id,Integer idFacultad, String nombre){
        this.idUAdmin=id;
        this.idFacultad=idFacultad;
        this.nombreUnidadAdmin=nombre;
    }

    public UnidadAdmin(){ }

    public Integer getIdUAdmin(){ return this.idUAdmin;}

    public void setIdUAdmin(Integer id){ this.idUAdmin=id; }

    public Integer getIdFacultad(){return this.idFacultad;}

    public void setIdFacultad(Integer id){this.idFacultad=id;}

    public String getNombreUAdmin(){return this.nombreUnidadAdmin;}

    public void setNombreUAdmin(String nombre){ this.nombreUnidadAdmin=nombre; }

}
