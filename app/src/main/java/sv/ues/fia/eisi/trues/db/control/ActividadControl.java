package sv.ues.fia.eisi.trues.db.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.entity.Actividad;

public class ActividadControl {

    private final Context context;
    private DatabaseHelper databaseHelper;


    public ActividadControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    public Integer CrearActividad(int idFacultad, String nombreActividad, String fechaInicio, String fechaFinal)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] idFacNom = {String.valueOf(idFacultad),nombreActividad};
        Integer ultimoId = null;

        Cursor cursor= db.query("actividad", null, "idFacultad = ? and nombreActividad = ?", idFacNom, null,null, null);

        ContentValues values =  new ContentValues();
        values.put("idFacultad", idFacultad);
        values.put("nombreActividad", nombreActividad);
        values.put("inicio", fechaInicio);
        values.put("final", fechaFinal);

        if (cursor.moveToFirst()){
            Toast.makeText(context.getApplicationContext(), "Error, esta actividad ya existe.", Toast.LENGTH_SHORT).show();
        }
        else {
            db.insert("actividad", null, values);

            Cursor cursor2 = db.rawQuery("SELECT MAX(idActividad) FROM actividad ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();

        }
        cursor.close();
        db.close();

        return  ultimoId;
    }

    public void CrearActividadID(int idActividad, int idFacultad, String nombreActividad, String fechaInicio, String fechaFinal)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values =  new ContentValues();
        values.put("idActividad", idActividad);
        values.put("idFacultad", idFacultad);
        values.put("nombreActividad", nombreActividad);
        values.put("inicio", fechaInicio);
        values.put("final", fechaFinal);

        db.insert("actividad", null, values);
        db.close();

        Toast.makeText(context.getApplicationContext(), "Se ha creado la actividad " + idActividad + " con éxito.", Toast.LENGTH_SHORT).show();
    }

    public void EliminarActividad(int idActividad){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idActividad)};
        String[] columnasActividad = {"idActividad"};
        Cursor cursor = db.query("actividad",null,"idActividad = ?",args,null,null,null);
        Cursor cursor1 = db.query("actividadTramite", columnasActividad,"idActividad = ?",args,null,null,null);
        if (cursor.moveToFirst()){
            if (cursor1.moveToFirst()){
                db.delete("actividadTramite","idActividad = ?",args);
                db.delete("actividad","idActividad = ?",args);
            }else {
                db.delete("actividad","idActividad = ?",args);
            }
        }
        cursor.close();
        cursor1.close();
        db.close();

    }

    public Actividad ObtenerActividad(int idActividad){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] id = {String .valueOf(idActividad)};
        String[] columnas = {"idActividad","idFacultad","nombreActividad", "inicio","final" };

        Cursor cursor = db.query("actividad",columnas,"idActividad = ?",id,null,null,null);
        if(cursor.moveToFirst()){
            Actividad actividad = new Actividad();
            actividad.setIdActividad(cursor.getInt(0));
            actividad.setIdFacultad(cursor.getInt(1));
            actividad.setNombreActividad(cursor.getString(2));
            actividad.setInicio(cursor.getString(3));
            actividad.setFin(cursor.getString(4));
            db.close();
            cursor.close();
            return actividad;
        }else {
            cursor.close();
            return null;
        }
    }

    public List<Actividad> ObtenerActividades(Integer idFacultad){
        String[] args = {String.valueOf(idFacultad)};
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Actividad> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM actividad WHERE idFacultad = ?",args);
        if (cursor.moveToFirst()){
            do {
                Actividad actividad = new Actividad();
                actividad.setIdActividad(cursor.getInt(0));
                actividad.setIdFacultad(cursor.getInt(1));
                actividad.setNombreActividad(cursor.getString(2));
                actividad.setInicio(cursor.getString(3));
                actividad.setFin(cursor.getString(4));
                list.add(actividad);
            }while (cursor.moveToNext());

            cursor.close();
        }

        return list;
    }

    public void ActualizarActividad(Integer idActividad, String nombreActividad, String fechaInicio, String fechaFinal){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idActividad)};
        Cursor cursor = db.query("actividad",null,"idActividad = ?", args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("nombreActividad",nombreActividad);
        values.put("inicio",fechaInicio);
        values.put("final",fechaFinal);

        if (cursor.moveToFirst()){
            db.update("actividad", values,"idActividad = ?", args);
            mensaje = "Se ha actualizado la Actividad "+idActividad+" con exito";
        }else {
            mensaje = "Error al actualizar";
        }
        db.close();
        cursor.close();

        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
