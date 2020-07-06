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
import sv.ues.fia.eisi.trues.db.entity.Requisito;

public class RequisitoControl {
    private final Context context;
    private DatabaseHelper databaseHelper;


    public RequisitoControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    //CREAR

    public Integer crearRequisito(String descripcion)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;
        String [] args = {descripcion};
        Cursor cursor = db.query("requisito",null,"descripcion = ?",args,null,null,null);
        ContentValues values =  new ContentValues();
        values.put("descripcion", descripcion);
        if (cursor.moveToFirst()){
            mensaje = context.getText(R.string.error_requisito).toString();
        }else {
            db.insert("requisito", null, values);
            mensaje = context.getText(R.string.requisito_creado).toString();

            Cursor cursor2 = db.rawQuery("SELECT MAX(idRequisito) FROM requisito ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();
        }

        cursor.close();
        db.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();

        return  ultimoId;
    }
    public void crearRequisitoDwnl(int idRequisito,String descripcion)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;
        String [] args = {String.valueOf(idRequisito)};
        Cursor cursor = db.query("requisito",null,"idRequisito = ?",args,null,null,null);
        ContentValues values =  new ContentValues();
        values.put("idRequisito", idRequisito);
        values.put("descripcion", descripcion);
        if (cursor.moveToFirst()){
            mensaje = "Error: el requisito ya existe";
            actualizarRequisito(idRequisito,descripcion);
        }else {
            db.insert("requisito", null, values);
            mensaje = "Requisito guardado con exito";

            Cursor cursor2 = db.rawQuery("SELECT MAX(idRequisito) FROM requisito ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();
        }

        cursor.close();
        db.close();
        //Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void crearRequisitoWS(final Integer idRequisito, final String descripcion){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/requisito.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        //Toast.makeText(context,"Requisido subido a MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Requisido subido a MySQL",Toast.LENGTH_SHORT).show();
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
                params.put("idRequisito",String.valueOf(idRequisito));
                params.put("descripcion",descripcion);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }


    //ACTUALIZAR
    public String actualizarRequisito(int idRequisito, String descripcion)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idRequisito)};
        Cursor cursor= db.query("requisito", null, "idRequisito = ?", args, null,null, null);
        ContentValues values =  new ContentValues();
        values.put("idRequisito",idRequisito);
        values.put("descripcion",descripcion);

        if (cursor.moveToFirst()){
            db.update("requisito",values, "idRequisito = ?",args);
            mensaje = "Registro actualizado";
        }
        else {
            mensaje = "Error, el registro no existe";
        }
        cursor.close();
        db.close();
        return mensaje;
    }
    public void actualizarRequisitoWS(final int idRequisito, final String descripcion){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/requisito.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        //Toast.makeText(context,"Requisito Actualizado en MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Requisito Actualizado en MySQL",Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "Registro no existe"
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
                params.put("operacion", "update");
                params.put("idRequisito",String.valueOf(idRequisito));
                params.put("descripcion",descripcion);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    //ELIMINAR

    public void eliminarRequisito(int idRequisito){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String args[] = {String.valueOf(idRequisito)};
        db.delete("requisito","idRequisito = ?", args);
        db.delete("requisitoTramite","idRequisito = ?",args);
        db.close();
    }

    public void eliminarRequisitoWS(final int idRequisito){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/requisito.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        Toast.makeText(context,"Requisito eliminado correctamente de MySQL",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context,"Eliminado"+response.toString(),Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "Registro no existe"
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
                params.put("idRequisito",String.valueOf(idRequisito));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }


    //CONSULTAR

    public Requisito consultarRequisito(int idRequisito){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idRequisito","descripcion"};
        String args[] = {String.valueOf(idRequisito)};
        Requisito requisito = null;
        Cursor cursor = db.query("requisito",columnas,"idRequisito = ?",args,null,null,null);
        if(cursor.moveToFirst()){
            requisito = new Requisito();
            requisito.setIdRequisito(cursor.getInt(0));
            requisito.setDescripcion(cursor.getString(1));

        }
        cursor.close();
        return requisito;
    }

    //CONSULTAR VARIOS

    public List<Requisito> consultarRequisitos(Integer idTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idRequisito","descripcion"};
        List<Requisito> requisitos = new ArrayList<>();
        Requisito requisito = null;
        String [] args = {String.valueOf(idTramite)};
        Cursor cursor = db.rawQuery("SELECT * FROM requisito r JOIN requisitoTramite rT  WHERE r.idRequisito = rT.idRequisito AND rt.idTramite = ?", args);

        if (cursor.moveToFirst()){

            do {
                requisito = new Requisito();
                requisito.setIdRequisito(cursor.getInt(0));
                requisito.setDescripcion(cursor.getString(1));
                requisitos.add(requisito);
            }while (cursor.moveToNext());

        }
        cursor.close();
        return requisitos;
    }

    public List<Requisito> consultarRequisitos(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Requisito> requisitos = new ArrayList<>();
        Requisito requisito = null;
        Cursor cursor = db.rawQuery("SELECT * FROM requisito", null);

        if (cursor.moveToFirst()){

            do {
                requisito = new Requisito();
                requisito.setIdRequisito(cursor.getInt(0));
                requisito.setDescripcion(cursor.getString(1));
                requisitos.add(requisito);
            }while (cursor.moveToNext());

        }
        cursor.close();
        return requisitos;
    }


}
