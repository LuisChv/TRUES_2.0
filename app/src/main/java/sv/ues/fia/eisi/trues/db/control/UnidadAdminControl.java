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
import sv.ues.fia.eisi.trues.db.entity.PersonalUnidadAdmin;
import sv.ues.fia.eisi.trues.db.entity.UnidadAdmin;

public class UnidadAdminControl {
    private final Context context;
    private DatabaseHelper databaseHelper;

    public UnidadAdminControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }
    /*idUnidad INTEGER NOT NULL PRIMARY KEY,
     idFacultad INTEGER NOT NULL,
     nombreUnidadAdmin VARCHAR(64) NOT NULL*/


    //CREAR
    public Integer crearUAdmin(Integer idFacultad, String nombreUnidadAdmin){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;

        ContentValues values = new ContentValues();
        values.put("idFacultad",idFacultad);
        values.put("nombreUnidadAdmin", nombreUnidadAdmin);

        db.insert("unidadAdmin", null, values);
        mensaje=context.getText(R.string.unidad_creada).toString();

        Cursor cursor2 = db.rawQuery("SELECT MAX(idUnidad) FROM unidadAdmin ",null);
        cursor2.moveToFirst();

        ultimoId = cursor2.getInt(0);
        cursor2.close();

        db.close();

        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
        return ultimoId;
    }
    public void crearUAdminDwnl(int idUnidad,Integer idFacultad, String nombreUnidadAdmin){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;

        ContentValues values = new ContentValues();
        values.put("idUnidad",idUnidad);
        values.put("idFacultad",idFacultad);
        values.put("nombreUnidadAdmin", nombreUnidadAdmin);

        db.insert("unidadAdmin", null, values);
        mensaje="Se ha creado con exito la unidad administrativa: " + nombreUnidadAdmin;

        Cursor cursor2 = db.rawQuery("SELECT MAX(idUnidad) FROM unidadAdmin ",null);
        cursor2.moveToFirst();

        ultimoId = cursor2.getInt(0);
        cursor2.close();

        db.close();
        UnidadAdmin u=new UnidadAdmin(idUnidad,idFacultad,nombreUnidadAdmin);
        actualizarUAdmin(u);
        //Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }
    public void crearUAdminWS(final Integer idUnidad, final Integer idFacultad, final String nombreUnidadAdmin){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/unidadAdmin.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        //Toast.makeText(context,"unidad Administrativa subida a MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"unidad Administrativa subida a MySQL",Toast.LENGTH_SHORT).show();
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
                params.put("operacion", "create");
                params.put("idUnidad",String.valueOf(idUnidad));
                params.put("idFacultad",String.valueOf(idFacultad));
                params.put("nombreUnidadAdmin",nombreUnidadAdmin);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }


    //OBTENER
    public UnidadAdmin obtenerUAdmin(String id){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] columnas = {"idUnidad","idFacultad", "nombreUAdmin"};
        String[] values={id};
        UnidadAdmin uAdmin = null;
        Cursor cursor = db.query("unidadAdmin",columnas, "idUnidad = ?",values, null, null, null, null);

        if (cursor.moveToFirst()){
            uAdmin = new UnidadAdmin();
            uAdmin.setIdUAdmin(cursor.getInt(0));
            uAdmin.setIdFacultad(cursor.getInt(1));
            uAdmin.setNombreUAdmin(cursor.getString(2));
        }

        return uAdmin;
    }

    public List<UnidadAdmin> obtenerUAdmin(int idFacultad){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<UnidadAdmin> uAdminList = new ArrayList<>();
        String[] args = {String.valueOf(idFacultad)};
        Cursor cursor = db.rawQuery("SELECT * FROM unidadAdmin WHERE idFacultad = ?",args);

        while (cursor.moveToNext()){
            UnidadAdmin uAdmin = new UnidadAdmin();
            uAdmin.setIdUAdmin(cursor.getInt(0));
            uAdmin.setNombreUAdmin(cursor.getString(2));
            uAdminList.add(uAdmin);
        }

        cursor.close();
        return uAdminList;
    }

    public List<UnidadAdmin> consultar(int idPersonal){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<UnidadAdmin> uAdminList = new ArrayList<>();
        String[] args = {String.valueOf(idPersonal)};
        Cursor cursor = db.rawQuery(
                "SELECT * FROM unidadAdmin uA JOIN personalUnidadAdmin pUA  " +
                        "WHERE uA.idUnidad = pUA.idUnidad AND pUA.idPersonal = ?",args);

        while (cursor.moveToNext()){
            UnidadAdmin uAdmin = new UnidadAdmin();
            uAdmin.setIdUAdmin(cursor.getInt(0));
            uAdmin.setNombreUAdmin(cursor.getString(2));
            uAdminList.add(uAdmin);
        }

        cursor.close();
        return uAdminList;
    }

    //UPDATE
    public String actualizarUAdmin(UnidadAdmin uAdmin){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(uAdmin.getIdUAdmin() )};
        Cursor cursor = db.query("unidadAdmin",null,"idUnidad = ?", args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("nombreUAdmin",uAdmin.getNombreUAdmin());

        if (cursor.moveToFirst()){
            db.update("unidadAdmin", values,"idUnidad = ?", args);
            mensaje = "Unidad administrativa: "+uAdmin.getNombreUAdmin()+" actualizado con exito";
        }
        else {
            mensaje = "Unidad administrativa: " + uAdmin.getNombreUAdmin() + "no se encuentra o no existe";
        }

        cursor.close();
        return mensaje;
    }
    //ELIMINAR
    public String EliminarCargo(UnidadAdmin uAdmin){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        PersonalUnidadAdminControl personalControl = new PersonalUnidadAdminControl(context);
        String mensaje;
        String[] args = {String.valueOf(uAdmin.getIdUAdmin())};
        String[] columnasPersonalUnidadAdmin = {"idUnidad"};
        Cursor cursor = db.query("unidadAdmnin",columnasPersonalUnidadAdmin,"idUnidad = ?", args,null,null,null);
        Cursor cursor1 = db.query("personalUnidadAdmin",null,"idPersonalAdmin = ?",args,null,null,null);

        if(cursor.moveToNext()){
            if(cursor1.moveToFirst()){
                db.delete("personalUnidadAdmin","idUnidad=?",args);
                db.delete("unidadAdmin","idUnidad=?",args);
            }
            mensaje="Se ha eliminado la unidad administrativa: "+uAdmin.getNombreUAdmin()+ " con exito";
        }else {
            mensaje ="Error al eliminar";
        }
        cursor.close();
        cursor1.close();
        return mensaje;
    }
}
