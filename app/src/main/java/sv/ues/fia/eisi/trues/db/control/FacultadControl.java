package sv.ues.fia.eisi.trues.db.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.entity.Facultad;

public class FacultadControl {
    private final Context context;
    private DatabaseHelper databaseHelper;

    public FacultadControl(Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context,"TRUES",null,1);
    }


    //Create
    public void crearFacultad(String nombreFacultad){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {nombreFacultad};
        String column[] = {"nombreFacultad"};
        Cursor cursor = db.query("facultad",column,"nombreFacultad = ?", args,null,null,null);
        ContentValues values = new ContentValues();
        if (cursor.moveToFirst()){
            mensaje ="Esta facultad ya ha sido registrada anteriormente";
        } else {
            values.put("nombreFacultad",nombreFacultad);
            db.insert("facultad",null,values);
            mensaje = "Se ha agregado una nueva Facultad";
        }
        db.close();

        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    //Read
    public Facultad consultarFacultad(int idFacultad){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idFacultad","nombreFacultad"};
        String args[] = {String.valueOf(idFacultad)};
        Facultad facultad = null;
        Cursor cursor = db.query("facultad",columnas,"idFacultad = ?",args,null,null,null);
        if(cursor.moveToFirst()){
            facultad = new Facultad();
            facultad.setIdFacultad(cursor.getInt(0));
            facultad.setNombreFacultad(cursor.getString(1));
        }

        return facultad;
    }

    public List<Facultad> consultarFacultades(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idFacultad","nombreFacultad"};
        List<Facultad> facultades = new ArrayList<>();
        Facultad facultad = null;
        Cursor cursor = db.query("facultad",columnas,null,null,null,null,null);

        if (cursor.moveToFirst()){
            do {
                facultad = new Facultad();
                facultad.setIdFacultad(cursor.getInt(0));
                facultad.setNombreFacultad(cursor.getString(1));
                facultades.add(facultad);

            }while (cursor.moveToNext());

        }
        return facultades;
    }

    //Update
    public String actualizarFacultad(int idFacultad, String nombreFacultad){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idFacultad)};
        Cursor cursor = db.query("facultad",null,"idFacultad = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("idFacultad",idFacultad);
        values.put("nombreFacultad",nombreFacultad);
        if (cursor.moveToFirst()){
            db.update("facultad",values,"idFacultad = ?",args);
            mensaje = "Se ha actualizado el nombre de la Facultad";
        } else {
            mensaje = "No se ha encontrado el registro";
        }
        return mensaje;
    }
}
