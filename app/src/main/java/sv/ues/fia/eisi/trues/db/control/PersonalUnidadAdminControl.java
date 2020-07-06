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

    public void crearPersonalUnidadDwnl(int idPersonalUnidad,int idPersonal, int idUnidad){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idPersonal), String.valueOf(idUnidad)};

        Cursor cursor = db.query("personalUnidadAdmin", null,"idPersonal = ? AND idUnidad = ?",args,null,null,null);
        ContentValues values = new ContentValues();

        if (cursor.moveToFirst()){
            mensaje = "El personal ya estaba registrado en esta unidad";
        } else {
            values.put("idPersonalAdmin",idPersonalUnidad);
            values.put("idPersonal",idPersonal);
            values.put("idUnidad",idUnidad);
            db.insert("personalUnidadAdmin",null,values);
            mensaje = "Se ha registrado los datos";
        }

        db.close();
        //Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
    public void crearPersonalUnidadWS(final int idPersonal, final int idUnidad){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/personalUnidadAdmin.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        //Toast.makeText(context,"Relacion creada correctamente en MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Relacion creada correctamente en MySQL",Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "Registro ya existe"
                                , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Hay un problema con la base de datos."
                            ,Toast.LENGTH_SHORT).show();
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
                params.put("operacion", "create");
                params.put("idPersonal",String.valueOf(idPersonal));
                params.put("idUnidad",String.valueOf(idUnidad));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
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

    public void eliminarPersonalUnidadWS(final int idPersonal, final int idUnidad){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/personalUnidadAdmin.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        //Toast.makeText(context,"Relacion eliminada correctamente de MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Relacion eliminada correctamente de MySQL",Toast.LENGTH_SHORT).show();
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
                params.put("idUnidad",String.valueOf(idUnidad));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }
}
