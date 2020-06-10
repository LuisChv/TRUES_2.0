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
import sv.ues.fia.eisi.trues.db.entity.AccesoUsuario;

public class AccesoUsuarioControl {
    private final Context context;
    private DatabaseHelper databaseHelper;

    public AccesoUsuarioControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    public void CrearAccesoUsuario(String usuario, String idOption){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        ContentValues values = new ContentValues();
        String [] columnasNull = {"idAccesoUsuario"};
        values.put("usuario", usuario);
        values.put("idOption", idOption);

        String [] args = {usuario, String.valueOf(idOption)};
        Cursor cursor = db.query("accesoUsuario", null, "usuario = ? AND idOption = ?", args, null, null, null);

        if (!cursor.moveToFirst()){
            db.insert("accesoUsuario", null, values);
            mensaje = "Se ha concedido el permiso " + values.get("idOption") + " al usuario " + values.get("usuario");
        }
        else {
            mensaje = "El usuario seleccionado ya posee este permiso.";
        }

        //Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void CrearAccesoUsuario2(String usuario, String idOption){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        ContentValues values = new ContentValues();
        String [] columnasNull = {"idAccesoUsuario"};
        values.put("usuario", usuario);
        values.put("idOption", idOption);

        String [] args = {usuario, String.valueOf(idOption)};
        Cursor cursor = db.query("accesoUsuario", null, "usuario = ? AND idOption = ?", args, null, null, null);

        if (!cursor.moveToFirst()){
            db.insert("accesoUsuario", null, values);
            mensaje = context.getText(R.string.permiso_concedido).toString();
        }
        else {
            mensaje = context.getText(R.string.error_permiso).toString();
        }

        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void eliminarPermiso(String usuario, String idOption){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {usuario, idOption};

        Cursor cursor = db.query("accesoUsuario", null, "usuario = ? AND idOption = ?", args, null, null, null);

        if (cursor.moveToFirst()){
            db.delete("accesoUsuario", "usuario = ? AND idOption = ?", args);
            mensaje = context.getText(R.string.permiso_suprimido).toString();
        }
        else {
            mensaje = context.getText(R.string.error).toString();
        }

        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public boolean existe(String usuario, String idOption){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String [] args = {usuario, idOption};
        Cursor cursor = db.query("accesoUsuario", null, "usuario = ? AND idOption = ?", args, null, null, null);
        if (cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }
    }
}
