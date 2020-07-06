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

    public void CrearAccesoUsuarioDwnl(int idAccesoUsuario,String usuario, String idOption){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        ContentValues values = new ContentValues();
        String [] columnasNull = {"idAccesoUsuario"};
        values.put("idAccesoUsuario",idAccesoUsuario);
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

    public void CrearAccesoUsuario2WS(final String usuario, final String idOption){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/accesoUsuario.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");
                    if (status.equals("ok")) {
                        //Toast.makeText(context,"AccesoUsuario Actualizado"+response.toString(),Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        //Toast.makeText(context, "Registro ya existe", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(context, "Hay un problema con la base de datos:\n" + response.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Hay un problema con la base de datos remota\nContacte al administrdor",
                            Toast.LENGTH_SHORT).show();
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
                        //+ error.toString(), Toast.LENGTH_SHORT).show();
                        , Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion", "asignar");
                params.put("usuario",usuario);
                params.put("idOption", idOption);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void eliminarPermiso(String usuario, String idOption){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {usuario, idOption};

        Cursor cursor = db.query("accesoUsuario", null, "usuario = ? AND idOption = ?", args, null, null, null);

        if (cursor.moveToFirst()){
            db.delete("accesoUsuario", "usuario = ? AND idOption = ?", args);
            mensaje = "Se ha quitado el permiso " + idOption + " al usuario " + usuario;
        }
        else {
            mensaje = "ERROR";
        }

        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void eliminarPermisoWS(final String usuario, final String idOption){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/accesoUsuario.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        Toast.makeText(context,"Eliminado"+response.toString(),Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        //Toast.makeText(context, "Registro ya existe", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Hay un problema con la base de datos remota"
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
                                "Estamos trabajando para corregirlo.\n"
                        //+ error.toString(), Toast.LENGTH_SHORT).show();
                        , Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion", "eliminar");
                params.put("usuario",usuario);
                params.put("idOption", idOption);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
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
