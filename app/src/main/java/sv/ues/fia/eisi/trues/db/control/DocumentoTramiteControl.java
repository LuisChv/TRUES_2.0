package sv.ues.fia.eisi.trues.db.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.entity.Documento;
import sv.ues.fia.eisi.trues.db.entity.DocumentoTramite;

public class DocumentoTramiteControl {
    private final Context context;
    private DatabaseHelper databaseHelper;
    private DocumentoControl documentoControl;

    public DocumentoTramiteControl(Context context) {
        this.context = context;
        documentoControl = new DocumentoControl(context);
        this.databaseHelper = new DatabaseHelper(context,"TRUES",null,1);
    }

    //Create
    public void crearDocumentoTramite(int idDocumento, int idTramite){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idDocumento),String.valueOf(idTramite)};
        String[] columns = {"idDocumento, idTramite"};
        Cursor cursor = db.query("documentoTramite",columns,"idDocumento = ? AND idTramite = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        if(cursor.moveToFirst()){
            mensaje = context.getText(R.string.error_documento).toString();
        } else {
            values.put("idDocumento",idDocumento);
            values.put("idTramite",idTramite);
            db.insert("documentoTramite",null,values);
            mensaje = context.getText(R.string.documento_creado).toString();
        }
        db.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    //Read
    public List<DocumentoTramite> consultarDocumentosTramites(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idDocumentoTramite","idDocumento","idTramite"};
        List<DocumentoTramite> arrayDocumentosTramites = new ArrayList<>();
        DocumentoTramite documentoTramite = null;
        Cursor cursor = db.query("documentoTramite",columnas,null,null,null,null,null);

        if (cursor.moveToFirst()){


            do {
                documentoTramite = new DocumentoTramite();
                documentoTramite.setIdDocumentoTramite(cursor.getInt(0));
                documentoTramite.setIdDocumento(cursor.getInt(1));
                documentoTramite.setIdDocumento(cursor.getInt(2));
                arrayDocumentosTramites.add(documentoTramite);
            }while (cursor.moveToNext());

        }
        return arrayDocumentosTramites;
    }

    public DocumentoTramite consultarDocumentoTramiteEspifico(int idDocumentoTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idDocumentoTramite","idDocumento","idTramite"};
        String args[] = {String.valueOf(idDocumentoTramite)};
        DocumentoTramite documentoTramite = null;
        Cursor cursor = db.query("documentoTramite",columnas,"idDocumentoTramite = ? ", args,null,null,null);
        if (cursor.moveToFirst()){
            documentoTramite = new DocumentoTramite();
            documentoTramite.setIdDocumentoTramite(cursor.getInt(0));
            documentoTramite.setIdDocumento(cursor.getInt(1));
            documentoTramite.setIdTramite(cursor.getInt(2));
        }

        return documentoTramite;
    }

    public List<Documento> consultarDocumentosDelTramite(int idTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String args[] = {String.valueOf(idTramite)};
        String[] columnas = {"idDocumentoTramite","idDocumento","idTramite"};
        List<Documento> arrayDocumento = new ArrayList<>();
        Documento documento = null;
        Cursor cursor = db.query("documentoTramite",columnas,"idTramite = ?", args,null,null,null);
        if (cursor.moveToFirst()){

            do{
                documento = documentoControl.consultarDocumeto(cursor.getInt(1));
                arrayDocumento.add(documento);
            }while(cursor.moveToNext());
        }

        return arrayDocumento;
    }

    //Update
    public String actualizarDocumentosTramite(int idDocumentoTramite, int idDocumento, int idTramite){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idDocumentoTramite)};
        Cursor cursor = db.query("documentoTramite",null,"idDocumentoTramite = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("idDocumentoTramite",idDocumentoTramite);
        values.put("idDocumento",idDocumento);
        values.put("idTramite",idTramite);
        if (cursor.moveToFirst()){
            db.update("documentoTramite",values,"idDocumentoTramite = ?",args);
            mensaje = "Se ha actualizado con exito el documento del tramite";
        } else {
            mensaje = "No se ha encontrado ningun registro";
        }
        return mensaje;
    }

    //Delete
    public void eliminarDocumentoTramite(int idDocumento, int idTramite){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String args[] = {String.valueOf(idDocumento), String.valueOf(idTramite)};
        db.delete("documentoTramite","idDocumento = ? AND idTramite = ?",args);
        db.close();
        Toast.makeText(context.getApplicationContext(), context.getText(R.string.documento_eliminado).toString(), Toast.LENGTH_SHORT).show();
    }
}
