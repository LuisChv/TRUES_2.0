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
import sv.ues.fia.eisi.trues.db.entity.PersonalUbicacion;
import sv.ues.fia.eisi.trues.db.entity.Ubicacion;

public class PersonalUbicacionControl {

    private final Context context;
    private DatabaseHelper databaseHelper;
    private UbicacionControl ubicacionControl;


    public PersonalUbicacionControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    //CREAR

    public void crearPersonalUbicacion(int idPersonal, int idUbicaion)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String [] args = {String.valueOf(idPersonal),String.valueOf(idUbicaion)};
        Cursor cursor= db.query("personalUbicacion",null, "idPersonal = ? AND idUbicacion = ?" ,args,null,null,null);
        ContentValues values =  new ContentValues();
        values.put("idPersonal", idPersonal);
        values.put("idUbicacion", idUbicaion);

        if(cursor.moveToFirst())
        {
            mensaje = context.getText(R.string.error).toString();
        }
        else
        {
            db.insert("personalUbicacion", null, values);
            mensaje=context.getText(R.string.cambios_guardados).toString();
        }
        db.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void crearPersonalUbicacionDwnl(int idPersonalUbicacion,int idPersonal, int idUbicaion)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String [] args = {String.valueOf(idPersonal),String.valueOf(idUbicaion)};
        Cursor cursor= db.query("personalUbicacion",null, "idPersonal = ? AND idUbicacion = ?" ,args,null,null,null);
        ContentValues values =  new ContentValues();
        values.put("idPersonalUbicacion",idPersonalUbicacion);
        values.put("idPersonal", idPersonal);
        values.put("idUbicacion", idUbicaion);

        if(cursor.moveToFirst())
        {
            mensaje = "Error: el registro ya existe";
        }
        else
        {
            db.insert("personalUbicacion", null, values);
            mensaje="Registro guardado";
        }
        db.close();
        //Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void crearPersonalUbicacionWS(final int idPersonal, final int idUbicaion){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/personalUbicacion.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        //Toast.makeText(context,"Relacion sincronizada en MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Relacion sincronizada en MySQL",Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "Registro ya existe"
                                , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Hay un problema con la base de datos:\n"
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
                params.put("operacion", "create");
                params.put("idPersonal",String.valueOf(idPersonal));
                params.put("idUbicacion",String.valueOf(idUbicaion));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    //ACTUALIZAR
    public String actualizarPersonalUbicacion(int idPersonalUbicacion, int idPersonal, int idUbicacion)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idPersonalUbicacion)};
        Cursor cursor= db.query("personalUbicacion", null, "idPersonalUbicaion = ?", args, null,null, null);
        ContentValues values =  new ContentValues();
        values.put("idPersonalUbicacion", idPersonalUbicacion);
        values.put("idPersonal", idPersonal);
        values.put("idUbicacion", idUbicacion);
        if (cursor.moveToFirst()){
            db.update("personalUbicacion",values,"idPersonalUbicacion = ?",args);
            mensaje = "Registro actualizado";
        }
        else {
            mensaje = "Error al actualizar";
        }
        db.close();
        return mensaje;

    }

    //ELIMINAR

    public void eliminarPersonalUbicacion(int idPersonal, int idUbicacion){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String args[] = {String.valueOf(idPersonal), String.valueOf(idUbicacion)};
        db.delete("personalUbicacion","idPersonal = ? AND idUbicacion = ?", args);
        db.close();
        Toast.makeText(context.getApplicationContext(), context.getText(R.string.cambios_guardados).toString(), Toast.LENGTH_SHORT).show();
    }

    public void eliminarPersonalUbicacionWS(final int idPersonal, final int idUbicacion){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/personalUbicacion.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        //Toast.makeText(context,"Relacion eliminada de MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Relacion eliminada de MySQL",Toast.LENGTH_SHORT).show();
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
                params.put("idUbicacion",String.valueOf(idUbicacion));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    //CONSULTAR
    public List<Ubicacion> consultarPersonalUbicaciones(int idPersonal){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List ubicacionList=new ArrayList();
        String args[] ={String.valueOf(idPersonal)};
        String[] columnas = {"idPersonalUbicacion","idUbicacion","idPersonal"};
        Ubicacion[] ubicaciones = null;
        Ubicacion ubicacion = null;
        /*idUbicacion INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
         longitud REAL NOT NULL,
         latitud REAL NOT NULL,
         altitud REAL NOT NULL,
         componenteTematica VARCHAR(128) NOT NULL*/
        Cursor cursor = db.rawQuery("SELECT u.idUbicacion, u.longitud, u.latitud, u.altitud, u.componenteTematica FROM ubicacion AS u INNER JOIN personal AS p ON pc.idPersonal=p.idPersonal WHERE pc.idPersonal=?",args);

        while (cursor.moveToNext())
        {
            ubicacion=new Ubicacion();
            ubicacion.setIdUbicacion(cursor.getInt(0));
            ubicacion.setLongitud(cursor.getFloat(1));
            ubicacion.setLatidud(cursor.getFloat(2));
            ubicacion.setAltitud(cursor.getFloat(3));
            ubicacion.setComponenteTematica(cursor.getString(4));
            ubicacionList.add(ubicacion);

        }
        return ubicacionList;
    }


    }
