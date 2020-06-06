package sv.ues.fia.eisi.trues.db.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.entity.ActividadTramite;

public class ActividadTramiteControl {
    private final Context context;
    private DatabaseHelper databaseHelper;

    public ActividadTramiteControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    public void CrearActividadTramite(int idActividad,int idTramite){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idActividad),String.valueOf(idTramite)};
        Cursor cursor = db.query("actividadTramite",null,"idActividad = ? AND idTramite = ?",args,null,null,null);


        if (!cursor.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put("idTramite",idTramite);
            values.put("idActividad",idActividad);

            db.insert("actividadTramite",null,values);
            mensaje = "Se ha agregado el tramite " + idTramite + " a la actividad " + idActividad;
        }else {
            mensaje = "Este trámite ya fue agregado.";
        }
        cursor.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }


    public void EliminarActividadTramite(int idActividad, int idTramite){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] args = {String.valueOf(idActividad), String.valueOf(idTramite)};

        db.delete("ActividadTramite","idActividad = ? AND idTramite = ?",args);
        db.close();

        Toast.makeText(context.getApplicationContext(),
                "Se ha quitado el tramite " + idTramite + " de la actividad "+ idActividad,
                Toast.LENGTH_SHORT).show();
    }

    public List<ActividadTramite> ObtenerActividadesTramites(Integer idActividad){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<ActividadTramite> list = new ArrayList<>();
        String[] args = {String.valueOf(idActividad)};
        Cursor cursor = db.rawQuery("SELECT * FROM actividadTramite WHERE idActividad = ?",args);
        ActividadTramite actividadTramite;

        while (cursor.moveToNext()) {
            actividadTramite = new ActividadTramite();
            actividadTramite.setIdActividadTramite(cursor.getInt(0));
            actividadTramite.setIdTramite(cursor.getInt(1));
            actividadTramite.setIdActividad(cursor.getInt(2));
            list.add(actividadTramite);

            db.close();
        }
        cursor.close();
        return list;
    }
}
