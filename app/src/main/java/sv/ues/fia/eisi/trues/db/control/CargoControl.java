package sv.ues.fia.eisi.trues.db.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.VolleySingleton;
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
            mensaje = context.getText(R.string.error_cargo).toString();
        }else {
            db.insert("cargo", null, values);
            Cursor cursor2 = db.rawQuery("SELECT MAX(idCargo) FROM cargo ",null);
            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();
            mensaje = context.getText(R.string.cargo_creado).toString();
        }
        cursor.close();
        db.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        return ultimoId;
    }
    public Integer CrearCargoDwld(int idCargo, String nombreCargo){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;
        String[] arg = {String.valueOf(idCargo),nombreCargo};
        Cursor cursor = db.query("cargo",null,"idCargo=? and nombreCargo = ?",arg,null,null,null);
        ContentValues values =  new ContentValues();
        values.put("nombreCargo", nombreCargo);
        if (cursor.moveToFirst()){
            mensaje = "Error al crear el Cargo";
            ActualizarCargo(idCargo,nombreCargo);
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
        //Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        return ultimoId;
    }

    public void CrearCargoWS(final Integer idCargo, final String nombreCargo){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/cargo.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");
                    if (status.equals("ok")) {
                        //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Se subió correctamente a MySQL",Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        //Toast.makeText(context, "Registro ya existe", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Hay un problema con la base de datos."
                            //+ response.toString(), Toast.LENGTH_SHORT).show();
                            , Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo."
                        //+ error.toString(), Toast.LENGTH_SHORT).show();
                        , Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion", "create");
                params.put("idCargo",String.valueOf(idCargo));
                params.put("nombreCargo",nombreCargo);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
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
