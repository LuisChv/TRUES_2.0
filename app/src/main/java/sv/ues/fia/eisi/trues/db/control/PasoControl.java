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
import sv.ues.fia.eisi.trues.db.entity.Paso;

public class PasoControl {

    private final Context context;
    private DatabaseHelper databaseHelper;

    public PasoControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }


    public void crearPaso(Integer idUbicacion, Integer idTramite, String descripcionPaso, float porcentaje){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idPersonal",idUbicacion);
        values.put("idTramite", idTramite);
        values.put("descripcionPaso",descripcionPaso);
        values.put("porcentaje", porcentaje);
        db.insert("paso",null,values);
        db.close();

        Toast.makeText(context.getApplicationContext(), "Guardado con éxito.", Toast.LENGTH_SHORT).show();
    }

    //ELIMINAR
    public void eliminarPaso(Integer id){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] values= {String.valueOf(id)};
        Paso paso=new Paso();
        paso=obtenerPaso(id);
        Cursor cursor=db.query("paso",null, "idPaso=?",values, null,null,null);
        Cursor cursor1=db.query("usuarioPaso",null,"idPaso=?",values,null,null,null);
        if(cursor.moveToFirst()){
            if(cursor1.moveToFirst()){
                db.delete("usuarioPaso","idPaso=?", values);
            }
            db.delete("paso", "idPaso = ?", values);
            mensaje= "Se ha eliminado el paso: " + paso.getDescripcion();
        }
        else{
            mensaje="Error al eliminar el mensaje, no existe o no se ha encontrado";
        }
        cursor.close();
        cursor1.close();
        db.close();

        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    //OBTENER

    public Paso obtenerPaso(Integer id){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idPaso","idPersonal","idTramite", "descripcionPaso", "porcentaje"};
        String[] values={String.valueOf(id)};
        Paso paso = null;
        Cursor cursor = db.query("paso",columnas, "idPaso = ?",values, null, null, null, null);
        if (cursor.moveToFirst()){
            paso = new Paso();
            paso.setIdPaso(cursor.getInt(0));
            paso.setIdPersonal(cursor.getInt(1));
            paso.setIdTramite(cursor.getInt(2));
            paso.setDescripcion(cursor.getString(3));
            paso.setPorcentaje(cursor.getFloat(4));
        }
        cursor.close();
        return paso;
    }

    public List<Paso> obtenerPasos(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Paso> pasoList = new ArrayList<>();
        String[] columnas = {"idPaso","idPersonal","idTramite", "descripcionPaso", "porcentaje"};
        Paso paso=null;
        Cursor cursor = db.rawQuery("SELECT * FROM paso",null);
        if (cursor.moveToFirst()){
            do{
                paso = new Paso();
                paso.setIdPaso(cursor.getInt(0));
                paso.setIdPersonal(cursor.getInt(1));
                paso.setIdTramite(cursor.getInt(2));
                paso.setDescripcion(cursor.getString(3));
                paso.setPorcentaje(cursor.getFloat(4));
                pasoList.add(paso);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return pasoList;
    }
    /*idPaso INTEGER NOT NULL PRIMARY KEY,
        idUbicacion INTEGER NOT NULL,
        idTramite INTEGER NOT NULL,
        descripcionPaso VARCHAR(256) NOT NULL,
        porcentaje REAL(10) NOT NULL*/

    public List<Paso> obtenerPasos(Integer idTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Paso> pasoList = new ArrayList<>();
        String[]args={String.valueOf(idTramite)};
        String[] columnas = {"idPaso","idPersonal","idTramite","descripcionPaso","porcentaje"};
        Paso paso=null;
        Cursor cursor = db.rawQuery("SELECT * FROM paso WHERE idTramite=?",args);
        while (cursor.moveToNext()){
                paso = new Paso();
                paso.setIdPaso(cursor.getInt(0));
                paso.setIdPersonal(cursor.getInt(1));
                paso.setIdTramite(cursor.getInt(2));
                paso.setDescripcion(cursor.getString(3));
                paso.setPorcentaje(cursor.getFloat(4));
                pasoList.add(paso);
        }
        cursor.close();
        return pasoList;
    }

    //UPDATE
    public void actualizarPaso(Integer id, Integer idPersonal, String descripcionPaso, float porcentaje){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(id)};
        Cursor cursor = db.query("paso",null,"idPaso = ?", args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("idPersonal",idPersonal);
        values.put("descripcionPaso",descripcionPaso);
        values.put("porcentaje", porcentaje);

        if (cursor.moveToFirst()){
            db.update("paso", values,"idPaso = ?", args);
            mensaje = "Paso actualizado con exito";
        }
        else {
            mensaje = "Paso no se encuentra o no existe";
        }

        cursor.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

}
