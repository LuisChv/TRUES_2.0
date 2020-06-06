package sv.ues.fia.eisi.trues.db.control;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.entity.Documento;

public class DocumentoControl {

    private final Context context;
    private DatabaseHelper databaseHelper;

    public DocumentoControl(Context context){
        this.context = context;
        databaseHelper = new DatabaseHelper(context,"TRUES",null,1);
    }

    public Integer CrearDocumento(String url, String nombreDocumento){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;
        String[] args = {url};
        Cursor cursor = db.query("documento",null,"url = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        if(cursor.moveToFirst()){
            mensaje = "El documento ya existe";
        } else {
            values.put("url",url);
            values.put("nombreDocumento",nombreDocumento);
            db.insert("documento",null,values);
            mensaje = "Se ha registrado un nuevo documento";

            Cursor cursor2 = db.rawQuery("SELECT MAX(idDocumento) FROM documento ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();
        }
        db.close();
        cursor.close();

        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        return ultimoId;
    }

    //Read
    public List<Documento> consultarDocumentos(Integer idTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Documento> arrayDocumentos = new ArrayList<>();
        Documento documento = null;
        String [] args = {String.valueOf(idTramite)};
        Cursor cursor = db.rawQuery("SELECT * FROM documento d JOIN documentoTramite dT  " +
                "WHERE d.idDocumento = dT.idDocumento AND dt.idTramite = ?", args);

        while (cursor.moveToNext()){
                documento = new Documento();
                documento.setIdDoc(cursor.getInt(0));
                documento.setUrl(cursor.getString(1));
                documento.setNombreDocumento(cursor.getString(2));
                arrayDocumentos.add(documento);
        }
        cursor.close();
        return arrayDocumentos;
    }

    public List<Documento> consultarDocumentos(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Documento> arrayDocumentos = new ArrayList<>();
        Documento documento = null;
        Cursor cursor = db.rawQuery("SELECT * FROM documento", null);

        while (cursor.moveToNext()){
            documento = new Documento();
            documento.setIdDoc(cursor.getInt(0));
            documento.setUrl(cursor.getString(1));
            documento.setNombreDocumento(cursor.getString(2));
            arrayDocumentos.add(documento);
        }
        cursor.close();
        return arrayDocumentos;
    }

    public Documento consultarDocumeto(int idDocumento){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idDocumento","url","nombreDocumento"};
        String[] args = {String.valueOf(idDocumento)};
        Documento documento = null;
        Cursor cursor = db.query("documento",columnas,"idDocumento = ? ", args,null,null,null);
        if (cursor.moveToFirst()){
            documento = new Documento();
            documento.setIdDoc(cursor.getInt(0));
            documento.setUrl(cursor.getString(1));
            documento.setNombreDocumento(cursor.getString(2));
        }
        cursor.close();
        return documento;
    }

    public void ActualizarDocumento(int idDocumento, String url, String nombreDocumento){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idDocumento)};
        Cursor cursor = db.query("documento",null,"idDocumento = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("idDocumento",idDocumento);
        values.put("url",url);
        values.put("nombreDocumento",nombreDocumento);
        if(cursor.moveToFirst()){
            db.update("documento",values,"idDocumento = ?", args);
            mensaje = "Se ha actualizado con exito el documento";
        } else {
            mensaje = "No se ha encontrado el documento";
        }
        cursor.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public String EliminarDocumento(int idDocumento){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idDocumento)};
        String[] columnasPersonalCargo = {"idDocumento"};
        Cursor cursorDocumentoTramite = db.query("documentoTramite",columnasPersonalCargo,"idDocumento = ?", args,null,null,null);
        Cursor cursorDocumento = db.query("documento",null,"idDocumento = ?",args,null,null,null);
        if(cursorDocumentoTramite.moveToNext()){
            if (cursorDocumento.moveToFirst()){
                db.delete("documentoTramite","idDocumento = ?", args);
                db.delete("documento","idDocumento = ?", args);
                mensaje = "Se ha eliminado correctamente el cargo "+idDocumento+" Además se eliminó un elemento de la tabla documentoTramite";
            }
            else {
                db.delete("cargo","idDocumento = ?",args);
                mensaje = "Se ha eliminado el documento " + idDocumento + " con éxito.";
            }
        }else {
            mensaje ="Error al eliminar";
        }
        db.close();
        cursorDocumento.close();
        cursorDocumentoTramite.close();
        return mensaje;
    }



}
