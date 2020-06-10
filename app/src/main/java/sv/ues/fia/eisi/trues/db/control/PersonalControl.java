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

public class PersonalControl {
    private final Context context;
    private DatabaseHelper databaseHelper;

    public PersonalControl(Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context,"TRUES",null,1);
    }

    //Create, Update
    public Integer crearPersonal(String nombrePersonal){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;
        String[] args = {nombrePersonal};

        Cursor cursor = db.query("personal", null,"nombrePersonal = ?", args,null,null,null);
        ContentValues values = new ContentValues();

        if (cursor.moveToFirst()){
            mensaje = context.getText(R.string.personal_error).toString();
        } else {
            values.put("nombrePersonal",nombrePersonal);

            db.insert("personal",null, values);

            Cursor cursor2 = db.rawQuery("SELECT MAX(idPersonal) FROM personal ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();

            mensaje = context.getText(R.string.personal_creado).toString();;
        }

        db.close();

        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        return ultimoId;
    }

    //Read
    public Personal consultarPersonal(int idPersonal){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idPersonal","nombrePersonal"};
        String args[] = {String.valueOf(idPersonal)};
        Personal personal = null;
        Cursor cursor = db.query("personal",columnas,"idPersonal = ?",args,null,null,null);
        if(cursor.moveToFirst()){
            personal = new Personal();
            personal.setIdPersonal(cursor.getInt(0));
            personal.setNombrePersonal(cursor.getString(1));
        }
        return personal;
    }

    public List<Personal> consultarTodoPersonal(int idFacultad){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idPersonal","nombrePersonal"};
        String[] args = {String.valueOf(idFacultad)};
        List<Personal> arrayPersonal = new ArrayList<>();
        Personal personal = null;
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " +
                        "personal p JOIN personalUnidadAdmin pua JOIN unidadAdmin uA " +
                        "WHERE (p.idPersonal = pua.idPersonal AND pua.idUnidad = uA.idUnidad AND ua.idFacultad = ?) ORDER BY p.nombrePersonal", args);

        while (cursor.moveToNext()){
            personal = new Personal();
            personal.setIdPersonal(cursor.getInt(0));
            personal.setNombrePersonal(cursor.getString(1));
            arrayPersonal.add(personal);
        }
        return arrayPersonal;
    }

    //Udate
    public void actualizarPersonal(int idPersonal, String nombrePersonal){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idPersonal)};
        Cursor cursor = db.query("personal",null,"idPersonal = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("idPersonal",idPersonal);
        values.put("nombrePersonal",nombrePersonal);
        if(cursor.moveToFirst()){
            db.update("personal",values,"idPersonal = ?", args);
            mensaje = context.getText(R.string.cambios_guardados).toString();
        } else {
            mensaje = context.getText(R.string.error).toString();
        }
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }


    public void eliminarPersonal(int idPersonal){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] args = {String.valueOf(idPersonal)};
        Cursor cursor = db.rawQuery("SELECT * FROM paso WHERE idPersonal = ?", args);

        if (cursor.moveToFirst()){
            Toast.makeText(
                    context.getApplicationContext(),
                    context.getText(R.string.error_eliminar).toString(),
                    Toast.LENGTH_SHORT).show();
        } else {
            db.delete("personal", "idPersonal = ?", args);
            db.delete("personalCargo", "idPersonal = ?", args);
            db.delete("personalUnidadAdmin", "idPersonal = ?", args);
            db.delete("personalUbicacion", "idPersonal = ?", args);

            Toast.makeText(
                    context.getApplicationContext(),
                    context.getText(R.string.personal_eliminado).toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
