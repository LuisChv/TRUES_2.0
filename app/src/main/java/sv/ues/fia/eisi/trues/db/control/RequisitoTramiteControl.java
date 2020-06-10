package sv.ues.fia.eisi.trues.db.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.entity.Requisito;
import sv.ues.fia.eisi.trues.db.entity.RequisitoTramite;

public class RequisitoTramiteControl {
    private final Context context;
    private DatabaseHelper databaseHelper;
    private RequisitoControl requisitoControl;


    public RequisitoTramiteControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    //CREAR

    public void crearRequisitoTramite(int idTramite, int idRequisito)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String [] args = {String.valueOf(idTramite),String.valueOf(idRequisito)};
        Cursor cursor= db.query("requisitoTramite",null, "idTramite = ? AND idRequisito = ?" ,args,null,null,null);
        ContentValues values =  new ContentValues();
        values.put("idTramite", idTramite);
        values.put("idRequisito", idRequisito);

        if(cursor.moveToFirst())
        {
            mensaje = context.getText(R.string.error_duplicado).toString();
        }
        else
        {
            db.insert("requisitoTramite", null, values);
            mensaje=context.getText(R.string.cambios_guardados).toString();
        }

        db.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    //ACTUALIZAR
    public String actualizarRequisito(int idRequisitoTramite, int idTramite, int idRequisito)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idRequisitoTramite)};

        Cursor cursor= db.query("requisitoTramite", null, "idRequisitoTramite = ?", args, null,null, null);
        ContentValues values =  new ContentValues();
        values.put("idRequisitoTramite", idRequisitoTramite);
        values.put("idTramite", idTramite);
        values.put("idRequisito", idRequisito);

        if (cursor.moveToFirst()){
            db.update("requisito",values, "idRequisito = ?",args);
            mensaje = "Registro actualizado";
        }
        else {
            mensaje = "Error, el registro no existe";
        }

        db.close();
        return mensaje;
    }

    //ELIMINAR

    public void eliminarRequisitoTramite(int idRequisito, int idTramite){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String args[] = {String.valueOf(idRequisito), String.valueOf(idTramite)};
        db.delete("requisitoTramite","idRequisito = ? AND idTramite = ?", args);
        db.close();
        Toast.makeText(context.getApplicationContext(), context.getText(R.string.cambios_guardados).toString(), Toast.LENGTH_SHORT).show();
    }

    //CONSULTAR (todos los requisitos de un tramite)

    public Requisito[] consultarRequisitosTramites(int idTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String args[] ={String.valueOf(idTramite)};
        String[] columnas = {"idRequisitoTramite","idRequisito","idTramite"};
        Requisito[] requisitos = null;
        Requisito requisito = null;
        Cursor cursor = db.query("requisitoTramite",columnas,"idTramite = ?",args,null,null,null);

        if (cursor.moveToFirst()){
            int i =0;
            do {
                requisito= requisitoControl.consultarRequisito(cursor.getInt(2));
                requisitos[i] = requisito;
                i++;
            }while (cursor.moveToNext());
        }
        return requisitos;
    }


}
