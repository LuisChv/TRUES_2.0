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
import sv.ues.fia.eisi.trues.db.entity.Paso;

public class PasoControl {

    private final Context context;
    private DatabaseHelper databaseHelper;

    public PasoControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }


    public void crearPaso(Integer idUbicacion, Integer idTramite, String descripcionPaso, float porcentaje){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idPersonal",idUbicacion);
        values.put("idTramite", idTramite);
        values.put("descripcionPaso",descripcionPaso);
        values.put("porcentaje", porcentaje);
        db.insert("paso",null,values);
        db.close();

        Toast.makeText(context.getApplicationContext(), context.getText(R.string.cambios_guardados).toString(), Toast.LENGTH_SHORT).show();
    }

    public void crearPasoDwnl(Integer idPaso,Integer idUbicacion, Integer idTramite, String descripcionPaso, float porcentaje){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idPaso",idPaso);
        values.put("idPersonal",idUbicacion);
        values.put("idTramite", idTramite);
        values.put("descripcionPaso",descripcionPaso);
        values.put("porcentaje", porcentaje);
        db.insert("paso",null,values);
        db.close();
        actualizarPaso(idPaso,idUbicacion,descripcionPaso,porcentaje);
        //Toast.makeText(context.getApplicationContext(), "Guardado con éxito.", Toast.LENGTH_SHORT).show();
    }

    public void crearPasoWS(final Integer idPersonal, final Integer idTramite, final String descripcionPaso, final float porcentaje){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/paso.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        Toast.makeText(context,"Subido correctamente a MySQL",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context,"Subido correctamente a MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "El Registro ya existe"
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
                params.put("idPersonal",String.valueOf(idPersonal));
                params.put("idTramite", String.valueOf(idTramite));
                params.put("descripcionPaso", descripcionPaso);
                params.put("porcentaje", String.valueOf(porcentaje));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    //ELIMINAR
    public void eliminarPaso(Integer id){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] values= {String.valueOf(id)};
        Paso paso=new Paso();
        paso=obtenerPaso(id);
        Cursor cursor=db.query("paso",null, "idPaso=?",values, null,null,null);
        Cursor cursor1=db.query("usuarioPaso",null,"idPaso=?",values,null,null,null);
        if(cursor.moveToFirst()){
            if(cursor1.moveToFirst()){
                db.delete("usuarioPaso","idPaso=?", values);
            }
            db.delete("paso", "idPaso = ?", values);
            mensaje= context.getText(R.string.paso_eliminado).toString();
        }
        else{
            mensaje=context.getText(R.string.error).toString();
        }
        cursor.close();
        cursor1.close();
        db.close();

        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void eliminarPasoWS(final Integer id){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/paso.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        Toast.makeText(context,"Eliminado correctamente de MySQL",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context,"Eliminado correctamente de MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "El Registro ya existe"
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
                params.put("idPaso",String.valueOf(id));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    //OBTENER

    public Paso obtenerPaso(Integer id){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idPaso","idPersonal","idTramite", "descripcionPaso", "porcentaje"};
        String[] values={String.valueOf(id)};
        Paso paso = null;
        Cursor cursor = db.query("paso",columnas, "idPaso = ?",values, null, null, null, null);
        if (cursor.moveToFirst()){
            paso = new Paso();
            paso.setIdPaso(cursor.getInt(0));
            paso.setIdPersonal(cursor.getInt(1));
            paso.setIdTramite(cursor.getInt(2));
            paso.setDescripcion(cursor.getString(3));
            paso.setPorcentaje(cursor.getFloat(4));
        }
        cursor.close();
        return paso;
    }

    public List<Paso> obtenerPasos(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Paso> pasoList = new ArrayList<>();
        String[] columnas = {"idPaso","idPersonal","idTramite", "descripcionPaso", "porcentaje"};
        Paso paso=null;
        Cursor cursor = db.rawQuery("SELECT * FROM paso",null);
        if (cursor.moveToFirst()){
            do{
                paso = new Paso();
                paso.setIdPaso(cursor.getInt(0));
                paso.setIdPersonal(cursor.getInt(1));
                paso.setIdTramite(cursor.getInt(2));
                paso.setDescripcion(cursor.getString(3));
                paso.setPorcentaje(cursor.getFloat(4));
                pasoList.add(paso);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return pasoList;
    }
    /*idPaso INTEGER NOT NULL PRIMARY KEY,
        idUbicacion INTEGER NOT NULL,
        idTramite INTEGER NOT NULL,
        descripcionPaso VARCHAR(256) NOT NULL,
        porcentaje REAL(10) NOT NULL*/

    public List<Paso> obtenerPasos(Integer idTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Paso> pasoList = new ArrayList<>();
        String[]args={String.valueOf(idTramite)};
        String[] columnas = {"idPaso","idPersonal","idTramite","descripcionPaso","porcentaje"};
        Paso paso=null;
        Cursor cursor = db.rawQuery("SELECT * FROM paso WHERE idTramite=?",args);
        while (cursor.moveToNext()){
                paso = new Paso();
                paso.setIdPaso(cursor.getInt(0));
                paso.setIdPersonal(cursor.getInt(1));
                paso.setIdTramite(cursor.getInt(2));
                paso.setDescripcion(cursor.getString(3));
                paso.setPorcentaje(cursor.getFloat(4));
                pasoList.add(paso);
        }
        cursor.close();
        return pasoList;
    }

    //UPDATE
    public void actualizarPaso(Integer id, Integer idPersonal, String descripcionPaso, float porcentaje){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(id)};
        Cursor cursor = db.query("paso",null,"idPaso = ?", args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("idPersonal",idPersonal);
        values.put("descripcionPaso",descripcionPaso);
        values.put("porcentaje", porcentaje);

        if (cursor.moveToFirst()){
            db.update("paso", values,"idPaso = ?", args);
            mensaje = context.getText(R.string.cambios_guardados).toString();
        }
        else {
            mensaje = context.getText(R.string.error).toString();
        }

        cursor.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void actualizarPasoWS(final Integer id, final Integer idPersonal, final String descripcionPaso, final float porcentaje){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/paso.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        Toast.makeText(context,"Actualizado correctamente en MySQL",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context,"Actualizado correctamente en MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "El Registro no existe"
                                , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Hay un problema con la base de datos:\n"
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
                        , Toast.LENGTH_SHORT).show();
                //+ error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion", "update");
                params.put("idPaso",String.valueOf(id));
                params.put("idPersonal",String.valueOf(idPersonal));
                params.put("descripcionPaso",descripcionPaso);
                params.put("porcentaje",String.valueOf(porcentaje));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

}
