package sv.ues.fia.eisi.trues.db.entity;

public class DocumentoTramite {
    private int idDocumentoTramite;
    private int idDocumento;
    private int idTramite;

    public DocumentoTramite() {
    }

    public DocumentoTramite(int idDocumentoTramite, int idDocumento, int idTramite) {
        this.idDocumentoTramite = idDocumentoTramite;
        this.idDocumento = idDocumento;
        this.idTramite = idTramite;
    }

    public int getIdDocumentoTramite() {
        return idDocumentoTramite;
    }

    public void setIdDocumentoTramite(int idDocumentoTramite) {
        this.idDocumentoTramite = idDocumentoTramite;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public int getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(int idTramite) {
        this.idTramite = idTramite;
    }
}
