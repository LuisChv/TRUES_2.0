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
import sv.ues.fia.eisi.trues.db.entity.PersonalUnidadAdmin;
import sv.ues.fia.eisi.trues.db.entity.UnidadAdmin;

public class UnidadAdminControl {
    private final Context context;
    private DatabaseHelper databaseHelper;

    public UnidadAdminControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }
    /*idUnidad INTEGER NOT NULL PRIMARY KEY,
     idFacultad INTEGER NOT NULL,
     nombreUnidadAdmin VARCHAR(64) NOT NULL*/


    //CREAR
    public Integer crearUAdmin(Integer idFacultad, String nombreUnidadAdmin){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;

        ContentValues values = new ContentValues();
        values.put("idFacultad",idFacultad);
        values.put("nombreUnidadAdmin", nombreUnidadAdmin);

        db.insert("unidadAdmin", null, values);
        mensaje=context.getText(R.string.unidad_creada).toString();

        Cursor cursor2 = db.rawQuery("SELECT MAX(idUnidad) FROM unidadAdmin ",null);
        cursor2.moveToFirst();

        ultimoId = cursor2.getInt(0);
        cursor2.close();

        db.close();

        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
        return ultimoId;
    }


    //OBTENER
    public UnidadAdmin obtenerUAdmin(String id){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] columnas = {"idUnidad","idFacultad", "nombreUAdmin"};
        String[] values={id};
        UnidadAdmin uAdmin = null;
        Cursor cursor = db.query("unidadAdmin",columnas, "idUnidad = ?",values, null, null, null, null);

        if (cursor.moveToFirst()){
            uAdmin = new UnidadAdmin();
            uAdmin.setIdUAdmin(cursor.getInt(0));
            uAdmin.setIdFacultad(cursor.getInt(1));
            uAdmin.setNombreUAdmin(cursor.getString(2));
        }

        return uAdmin;
    }

    public List<UnidadAdmin> obtenerUAdmin(int idFacultad){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<UnidadAdmin> uAdminList = new ArrayList<>();
        String[] args = {String.valueOf(idFacultad)};
        Cursor cursor = db.rawQuery("SELECT * FROM unidadAdmin WHERE idFacultad = ?",args);

        while (cursor.moveToNext()){
            UnidadAdmin uAdmin = new UnidadAdmin();
            uAdmin.setIdUAdmin(cursor.getInt(0));
            uAdmin.setNombreUAdmin(cursor.getString(2));
            uAdminList.add(uAdmin);
        }

        cursor.close();
        return uAdminList;
    }

    public List<UnidadAdmin> consultar(int idPersonal){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<UnidadAdmin> uAdminList = new ArrayList<>();
        String[] args = {String.valueOf(idPersonal)};
        Cursor cursor = db.rawQuery(
                "SELECT * FROM unidadAdmin uA JOIN personalUnidadAdmin pUA  " +
                        "WHERE uA.idUnidad = pUA.idUnidad AND pUA.idPersonal = ?",args);

        while (cursor.moveToNext()){
            UnidadAdmin uAdmin = new UnidadAdmin();
            uAdmin.setIdUAdmin(cursor.getInt(0));
            uAdmin.setNombreUAdmin(cursor.getString(2));
            uAdminList.add(uAdmin);
        }

        cursor.close();
        return uAdminList;
    }

    //UPDATE
    public String actualizarUAdmin(UnidadAdmin uAdmin){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(uAdmin.getIdUAdmin() )};
        Cursor cursor = db.query("unidadAdmin",null,"idUnidad = ?", args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("nombreUAdmin",uAdmin.getNombreUAdmin());

        if (cursor.moveToFirst()){
            db.update("unidadAdmin", values,"idUnidad = ?", args);
            mensaje = "Unidad administrativa: "+uAdmin.getNombreUAdmin()+" actualizado con exito";
        }
        else {
            mensaje = "Unidad administrativa: " + uAdmin.getNombreUAdmin() + "no se encuentra o no existe";
        }

        cursor.close();
        return mensaje;
    }
    //ELIMINAR
    public String EliminarCargo(UnidadAdmin uAdmin){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        PersonalUnidadAdminControl personalControl = new PersonalUnidadAdminControl(context);
        String mensaje;
        String[] args = {String.valueOf(uAdmin.getIdUAdmin())};
        String[] columnasPersonalUnidadAdmin = {"idUnidad"};
        Cursor cursor = db.query("unidadAdmnin",columnasPersonalUnidadAdmin,"idUnidad = ?", args,null,null,null);
        Cursor cursor1 = db.query("personalUnidadAdmin",null,"idPersonalAdmin = ?",args,null,null,null);

        if(cursor.moveToNext()){
            if(cursor1.moveToFirst()){
                db.delete("personalUnidadAdmin","idUnidad=?",args);
                db.delete("unidadAdmin","idUnidad=?",args);
            }
            mensaje="Se ha eliminado la unidad administrativa: "+uAdmin.getNombreUAdmin()+ " con exito";
        }else {
            mensaje ="Error al eliminar";
        }
        cursor.close();
        cursor1.close();
        return mensaje;
    }
}
