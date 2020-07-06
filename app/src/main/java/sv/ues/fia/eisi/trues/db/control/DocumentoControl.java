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
import sv.ues.fia.eisi.trues.db.entity.Documento;

public class DocumentoControl {

    private final Context context;
    private DatabaseHelper databaseHelper;

    public DocumentoControl(Context context){
        this.context = context;
        databaseHelper = new DatabaseHelper(context,"TRUES",null,1);
    }

    public Integer CrearDocumento(String url, String nombreDocumento){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;
        String[] args = {url};
        Cursor cursor = db.query("documento",null,"url = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        if(cursor.moveToFirst()){
            mensaje = context.getText(R.string.error_documento).toString();
        } else {
            values.put("url",url);
            values.put("nombreDocumento",nombreDocumento);
            db.insert("documento",null,values);
            mensaje = context.getText(R.string.documento_creado).toString();

            Cursor cursor2 = db.rawQuery("SELECT MAX(idDocumento) FROM documento ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();
        }
        db.close();
        cursor.close();

        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        return ultimoId;
    }

    public Integer CrearDocumentoDwld(String idDocumento,String url, String nombreDocumento){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;
        String[] args = {idDocumento};
        Cursor cursor = db.query("documento",null,"idDocumento = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        if(cursor.moveToFirst()){
            mensaje = "El documento ya existe";
            ActualizarDocumento(Integer.parseInt(idDocumento),url,nombreDocumento);
        } else {
            values.put("url",url);
            values.put("nombreDocumento",nombreDocumento);
            db.insert("documento",null,values);
            mensaje = "Se ha registrado un nuevo documento";

            Cursor cursor2 = db.rawQuery("SELECT MAX(idDocumento) FROM documento ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();
        }
        db.close();
        cursor.close();

        //Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        return ultimoId;
    }

    public void CrearDocumentoWS(final Integer idDocumento, final String url, final String nombreDocumento){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/documento.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Se subió correctamente a MySQL",Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "Registro ya existe"
                                , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Hay un problema con la base de datos."
                            + response.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("operacion", "create");
                params.put("idDocumento",String.valueOf(idDocumento));
                params.put("url",url);
                params.put("nombreDocumento",nombreDocumento);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void actualizarDocumentoWS(final Integer idDocumento, final String url, final String nombreDocumento){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/documento.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        Toast.makeText(context,"Actualizado correctamente en MySQL",Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "Registro Not existe"+response.toString()
                                , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Hay un problema con la base de datos."
                            + response.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("operacion", "update");
                params.put("idDocumento",String.valueOf(idDocumento));
                params.put("url",url);
                params.put("nombreDocumento",nombreDocumento);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }


    //Read
    public List<Documento> consultarDocumentos(Integer idTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Documento> arrayDocumentos = new ArrayList<>();
        Documento documento = null;
        String [] args = {String.valueOf(idTramite)};
        Cursor cursor = db.rawQuery("SELECT * FROM documento d JOIN documentoTramite dT  " +
                "WHERE d.idDocumento = dT.idDocumento AND dt.idTramite = ?", args);

        while (cursor.moveToNext()){
                documento = new Documento();
                documento.setIdDoc(cursor.getInt(0));
                documento.setUrl(cursor.getString(1));
                documento.setNombreDocumento(cursor.getString(2));
                arrayDocumentos.add(documento);
        }
        cursor.close();
        return arrayDocumentos;
    }

    public List<Documento> consultarDocumentos(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Documento> arrayDocumentos = new ArrayList<>();
        Documento documento = null;
        Cursor cursor = db.rawQuery("SELECT * FROM documento", null);

        while (cursor.moveToNext()){
            documento = new Documento();
            documento.setIdDoc(cursor.getInt(0));
            documento.setUrl(cursor.getString(1));
            documento.setNombreDocumento(cursor.getString(2));
            arrayDocumentos.add(documento);
        }
        cursor.close();
        return arrayDocumentos;
    }

    public Documento consultarDocumeto(int idDocumento){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idDocumento","url","nombreDocumento"};
        String[] args = {String.valueOf(idDocumento)};
        Documento documento = null;
        Cursor cursor = db.query("documento",columnas,"idDocumento = ? ", args,null,null,null);
        if (cursor.moveToFirst()){
            documento = new Documento();
            documento.setIdDoc(cursor.getInt(0));
            documento.setUrl(cursor.getString(1));
            documento.setNombreDocumento(cursor.getString(2));
        }
        cursor.close();
        return documento;
    }

    public void ActualizarDocumento(int idDocumento, String url, String nombreDocumento){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idDocumento)};
        Cursor cursor = db.query("documento",null,"idDocumento = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("idDocumento",idDocumento);
        values.put("url",url);
        values.put("nombreDocumento",nombreDocumento);
        if(cursor.moveToFirst()){
            db.update("documento",values,"idDocumento = ?", args);
            mensaje = context.getText(R.string.cambios_guardados).toString();
        } else {
            mensaje = context.getText(R.string.error_actualizar).toString();
        }
        cursor.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public String EliminarDocumento(int idDocumento){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idDocumento)};
        String[] columnasPersonalCargo = {"idDocumento"};
        Cursor cursorDocumentoTramite = db.query("documentoTramite",columnasPersonalCargo,"idDocumento = ?", args,null,null,null);
        Cursor cursorDocumento = db.query("documento",null,"idDocumento = ?",args,null,null,null);
        if(cursorDocumentoTramite.moveToNext()){
            if (cursorDocumento.moveToFirst()){
                db.delete("documentoTramite","idDocumento = ?", args);
                db.delete("documento","idDocumento = ?", args);
                mensaje = "Se ha eliminado correctamente el cargo "+idDocumento+" Además se eliminó un elemento de la tabla documentoTramite";
            }
            else {
                db.delete("cargo","idDocumento = ?",args);
                mensaje = "Se ha eliminado el documento " + idDocumento + " con éxito.";
            }
        }else {
            mensaje ="Error al eliminar";
        }
        db.close();
        cursorDocumento.close();
        cursorDocumentoTramite.close();
        return mensaje;
    }
    public void eliminarDocumentoWS(final Integer idDocumento){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/documento.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        Toast.makeText(context,"Eliminado correctamente de MySQL",Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "Documento no existe"+response.toString()
                                , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Hay un problema con la base de datos."
                            + response.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("idDocumento",String.valueOf(idDocumento));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }



}
