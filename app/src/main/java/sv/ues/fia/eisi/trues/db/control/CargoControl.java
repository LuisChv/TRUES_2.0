package sv.ues.fia.eisi.trues.db.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.entity.Cargo;

public class CargoControl {
    private final Context context;
    private DatabaseHelper databaseHelper;

    public CargoControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    public Integer CrearCargo(String nombreCargo){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;
        String[] arg = {nombreCargo};
        Cursor cursor = db.query("cargo",null,"nombreCargo = ?",arg,null,null,null);
        ContentValues values =  new ContentValues();
        values.put("nombreCargo", nombreCargo);
        if (cursor.moveToFirst()){
            mensaje = "Error al crear el Cargo";
        }else {
            db.insert("cargo", null, values);
            Cursor cursor2 = db.rawQuery("SELECT MAX(idCargo) FROM cargo ",null);
            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();
            mensaje = "Se a creado el cargo "+ultimoId+" con éxito.";
        }
        cursor.close();
        db.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        return ultimoId;
    }

    public String ActualizarCargo(int idCargo, String nombreCargo){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idCargo)};
        Cursor cursor = db.query("cargo",null,"idCargo = ?", args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("nombreCargo",nombreCargo);

        if (cursor.moveToFirst()){
            db.update("cargo", values,"idCargo = ?", args);
            mensaje = "Se ha actualizado el cargo "+idCargo+" con exito";
        }
        else {
            mensaje = "Error al actualizar el nuevo cargo";
        }
        db.close();
        cursor.close();
        return mensaje;
    }

    public String EliminarCargo(int idcargo){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        PersonalControl personalControl = new PersonalControl(context);
        String mensaje;
        String[] args = {String.valueOf(idcargo)};
        String[] columnasPersonalCargo = {"idCargo"};
        Cursor cursor = db.query("personalCargo",columnasPersonalCargo,"idCargo = ?", args,null,null,null);
        Cursor cursor1 = db.query("cargo",null,"idCargo = ?",args,null,null,null);

        if(cursor1.moveToNext()){
            if (cursor.moveToFirst()){
                db.delete("personalCargo","idCargo = ?", args);
                db.delete("cargo","idCargo = ?", args);
                mensaje = "Cargo eliminado correctamente el cargo "+idcargo+" Además se eliminó un elemento de la tabla PersonalCargo";
            }
            else {
                db.delete("cargo","idCargo = ?",args);
                mensaje = "Se ha eliminado el cargo " + idcargo + " con éxito.";
            }
        }else {
            mensaje ="Error al eliminar";
        }
        db.close();
        cursor.close();
        cursor1.close();
        return mensaje;
    }

    public Cargo ObtenerCargo(int idCargo){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] id = {String.valueOf(idCargo)};
        String[] campos = {"idCargo","nombreCargo"};
        Cursor cursor = db.query("cargo",campos,"idCursor = ?", id,null,null,null);
        if(cursor.moveToFirst()){
            Cargo cargo = new Cargo();
            cargo.setIdCargo(cursor.getInt(0));
            cargo.setNombreCargo((cursor.getString(1)));
            db.close();
            cursor.close();
            return cargo;
        }else {
            cursor.close();
            return null;
        }
    }

    public List<Cargo> ObtenerCargos(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Cargo> cargoList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM cargo",null);

        while (cursor.moveToNext()){
                Cargo cargo = new Cargo();
                cargo.setIdCargo(cursor.getInt(0));
                cargo.setNombreCargo(cursor.getString(1));
                cargoList.add(cargo);
        }
        cursor.close();
        return cargoList;
    }

    public List<Cargo> ObtenerCargos(int idPersonal){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Cargo> cargoList = new ArrayList<>();
        String[] args = {String.valueOf(idPersonal)};
        Cursor cursor = db.rawQuery(
                "SELECT * FROM cargo c JOIN personalCargo pc " +
                        "WHERE c.idCargo = pc.idCargo AND pc.idPersonal = ?", args);

        while (cursor.moveToNext()){
            Cargo cargo = new Cargo();
            cargo.setIdCargo(cursor.getInt(0));
            cargo.setNombreCargo(cursor.getString(1));
            cargoList.add(cargo);
        }
        cursor.close();
        return cargoList;
    }

}
