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
import sv.ues.fia.eisi.trues.db.entity.Personal;
import sv.ues.fia.eisi.trues.db.entity.PersonalCargo;

public class PersonalCargoControl {
    private final Context context;
    private DatabaseHelper databaseHelper;
    private PersonalControl personalControl;

    public PersonalCargoControl(Context context) {
        this.context = context;
        personalControl = new PersonalControl(context);
        this.databaseHelper = new DatabaseHelper(context, "TRUES",null,1);
    }

    //Create
    public void crearPersonalCargo(int idPersonal, int idCargo){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idPersonal),String.valueOf(idCargo)};
        String columns[] = {"idPersonal","idCargo"};
        Cursor cursor = db.query("personalCargo",columns,"idPersonal = ? AND idCargo = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        if (cursor.moveToFirst()){
            mensaje = context.getText(R.string.error_personal_cargo).toString();
        } else {
            values.put("idPersonal",idPersonal);
            values.put("idCargo",idCargo);
            db.insert("personalCargo",null,values);
            mensaje = context.getText(R.string.cambios_guardados).toString();
        }

        db.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void crearPersonalCargoDwnl(int idPersonalCargo,int idPersonal, int idCargo){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idPersonalCargo),String.valueOf(idPersonal),String.valueOf(idCargo)};
        String columns[] = {"idPersonalCargo","idPersonal","idCargo"};
        Cursor cursor = db.query("personalCargo",columns,"idPersonalCargo=? and idPersonal = ? AND idCargo = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        if (cursor.moveToFirst()){
            mensaje = "La persona ya tenia asignado este cargo";
        } else {
            values.put("idPersonal",idPersonal);
            values.put("idCargo",idCargo);
            db.insert("personalCargo",null,values);
            mensaje = "Se ha registrado el cargo del Empleado";
        }

        db.close();
        //Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void crearPersonalCargoWS(final int idPersonal, final int idCargo){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/personalCargo.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        Toast.makeText(context,"Relacion Gardada correctamente en MySQL",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context,"Relacion Gardada correctamente en MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "Registro ya existe"
                                , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Hay un problema con la base de datos."
                            , Toast.LENGTH_SHORT).show();
                    //+ response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                        , Toast.LENGTH_SHORT).show();
                //+ error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion", "create");
                params.put("idPersonal",String.valueOf(idPersonal));
                params.put("idCargo",String.valueOf(idCargo));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    //Read
    public List<Personal> consultarPersonalDelCargo(int idCargo){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String args[] = {String.valueOf(idCargo)};
        String[] columnas = {"idPersonalCargo","idCargo","idPersonal"};
        List<Personal> arrayPersonal = new ArrayList<>();
        Personal personal = null;
        Cursor cursor = db.rawQuery("SELECT p.idPersonal, p.nombrePersonal FROM personalCargo AS pc INNER JOIN personal AS p ON pc.idPersonal=p.idPersonal WHERE pc.idCargo=?",args);

        while (cursor.moveToNext()) {
            personal=new Personal();
            personal.setIdPersonal(cursor.getInt(0));
            personal.setNombrePersonal(cursor.getString(1));

            arrayPersonal.add(personal);
        }
        return arrayPersonal;
    }

    //Update
    public String actualizarPersonalCargo(int idPersonalCargo, int idPersonal, int idCargo){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idPersonalCargo)};
        Cursor cursor = db.query("personalCargo",null,"idPersonalCargo = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("idPersonal",idPersonal);
        values.put("idCargo",idCargo);
        if (cursor.moveToFirst()){
            db.update("personalCargo",values,"idPersonalCargo = ?",args);
            mensaje = "Se ha actualizado con exito el Personal del Cargo";
        } else {
            mensaje = "No se ha encontrado ningun registro";
        }
        return mensaje;
    }

    //Delete
    public void eliminarPersonalCargo(int idPersonal, int idCargo){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String args[] = {String.valueOf(idPersonal), String.valueOf(idCargo)};
        db.delete("personalCargo","idPersonal = ? AND idCargo = ?", args);
        db.close();
        Toast.makeText(context.getApplicationContext(), context.getText(R.string.cambios_guardados).toString(), Toast.LENGTH_SHORT).show();
    }

    public void eliminarPersonalCargoWS(final int idPersonal, final int idCargo){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/personalCargo.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        Toast.makeText(context,"Relacion correctamente eliminada de MySQL",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context,"Relacion correctamente eliminada de MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "Registro NO existe"
                                , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Hay un problema con la base de datos."
                            , Toast.LENGTH_SHORT).show();
                    //+ response.toString(), Toast.LENGTH_SHORT).show();
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
                        , Toast.LENGTH_SHORT).show();
                //+ error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion", "delete");
                params.put("idPersonal",String.valueOf(idPersonal));
                params.put("idCargo",String.valueOf(idCargo));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public List<Cargo> consultarCargo(int idPersonal){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] args = {String.valueOf(idPersonal)};
        List<Cargo> cargos = new ArrayList<>();
        Cargo cargo;
        Cursor cursor = db.rawQuery("SELECT * FROM cargo c JOIN personalCargo pc WHERE (c.idCargo = pc.idCargo AND pc.idPersonal = ?)", args);

        while (cursor.moveToNext()){
            cargo = new Cargo();
            cargo.setIdCargo(cursor.getInt(0));
            cargo.setNombreCargo(cursor.getString(1));
            cargos.add(cargo);
        }
        cursor.close();

        return cargos;
    }

}
