package sv.ues.fia.eisi.trues.db.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.VolleySingleton;
import sv.ues.fia.eisi.trues.db.entity.Actividad;

import static android.content.Context.MODE_PRIVATE;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class ActividadControl {

    private final Context context;
    private DatabaseHelper databaseHelper;


    public ActividadControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    public Integer CrearActividad(int idFacultad, String nombreActividad, String fechaInicio, String fechaFinal)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] idFacNom = {String.valueOf(idFacultad),nombreActividad};
        Integer ultimoId = null;

        Cursor cursor= db.query("actividad", null, "idFacultad = ? and nombreActividad = ?", idFacNom, null,null, null);

        ContentValues values =  new ContentValues();
        values.put("idFacultad", idFacultad);
        values.put("nombreActividad", nombreActividad);
        values.put("inicio", fechaInicio);
        values.put("final", fechaFinal);

        if (cursor.moveToFirst()){
            Toast.makeText(context.getApplicationContext(), context.getText(R.string.error_actividad).toString(), Toast.LENGTH_SHORT).show();
        }
        else {
            db.insert("actividad", null, values);

            Cursor cursor2 = db.rawQuery("SELECT MAX(idActividad) FROM actividad ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();

        }
        cursor.close();
        db.close();

        return  ultimoId;
    }

    public void CrearActividadID(int idActividad, int idFacultad, String nombreActividad, String fechaInicio, String fechaFinal)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values =  new ContentValues();
        values.put("idActividad", idActividad);
        values.put("idFacultad", idFacultad);
        values.put("nombreActividad", nombreActividad);
        values.put("inicio", fechaInicio);
        values.put("final", fechaFinal);

        db.insert("actividad", null, values);
        db.close();

        Toast.makeText(context.getApplicationContext(), context.getText(R.string.actividad_creada).toString(), Toast.LENGTH_SHORT).show();
    }

    public Integer CrearActividadDwld(int idActividad,int idFacultad, String nombreActividad, String fechaInicio, String fechaFinal)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] idFacNom = {String.valueOf(idActividad)};
        Integer ultimoId = null;

        Cursor cursor= db.query("actividad", null, "idActividad=?", idFacNom, null,null, null);

        ContentValues values =  new ContentValues();
        values.put("idActividad",idActividad);
        values.put("idFacultad", idFacultad);
        values.put("nombreActividad", nombreActividad);
        values.put("inicio", fechaInicio);
        values.put("final", fechaFinal);

        if (cursor.moveToFirst()){
            //Toast.makeText(context.getApplicationContext(), "Error, esta actividad ya existe.", Toast.LENGTH_SHORT).show();
            ActualizarActividad(idActividad,nombreActividad,fechaInicio,fechaFinal);
        }
        else {
            db.insert("actividad", null, values);

            Cursor cursor2 = db.rawQuery("SELECT MAX(idActividad) FROM actividad ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();

        }
        cursor.close();
        db.close();

        return  ultimoId;
    }

    public Integer CrearActividadWS(final Integer idActividad, final int idFacultad, final String nombreActividad, final String fechaInicio, final String fechaFinal){
        Integer ultimoId = null;
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/actividad.php";
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
                    Toast.makeText(context, "Hay un problema con la base de datos remota."
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
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion", "insert");
                params.put("idActividad",String.valueOf(idActividad));
                params.put("idFacultad",String.valueOf(idFacultad));
                params.put("nombreActividad", nombreActividad);
                params.put("inicio", fechaInicio);
                params.put("final", fechaFinal);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
        return ultimoId;
    }

    public void EliminarActividad(int idActividad){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idActividad)};
        String[] columnasActividad = {"idActividad"};
        Cursor cursor = db.query("actividad",null,"idActividad = ?",args,null,null,null);
        Cursor cursor1 = db.query("actividadTramite", columnasActividad,"idActividad = ?",args,null,null,null);
        if (cursor.moveToFirst()){
            if (cursor1.moveToFirst()){
                db.delete("actividadTramite","idActividad = ?",args);
                db.delete("actividad","idActividad = ?",args);
            }else {
                db.delete("actividad","idActividad = ?",args);
            }
        }
        cursor.close();
        cursor1.close();
        db.close();

    }

    public void EliminarActividadWS(final Integer idActividad){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/actividad.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");
                    if (status.equals("ok")) {
                        //Toast.makeText(context,"Actualizado"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Se eliminó correctamente de MySQL",Toast.LENGTH_SHORT).show();
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
                params.put("operacion", "delete");
                params.put("idActividad",String.valueOf(idActividad));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public Actividad ObtenerActividad(int idActividad){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] id = {String .valueOf(idActividad)};
        String[] columnas = {"idActividad","idFacultad","nombreActividad", "inicio","final" };

        Cursor cursor = db.query("actividad",columnas,"idActividad = ?",id,null,null,null);
        if(cursor.moveToFirst()){
            Actividad actividad = new Actividad();
            actividad.setIdActividad(cursor.getInt(0));
            actividad.setIdFacultad(cursor.getInt(1));
            actividad.setNombreActividad(cursor.getString(2));
            actividad.setInicio(cursor.getString(3));
            actividad.setFin(cursor.getString(4));
            db.close();
            cursor.close();
            return actividad;
        }else {
            cursor.close();
            return null;
        }
    }

    public List<Actividad> ObtenerActividades(Integer idFacultad){
        String[] args = {String.valueOf(idFacultad)};
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Actividad> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM actividad WHERE idFacultad = ?",args);
        if (cursor.moveToFirst()){
            do {
                Actividad actividad = new Actividad();
                actividad.setIdActividad(cursor.getInt(0));
                actividad.setIdFacultad(cursor.getInt(1));
                actividad.setNombreActividad(cursor.getString(2));
                actividad.setInicio(cursor.getString(3));
                actividad.setFin(cursor.getString(4));
                list.add(actividad);
            }while (cursor.moveToNext());

            cursor.close();
        }

        return list;
    }

    public void ActualizarActividad(Integer idActividad, String nombreActividad, String fechaInicio, String fechaFinal){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idActividad)};
        Cursor cursor = db.query("actividad",null,"idActividad = ?", args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("nombreActividad",nombreActividad);
        values.put("inicio",fechaInicio);
        values.put("final",fechaFinal);

        if (cursor.moveToFirst()){
            db.update("actividad", values,"idActividad = ?", args);
            mensaje = context.getText(R.string.actividad_actualizada).toString();
        }else {
            mensaje = context.getText(R.string.error_actualizar).toString();
        }
        db.close();
        cursor.close();

        //Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void ActualizarActividadWS(final Integer idActividad, final String nombreActividad, final String fechaInicio, final String fechaFinal){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/actividad.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");
                    if (status.equals("ok")) {
                        //Toast.makeText(context,"Actualizado"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Se actualizó correctamente en MySQL",Toast.LENGTH_SHORT).show();
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
                params.put("operacion", "update");
                params.put("idActividad",String.valueOf(idActividad));
                params.put("nombreActividad", nombreActividad);
                params.put("inicio", fechaInicio);
                params.put("final", fechaFinal);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }
}
