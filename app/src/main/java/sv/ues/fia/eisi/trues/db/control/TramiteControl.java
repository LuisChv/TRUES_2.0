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
import sv.ues.fia.eisi.trues.db.entity.ActividadTramite;
import sv.ues.fia.eisi.trues.db.entity.Tramite;

public class TramiteControl {
    private final Context context;
    private DatabaseHelper databaseHelper;


    public TramiteControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    //CREAR

    public Integer crearTramite(int idFacultad, String nombreTramite)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idFacultad),nombreTramite};
        Integer ultimoId = null;

        Cursor cursor= db.query("tramite", null, "idFacultad = ? AND nombreTramite = ?", args, null,null, null);

        ContentValues values =  new ContentValues();
        values.put("idFacultad", idFacultad);
        values.put("nombreTramite",nombreTramite);

        if(cursor.moveToFirst())
        {
            mensaje = context.getText(R.string.tramite_error).toString();
        }
        else
        {
            db.insert("tramite", null, values);
            mensaje=context.getText(R.string.tramite_agregado).toString();

            Cursor cursor2 = db.rawQuery("SELECT MAX(idTramite) FROM tramite ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();
        }
        cursor.close();
        db.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        return ultimoId;
    }

    //ACTUALIZAR
    public String actualizarTramite(int idTramite, int idFacultad, String nombreTramite)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idTramite)};

        Cursor cursor= db.query("tramite", null, "idTramite = ?", args, null,null, null);

        ContentValues values =  new ContentValues();
        values.put("idTramite", idTramite);
        values.put("idFacultad", idFacultad);
        values.put("nombreTramite", nombreTramite);

        if (cursor.moveToFirst()){
            db.update("tramite",values, "idTramite = ?",args);
            mensaje = "Tramite actualizado";
        }
        else {
            mensaje = "Error, el tramite no existe";
        }
        cursor.close();
        db.close();
        return mensaje;

    }

    //ELIMINAR

    public void eliminarTramite(int idTramite){
        /*db.delete("tramite","idTramite = ?", args);
        db.delete("requisitoTramite","idTramite = ?",args);
        db.delete("documentoTramite","idTramite = ?",args);
        db.delete("actividadTramite","idTramite = ?",args);
        db.delete("usuarioTramite","idTramite = ?",args);
        db.delete("paso","idTramite = ?",args);*/
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idTramite)};
        Cursor cursorTramite = db.query("tramite",null,"idTramite = ?",args,null,null,null);
        Cursor cursorRequisitoTramite = db.query("requisitoTramite",null,"idTramite = ?",args,null,null,null);
        Cursor cursorDocumentoTramite = db.query("documentoTramite",null,"idTramite = ?",args,null,null,null);
        Cursor cursorActividadTramite = db.query("actividadTramite",null,"idTramite = ?",args,null,null,null);
        Cursor cursorUsuarioTramite = db.query("usuarioTramite",null,"idTramite = ?",args,null,null,null);
        Cursor cursorPaso = db.query("paso",null,"idTramite = ?",args,null,null,null);

        if(cursorTramite.moveToFirst())
        {
            if(cursorRequisitoTramite.moveToFirst())
            {
                db.delete("requisitoTramite","idTramite = ?",args);
            }
            if(cursorDocumentoTramite.moveToFirst())
            {
                db.delete("documentoTramite","idTramite = ?",args);
            }
            if(cursorActividadTramite.moveToFirst())
            {
                db.delete("actividadTramite","idTramite = ?",args);
            }
            if(cursorUsuarioTramite.moveToFirst())
            {
                db.delete("usuarioTramite","idTramite = ?",args);
            }
            if(cursorPaso.moveToFirst())
            {
                String [] idPaso= {String.valueOf(cursorPaso.getInt(2))};
                Cursor cursorUsuarioPaso = db.query("usuarioPaso",null,"idPaso = ?",idPaso,null,null,null);
                if (cursorUsuarioPaso.moveToFirst())
                {
                    db.delete("usuarioPaso","idPaso = ?",idPaso);
                    db.delete("paso","idTramite = ?",args);
                }else {
                    db.delete("paso","idTramite = ?",args);
                }
                cursorUsuarioPaso.close();
            }
            db.delete("tramite","idTramite = ?", args);
            mensaje = context.getText(R.string.elimindo).toString();
        } else {
            mensaje = context.getText(R.string.no_existe).toString();
        }
        cursorActividadTramite.close();
        cursorDocumentoTramite.close();
        cursorPaso.close();
        cursorRequisitoTramite.close();
        cursorUsuarioTramite.close();
        cursorTramite.close();
        db.close();

        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    //CONSULTAR

    public Tramite consultarTramite(int idTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idTramite","idFacultad","nombreTramite"};
        String[] args = {String.valueOf(idTramite)};
        Tramite tramite = null;
        Cursor cursor = db.query("tramite",columnas,"idTramite = ?",args,null,null,null);
        if(cursor.moveToFirst()){
            tramite = new Tramite();
            tramite.setIdTramite(cursor.getInt(0));
            tramite.setIdFacultad(cursor.getInt(1));
            tramite.setNombreTramite(cursor.getString(2));
        }
        cursor.close();
        db.close();
        return tramite;
    }

    public List<Tramite> ObtenerTramites(Integer idFacultad){
        String[] args = {String.valueOf(idFacultad)};
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Tramite> tramiteList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM tramite WHERE idFacultad = ?",args);

        if (cursor.moveToFirst()){
            do {
                Tramite tramite = new Tramite();
                tramite.setIdTramite(cursor.getInt(0));
                tramite.setIdFacultad(cursor.getInt(1));
                tramite.setNombreTramite(cursor.getString(2));
                tramiteList.add(tramite);
            }while (cursor.moveToNext());

        }
        cursor.close();
        return tramiteList;
    }

    public List<Tramite> obtenerTramiteActividad(Integer idActividad){
        List<Tramite> tramiteList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor;
        Tramite tramite;
        String [] args = {String.valueOf(idActividad)};
        cursor = db.rawQuery("SELECT * from tramite T  JOIN actividadTramite AT ON T.idTramite = AT.idTramite  AND AT.idActividad = ?", args);

        while (cursor.moveToNext()){
            tramite = new Tramite();
            tramite.setIdTramite(cursor.getInt(0));
            tramite.setIdFacultad(cursor.getInt(1));
            tramite.setNombreTramite(cursor.getString(2));
            tramiteList.add(tramite);
        }

        db.close();

        return tramiteList;
    }
}
