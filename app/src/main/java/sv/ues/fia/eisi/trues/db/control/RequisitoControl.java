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
import sv.ues.fia.eisi.trues.db.entity.Requisito;

public class RequisitoControl {
    private final Context context;
    private DatabaseHelper databaseHelper;


    public RequisitoControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    //CREAR

    public Integer crearRequisito(String descripcion)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;
        String [] args = {descripcion};
        Cursor cursor = db.query("requisito",null,"descripcion = ?",args,null,null,null);
        ContentValues values =  new ContentValues();
        values.put("descripcion", descripcion);
        if (cursor.moveToFirst()){
            mensaje = context.getText(R.string.error_requisito).toString();
        }else {
            db.insert("requisito", null, values);
            mensaje = context.getText(R.string.requisito_creado).toString();

            Cursor cursor2 = db.rawQuery("SELECT MAX(idRequisito) FROM requisito ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();
        }

        cursor.close();
        db.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();

        return  ultimoId;
    }

    //ACTUALIZAR
    public String actualizarRequisito(int idRequisito, String descripcion)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idRequisito)};
        Cursor cursor= db.query("requisito", null, "idRequisito = ?", args, null,null, null);
        ContentValues values =  new ContentValues();
        values.put("idRequisito",idRequisito);
        values.put("descripcion",descripcion);

        if (cursor.moveToFirst()){
            db.update("requisito",values, "idRequisito = ?",args);
            mensaje = "Registro actualizado";
        }
        else {
            mensaje = "Error, el registro no existe";
        }
        cursor.close();
        db.close();
        return mensaje;
    }

    //ELIMINAR

    public void eliminarRequisito(int idRequisito){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String args[] = {String.valueOf(idRequisito)};
        db.delete("requisito","idRequisito = ?", args);
        db.delete("requisitoTramite","idRequisito = ?",args);
        db.close();
    }

    //CONSULTAR

    public Requisito consultarRequisito(int idRequisito){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idRequisito","descripcion"};
        String args[] = {String.valueOf(idRequisito)};
        Requisito requisito = null;
        Cursor cursor = db.query("requisito",columnas,"idRequisito = ?",args,null,null,null);
        if(cursor.moveToFirst()){
            requisito = new Requisito();
            requisito.setIdRequisito(cursor.getInt(0));
            requisito.setDescripcion(cursor.getString(1));

        }
        cursor.close();
        return requisito;
    }

    //CONSULTAR VARIOS

    public List<Requisito> consultarRequisitos(Integer idTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idRequisito","descripcion"};
        List<Requisito> requisitos = new ArrayList<>();
        Requisito requisito = null;
        String [] args = {String.valueOf(idTramite)};
        Cursor cursor = db.rawQuery("SELECT * FROM requisito r JOIN requisitoTramite rT  WHERE r.idRequisito = rT.idRequisito AND rt.idTramite = ?", args);

        if (cursor.moveToFirst()){

            do {
                requisito = new Requisito();
                requisito.setIdRequisito(cursor.getInt(0));
                requisito.setDescripcion(cursor.getString(1));
                requisitos.add(requisito);
            }while (cursor.moveToNext());

        }
        cursor.close();
        return requisitos;
    }

    public List<Requisito> consultarRequisitos(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Requisito> requisitos = new ArrayList<>();
        Requisito requisito = null;
        Cursor cursor = db.rawQuery("SELECT * FROM requisito", null);

        if (cursor.moveToFirst()){

            do {
                requisito = new Requisito();
                requisito.setIdRequisito(cursor.getInt(0));
                requisito.setDescripcion(cursor.getString(1));
                requisitos.add(requisito);
            }while (cursor.moveToNext());

        }
        cursor.close();
        return requisitos;
    }


}
