package sv.ues.fia.eisi.trues.db.entity;

public class Documento {
    private int idDocumento;
    private String url;
    private String nombreDocumento;

    public Documento() {
    }

    public Documento(int idDocumento, String url, String nombreDocumento) {
        this.idDocumento = idDocumento;
        this.url = url;
        this.nombreDocumento = nombreDocumento;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDoc(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }
}
