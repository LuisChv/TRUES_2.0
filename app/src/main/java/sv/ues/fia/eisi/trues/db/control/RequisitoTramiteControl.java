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

import java.util.HashMap;
import java.util.Map;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.VolleySingleton;
import sv.ues.fia.eisi.trues.db.entity.Requisito;
import sv.ues.fia.eisi.trues.db.entity.RequisitoTramite;
public class RequisitoTramiteControl {
    private final Context context;
    private DatabaseHelper databaseHelper;
    private RequisitoControl requisitoControl;


    public RequisitoTramiteControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    //CREAR

    public void crearRequisitoTramite(int idTramite, int idRequisito)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String [] args = {String.valueOf(idTramite),String.valueOf(idRequisito)};
        Cursor cursor= db.query("requisitoTramite",null, "idTramite = ? AND idRequisito = ?" ,args,null,null,null);
        ContentValues values =  new ContentValues();
        values.put("idTramite", idTramite);
        values.put("idRequisito", idRequisito);

        if(cursor.moveToFirst())
        {
            mensaje = context.getText(R.string.error_duplicado).toString();
        }
        else
        {
            db.insert("requisitoTramite", null, values);
            mensaje=context.getText(R.string.cambios_guardados).toString();
        }

        db.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
    public void crearRequisitoTramiteDwnl(int idRequisitoTramite,int idTramite, int idRequisito)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String [] args = {String.valueOf(idTramite),String.valueOf(idRequisito)};
        Cursor cursor= db.query("requisitoTramite",null, "idTramite = ? AND idRequisito = ?" ,args,null,null,null);
        ContentValues values =  new ContentValues();
        values.put("idRequisitoTramite", idRequisitoTramite);
        values.put("idTramite", idTramite);
        values.put("idRequisito", idRequisito);

        if(cursor.moveToFirst())
        {
            mensaje = "Error: el registro ya existe";
        }
        else
        {
            db.insert("requisitoTramite", null, values);
            mensaje="Registro guardado";
        }

        db.close();
        //Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void crearRequisitoTramiteWS(final int idTramite, final int idRequisito){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/requisitoTramite.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        //Toast.makeText(context,"Relacion guardada en MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Relacion guardada en MySQL",Toast.LENGTH_SHORT).show();
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
                                "Estamos trabajando para corregirlo."
                        ,Toast.LENGTH_SHORT).show();
                //+ error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion", "create");
                params.put("idTramite",String.valueOf(idTramite));
                params.put("idRequisito",String.valueOf(idRequisito));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    //ACTUALIZAR
    public String actualizarRequisito(int idRequisitoTramite, int idTramite, int idRequisito)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idRequisitoTramite)};

        Cursor cursor= db.query("requisitoTramite", null, "idRequisitoTramite = ?", args, null,null, null);
        ContentValues values =  new ContentValues();
        values.put("idRequisitoTramite", idRequisitoTramite);
        values.put("idTramite", idTramite);
        values.put("idRequisito", idRequisito);

        if (cursor.moveToFirst()){
            db.update("requisito",values, "idRequisito = ?",args);
            mensaje = "Registro actualizado";
        }
        else {
            mensaje = "Error, el registro no existe";
        }

        db.close();
        return mensaje;
    }

    //ELIMINAR

    public void eliminarRequisitoTramite(int idRequisito, int idTramite){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String args[] = {String.valueOf(idRequisito), String.valueOf(idTramite)};
        db.delete("requisitoTramite","idRequisito = ? AND idTramite = ?", args);
        db.close();
        Toast.makeText(context.getApplicationContext(), context.getText(R.string.cambios_guardados).toString(), Toast.LENGTH_SHORT).show();
    }

    public void eliminarRequisitoTramiteWS(final int idRequisito, final int idTramite){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/requisitoTramite.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        //Toast.makeText(context,"Relacion eliminada de MySQL "+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Relacion eliminada de MySQL ",Toast.LENGTH_SHORT).show();
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
                params.put("operacion", "delete");
                params.put("idTramite",String.valueOf(idTramite));
                params.put("idRequisito",String.valueOf(idRequisito));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }


    //CONSULTAR (todos los requisitos de un tramite)

    public Requisito[] consultarRequisitosTramites(int idTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String args[] ={String.valueOf(idTramite)};
        String[] columnas = {"idRequisitoTramite","idRequisito","idTramite"};
        Requisito[] requisitos = null;
        Requisito requisito = null;
        Cursor cursor = db.query("requisitoTramite",columnas,"idTramite = ?",args,null,null,null);

        if (cursor.moveToFirst()){
            int i =0;
            do {
                requisito= requisitoControl.consultarRequisito(cursor.getInt(2));
                requisitos[i] = requisito;
                i++;
            }while (cursor.moveToNext());
        }
        return requisitos;
    }


}
