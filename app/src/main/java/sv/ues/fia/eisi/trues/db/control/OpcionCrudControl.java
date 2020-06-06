package sv.ues.fia.eisi.trues.db.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.entity.OpcionCrud;

public class OpcionCrudControl {

    private final Context context;
    private DatabaseHelper databaseHelper;

    public OpcionCrudControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    public void crearOpcion(String idOpcion, String descOpcion){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] args = {String.valueOf(idOpcion)};
        Cursor cursor = db.query("opcionCrud", null, "idOpcion = ?", args, null,null, null);
        ContentValues values = new ContentValues();
        values.put("idOpcion", idOpcion);
        values.put("descOpcion", idOpcion);

        if (!cursor.moveToFirst()){
            db.insert("opcionCrud", null, values);

            Toast.makeText(context.getApplicationContext(), "Se ha creado la opcion " + idOpcion, Toast.LENGTH_SHORT).show();
        }
    }

    public List<OpcionCrud> obtenerPermisos(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM opcionCrud ORDER BY descOpcion ASC", null);
        List<OpcionCrud> list = new ArrayList<>();
        OpcionCrud opcionCrud;

        while (cursor.moveToNext()){
            opcionCrud = new OpcionCrud();
            opcionCrud.setIdOpcion(cursor.getString(0));
            opcionCrud.setDesOpcion(cursor.getString(1));
            list.add(opcionCrud);
        }
        return list;
    }

    public List<OpcionCrud> obtenerPermisos(String usuario){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] args = {usuario};
        Cursor cursor = db.rawQuery(
                "SELECT * FROM opcionCrud op JOIN accesoUsuario au " +
                        "WHERE (op.idOpcion = au.idOption AND au.usuario = ?) ORDER BY descOpcion ASC", args);
        List<OpcionCrud> list = new ArrayList<>();
        OpcionCrud opcionCrud;

        while (cursor.moveToNext()){
            opcionCrud = new OpcionCrud();
            opcionCrud.setIdOpcion(cursor.getString(0));
            opcionCrud.setDesOpcion(cursor.getString(1));
            list.add(opcionCrud);
        }
        return list;
    }

}
