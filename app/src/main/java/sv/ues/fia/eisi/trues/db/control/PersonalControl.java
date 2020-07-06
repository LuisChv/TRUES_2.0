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

public class PersonalControl {
    private final Context context;
    private DatabaseHelper databaseHelper;

    public PersonalControl(Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context,"TRUES",null,1);
    }

    public Integer crearPersonalDwld(int idPersonal,String nombrePersonal){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;
        String[] args = {String.valueOf(idPersonal),nombrePersonal};

        Cursor cursor = db.query("personal", null,"idPersonal=? and nombrePersonal = ?", args,null,null,null);
        ContentValues values = new ContentValues();

        if (cursor.moveToFirst()){
            mensaje = "La persona ya existe en la base de datos";
            actualizarPersonal(idPersonal,nombrePersonal);
        } else {
            values.put("nombrePersonal",nombrePersonal);

            db.insert("personal",null, values);

            Cursor cursor2 = db.rawQuery("SELECT MAX(idPersonal) FROM personal ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();

            mensaje = "Se ha registrado un nuevo personal";
        }

        db.close();

        //Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        return ultimoId;
    }

    //Create, Update
    public Integer crearPersonal(String nombrePersonal){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;
        String[] args = {nombrePersonal};

        Cursor cursor = db.query("personal", null,"nombrePersonal = ?", args,null,null,null);
        ContentValues values = new ContentValues();

        if (cursor.moveToFirst()){
            mensaje = context.getText(R.string.personal_error).toString();
        } else {
            values.put("nombrePersonal",nombrePersonal);

            db.insert("personal",null, values);

            Cursor cursor2 = db.rawQuery("SELECT MAX(idPersonal) FROM personal ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();

            mensaje = context.getText(R.string.personal_creado).toString();;
        }

        db.close();

        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        return ultimoId;
    }

    public void crearPersonalWS(final Integer idPersonal, final String nombrePersonal){
        Integer ultimoId = null;
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/personal.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        Toast.makeText(context,"Subido correctamente a MySQL",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context,"Subido correctamente a MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
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
                params.put("nombrePersonal",nombrePersonal);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    //Read
    public Personal consultarPersonal(int idPersonal){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idPersonal","nombrePersonal"};
        String args[] = {String.valueOf(idPersonal)};
        Personal personal = null;
        Cursor cursor = db.query("personal",columnas,"idPersonal = ?",args,null,null,null);
        if(cursor.moveToFirst()){
            personal = new Personal();
            personal.setIdPersonal(cursor.getInt(0));
            personal.setNombrePersonal(cursor.getString(1));
        }
        return personal;
    }

    public List<Personal> consultarTodoPersonal(int idFacultad){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idPersonal","nombrePersonal"};
        String[] args = {String.valueOf(idFacultad)};
        List<Personal> arrayPersonal = new ArrayList<>();
        Personal personal = null;
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " +
                        "personal p JOIN personalUnidadAdmin pua JOIN unidadAdmin uA " +
                        "WHERE (p.idPersonal = pua.idPersonal AND pua.idUnidad = uA.idUnidad AND ua.idFacultad = ?) ORDER BY p.nombrePersonal", args);

        while (cursor.moveToNext()){
            personal = new Personal();
            personal.setIdPersonal(cursor.getInt(0));
            personal.setNombrePersonal(cursor.getString(1));
            arrayPersonal.add(personal);
        }
        return arrayPersonal;
    }

    //Udate
    public void actualizarPersonal(int idPersonal, String nombrePersonal){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idPersonal)};
        Cursor cursor = db.query("personal",null,"idPersonal = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("idPersonal",idPersonal);
        values.put("nombrePersonal",nombrePersonal);
        if(cursor.moveToFirst()){
            db.update("personal",values,"idPersonal = ?", args);
            mensaje = context.getText(R.string.cambios_guardados).toString();
        } else {
            mensaje = context.getText(R.string.error).toString();
        }
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void actualizarPersonalWS(final int idPersonal, final String nombrePersonal){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/personal.php";
        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        //Toast.makeText(context,"Actualizado correctamente en MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Actualizado correctamente en MySQL",Toast.LENGTH_SHORT).show();
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
                params.put("idPersonal",String.valueOf(idPersonal));
                params.put("nombrePersonal", nombrePersonal);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }


    public void eliminarPersonal(int idPersonal){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] args = {String.valueOf(idPersonal)};
        Cursor cursor = db.rawQuery("SELECT * FROM paso WHERE idPersonal = ?", args);

        if (cursor.moveToFirst()){
            Toast.makeText(
                    context.getApplicationContext(),
                    context.getText(R.string.error_eliminar).toString(),
                    Toast.LENGTH_SHORT).show();
        } else {
            db.delete("personal", "idPersonal = ?", args);
            db.delete("personalCargo", "idPersonal = ?", args);
            db.delete("personalUnidadAdmin", "idPersonal = ?", args);
            db.delete("personalUbicacion", "idPersonal = ?", args);

            Toast.makeText(
                    context.getApplicationContext(),
                    context.getText(R.string.personal_eliminado).toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void eliminarPersonalWS(final int idPersonal){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/personal.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        //Toast.makeText(context,"Eliminado correctamente de MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Eliminado correctamente de MySQL",Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

}
