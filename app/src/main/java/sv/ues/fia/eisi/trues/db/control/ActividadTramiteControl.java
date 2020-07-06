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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.VolleySingleton;
import sv.ues.fia.eisi.trues.db.entity.ActividadTramite;

public class ActividadTramiteControl {
    private final Context context;
    private DatabaseHelper databaseHelper;

    public ActividadTramiteControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    public void CrearActividadTramite(int idActividad,int idTramite){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idActividad),String.valueOf(idTramite)};
        Cursor cursor = db.query("actividadTramite",null,"idActividad = ? AND idTramite = ?",args,null,null,null);


        if (!cursor.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put("idTramite",idTramite);
            values.put("idActividad",idActividad);

            db.insert("actividadTramite",null,values);
            mensaje = context.getText(R.string.tramite_agregado).toString();
        }else {
            mensaje = context.getText(R.string.tramite_error).toString();
        }
        cursor.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void CrearActividadTramiteDwld(int idActividadTramite,int idActividad,int idTramite){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idActividad),String.valueOf(idTramite)};
        Cursor cursor = db.query("actividadTramite",null,"idActividad = ? AND idTramite=?",args,null,null,null);


        if (!cursor.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put("idActividadTramite",idActividadTramite);
            values.put("idTramite",idTramite);
            values.put("idActividad",idActividad);

            db.insert("actividadTramite",null,values);
            mensaje = "Se ha agregado el tramite " + idTramite + " a la actividad " + idActividad;
            //Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        }else {
            mensaje = "Este trámite ya fue agregado.";
            //Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        }
        cursor.close();

    }

    public void CrearActividadTramiteWS(final int idActividad, final int idTramite){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/actividadTramite.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");
                    if (status.equals("ok")) {
                        //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Relacion guardada en MySQL",Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "Registro ya existe", Toast.LENGTH_SHORT).show();
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
                params.put("operacion", "create");
                params.put("idActividad",String.valueOf(idActividad));
                params.put("idTramite",String.valueOf(idTramite));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }


    public void EliminarActividadTramite(int idActividad, int idTramite){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] args = {String.valueOf(idActividad), String.valueOf(idTramite)};

        db.delete("ActividadTramite","idActividad = ? AND idTramite = ?",args);
        db.close();

        Toast.makeText(context.getApplicationContext(),
                context.getText(R.string.tramite_suprimido).toString(),
                Toast.LENGTH_SHORT).show();
    }

    public void EliminarActividadTramiteWS(final int idActividad, final int idTramite){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/actividadTramite.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");
                    if (status.equals("ok")) {
                        //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Eliminado correctament de MySQL",Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "Registro ya existe", Toast.LENGTH_SHORT).show();
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
                params.put("operacion", "delete");
                params.put("idActividad",String.valueOf(idActividad));
                params.put("idTramite",String.valueOf(idTramite));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public List<ActividadTramite> ObtenerActividadesTramites(Integer idActividad){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<ActividadTramite> list = new ArrayList<>();
        String[] args = {String.valueOf(idActividad)};
        Cursor cursor = db.rawQuery("SELECT * FROM actividadTramite WHERE idActividad = ?",args);
        ActividadTramite actividadTramite;

        while (cursor.moveToNext()) {
            actividadTramite = new ActividadTramite();
            actividadTramite.setIdActividadTramite(cursor.getInt(0));
            actividadTramite.setIdTramite(cursor.getInt(1));
            actividadTramite.setIdActividad(cursor.getInt(2));
            list.add(actividadTramite);

            db.close();
        }
        cursor.close();
        return list;
    }
}
