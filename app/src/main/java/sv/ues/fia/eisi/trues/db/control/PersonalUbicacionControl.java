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
import sv.ues.fia.eisi.trues.db.entity.PersonalUbicacion;
import sv.ues.fia.eisi.trues.db.entity.Ubicacion;


public class PersonalUbicacionControl {

    private final Context context;
    private DatabaseHelper databaseHelper;
    private UbicacionControl ubicacionControl;


    public PersonalUbicacionControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    //CREAR

    public void crearPersonalUbicacion(int idPersonal, int idUbicaion)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String [] args = {String.valueOf(idPersonal),String.valueOf(idUbicaion)};
        Cursor cursor= db.query("personalUbicacion",null, "idPersonal = ? AND idUbicacion = ?" ,args,null,null,null);
        ContentValues values =  new ContentValues();
        values.put("idPersonal", idPersonal);
        values.put("idUbicacion", idUbicaion);

        if(cursor.moveToFirst())
        {
            mensaje = context.getText(R.string.error).toString();
        }
        else
        {
            db.insert("personalUbicacion", null, values);
            mensaje=context.getText(R.string.cambios_guardados).toString();
        }
        db.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    //ACTUALIZAR
    public String actualizarPersonalUbicacion(int idPersonalUbicacion, int idPersonal, int idUbicacion)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idPersonalUbicacion)};
        Cursor cursor= db.query("personalUbicacion", null, "idPersonalUbicaion = ?", args, null,null, null);
        ContentValues values =  new ContentValues();
        values.put("idPersonalUbicacion", idPersonalUbicacion);
        values.put("idPersonal", idPersonal);
        values.put("idUbicacion", idUbicacion);
        if (cursor.moveToFirst()){
            db.update("personalUbicacion",values,"idPersonalUbicacion = ?",args);
            mensaje = "Registro actualizado";
        }
        else {
            mensaje = "Error al actualizar";
        }
        db.close();
        return mensaje;

    }

    //ELIMINAR

    public void eliminarPersonalUbicacion(int idPersonal, int idUbicacion){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String args[] = {String.valueOf(idPersonal), String.valueOf(idUbicacion)};
        db.delete("personalUbicacion","idPersonal = ? AND idUbicacion = ?", args);
        db.close();
        Toast.makeText(context.getApplicationContext(), context.getText(R.string.cambios_guardados).toString(), Toast.LENGTH_SHORT).show();
    }

    //CONSULTAR
    public List<Ubicacion> consultarPersonalUbicaciones(int idPersonal){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List ubicacionList=new ArrayList();
        String args[] ={String.valueOf(idPersonal)};
        String[] columnas = {"idPersonalUbicacion","idUbicacion","idPersonal"};
        Ubicacion[] ubicaciones = null;
        Ubicacion ubicacion = null;
        /*idUbicacion INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
         longitud REAL NOT NULL,
         latitud REAL NOT NULL,
         altitud REAL NOT NULL,
         componenteTematica VARCHAR(128) NOT NULL*/
        Cursor cursor = db.rawQuery("SELECT u.idUbicacion, u.longitud, u.latitud, u.altitud, u.componenteTematica FROM ubicacion AS u INNER JOIN personal AS p ON pc.idPersonal=p.idPersonal WHERE pc.idPersonal=?",args);

        while (cursor.moveToNext())
        {
            ubicacion=new Ubicacion();
            ubicacion.setIdUbicacion(cursor.getInt(0));
            ubicacion.setLongitud(cursor.getFloat(1));
            ubicacion.setLatidud(cursor.getFloat(2));
            ubicacion.setAltitud(cursor.getFloat(3));
            ubicacion.setComponenteTematica(cursor.getString(4));
            ubicacionList.add(ubicacion);

        }
        return ubicacionList;
    }


    }
