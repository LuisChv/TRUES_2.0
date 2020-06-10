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
import sv.ues.fia.eisi.trues.db.entity.Personal;
import sv.ues.fia.eisi.trues.db.entity.PersonalUnidadAdmin;
import sv.ues.fia.eisi.trues.db.entity.UnidadAdmin;

public class PersonalUnidadAdminControl {
    private final Context context;
    private DatabaseHelper databaseHelper;
    private PersonalControl personalControl;

    public PersonalUnidadAdminControl(Context context) {
        this.context = context;
        personalControl = new PersonalControl(context);
        this.databaseHelper = new DatabaseHelper(context,"TRUES",null,1);
    }

    //Create
    public void crearPersonalUnidad(int idPersonal, int idUnidad){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idPersonal), String.valueOf(idUnidad)};

        Cursor cursor = db.query("personalUnidadAdmin", null,"idPersonal = ? AND idUnidad = ?",args,null,null,null);
        ContentValues values = new ContentValues();

        if (cursor.moveToFirst()){
            mensaje = context.getText(R.string.error_personal).toString();
        } else {
            values.put("idPersonal",idPersonal);
            values.put("idUnidad",idUnidad);
            db.insert("personalUnidadAdmin",null,values);
            mensaje = context.getText(R.string.cambios_guardados).toString();
        }

        db.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    //No se que recuperar con el Read jsjsjsjs xd
    public List<Personal> consultarPersonalDeUnidad(int idUnidad){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String args[] = {String.valueOf(idUnidad)};
        String[] columnas = {"idPersonalAdmin","idPersonal","idUnidad"};
        List<Personal> arrayPersonal = new ArrayList<>();
        Personal personal = null;
        Cursor cursor = db.query("personalUnidadAdmin",columnas,"idUnidad",args,null,null,null);
        if(cursor.moveToFirst()){
            do{
                personal = personalControl.consultarPersonal(cursor.getInt(1));
                arrayPersonal.add(personal);
            }while (cursor.moveToNext());
        }
        return arrayPersonal;
    }

    public List<UnidadAdmin> consultarUnidades(int idPersonal){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String args[] = {String.valueOf(idPersonal)};
        List<UnidadAdmin> unidadList = new ArrayList<>();
        UnidadAdmin unidad = null;
        Cursor cursor = db.rawQuery("SELECT * FROM unidadAdmin uA JOIN personalUnidadAdmin pUA WHERE uA.idUnidad = pUA.idUnidad AND pUA.idPersonal = ?", args);

        while (cursor.moveToNext()){
            unidad = new UnidadAdmin();
            unidad.setIdUAdmin(cursor.getInt(0));
            unidad.setNombreUAdmin(cursor.getString(2));
            unidadList.add(unidad);
        }
        return unidadList;
    }

    //Update
    public String actualizarPersonalAdmin(int idPersonalAdmin, int idPersonal, int idUnidad){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idPersonalAdmin)};
        Cursor cursor = db.query("personalUnidadAdmin",null,"idPersonalAdmin = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("idPersonalAdmin",idPersonalAdmin);
        values.put("idPersonal",idPersonal);
        values.put("idUnidad",idUnidad);
        if (cursor.moveToFirst()){
            db.update("personalUnidadAdmin",values,"idPersonalAdmin = ?",args);
            mensaje = "Se ha actualizado con exito el personal de la Unidad";
        } else {
            mensaje = "No se ha encontrado ningun registro";
        }
        return mensaje;
    }

    //Delete
    public void eliminarPersonalUnidad(int idPersonal, int idUnidad){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String args[] = {String.valueOf(idPersonal), String.valueOf(idUnidad)};
        db.delete("PersonalUnidadAdmin","idPersonal = ? AND idUnidad = ?", args);
        db.close();
        Toast.makeText(context.getApplicationContext(), context.getText(R.string.cambios_guardados).toString(), Toast.LENGTH_SHORT).show();
    }
}
