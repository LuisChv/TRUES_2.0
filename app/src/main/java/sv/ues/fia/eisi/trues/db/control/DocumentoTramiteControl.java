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
import sv.ues.fia.eisi.trues.db.entity.DocumentoTramite;

public class DocumentoTramiteControl {
    private final Context context;
    private DatabaseHelper databaseHelper;
    private DocumentoControl documentoControl;

    public DocumentoTramiteControl(Context context) {
        this.context = context;
        documentoControl = new DocumentoControl(context);
        this.databaseHelper = new DatabaseHelper(context,"TRUES",null,1);
    }

    //Create
    public void crearDocumentoTramite(int idDocumento, int idTramite){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idDocumento),String.valueOf(idTramite)};
        String[] columns = {"idDocumento, idTramite"};
        Cursor cursor = db.query("documentoTramite",columns,"idDocumento = ? AND idTramite = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        if(cursor.moveToFirst()){
            mensaje = context.getText(R.string.error_documento).toString();
        } else {
            values.put("idDocumento",idDocumento);
            values.put("idTramite",idTramite);
            db.insert("documentoTramite",null,values);
            mensaje = context.getText(R.string.documento_creado).toString();
        }
        db.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void crearDocumentoTramiteDlwd(int idDocumentoTramite,int idDocumento, int idTramite){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idDocumentoTramite),String.valueOf(idDocumento),String.valueOf(idTramite)};
        String[] columns = {"idDocumentoTramite","idDocumento, idTramite"};
        Cursor cursor = db.query("documentoTramite",columns,"idDocumentoTramite = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        if(cursor.moveToFirst()){
            mensaje = "Ya existe este registro";
        } else {
            values.put("idDocumento",idDocumento);
            values.put("idTramite",idTramite);
            db.insert("documentoTramite",null,values);
            mensaje = "Se ha registrado un nuevo Documento al tramite";
        }
        db.close();
        //Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void crearDocumentoTramiteWS(final int idDocumento, final int idTramite){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/documentoTramite.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        Toast.makeText(context,"Relacion Guardada en MySQL",Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "Registro ya existe en MySQL"
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
                params.put("idDocumento",String.valueOf(idDocumento));
                params.put("idTramite",String.valueOf(idTramite));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    //Read
    public List<DocumentoTramite> consultarDocumentosTramites(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idDocumentoTramite","idDocumento","idTramite"};
        List<DocumentoTramite> arrayDocumentosTramites = new ArrayList<>();
        DocumentoTramite documentoTramite = null;
        Cursor cursor = db.query("documentoTramite",columnas,null,null,null,null,null);

        if (cursor.moveToFirst()){


            do {
                documentoTramite = new DocumentoTramite();
                documentoTramite.setIdDocumentoTramite(cursor.getInt(0));
                documentoTramite.setIdDocumento(cursor.getInt(1));
                documentoTramite.setIdDocumento(cursor.getInt(2));
                arrayDocumentosTramites.add(documentoTramite);
            }while (cursor.moveToNext());

        }
        return arrayDocumentosTramites;
    }

    public DocumentoTramite consultarDocumentoTramiteEspifico(int idDocumentoTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idDocumentoTramite","idDocumento","idTramite"};
        String args[] = {String.valueOf(idDocumentoTramite)};
        DocumentoTramite documentoTramite = null;
        Cursor cursor = db.query("documentoTramite",columnas,"idDocumentoTramite = ? ", args,null,null,null);
        if (cursor.moveToFirst()){
            documentoTramite = new DocumentoTramite();
            documentoTramite.setIdDocumentoTramite(cursor.getInt(0));
            documentoTramite.setIdDocumento(cursor.getInt(1));
            documentoTramite.setIdTramite(cursor.getInt(2));
        }

        return documentoTramite;
    }

    public List<Documento> consultarDocumentosDelTramite(int idTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String args[] = {String.valueOf(idTramite)};
        String[] columnas = {"idDocumentoTramite","idDocumento","idTramite"};
        List<Documento> arrayDocumento = new ArrayList<>();
        Documento documento = null;
        Cursor cursor = db.query("documentoTramite",columnas,"idTramite = ?", args,null,null,null);
        if (cursor.moveToFirst()){

            do{
                documento = documentoControl.consultarDocumeto(cursor.getInt(1));
                arrayDocumento.add(documento);
            }while(cursor.moveToNext());
        }

        return arrayDocumento;
    }

    //Update
    public String actualizarDocumentosTramite(int idDocumentoTramite, int idDocumento, int idTramite){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {String.valueOf(idDocumentoTramite)};
        Cursor cursor = db.query("documentoTramite",null,"idDocumentoTramite = ?",args,null,null,null);
        ContentValues values = new ContentValues();
        values.put("idDocumentoTramite",idDocumentoTramite);
        values.put("idDocumento",idDocumento);
        values.put("idTramite",idTramite);
        if (cursor.moveToFirst()){
            db.update("documentoTramite",values,"idDocumentoTramite = ?",args);
            mensaje = "Se ha actualizado con exito el documento del tramite";
        } else {
            mensaje = "No se ha encontrado ningun registro";
        }
        return mensaje;
    }

    //Delete
    public void eliminarDocumentoTramite(int idDocumento, int idTramite){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String args[] = {String.valueOf(idDocumento), String.valueOf(idTramite)};
        db.delete("documentoTramite","idDocumento = ? AND idTramite = ?",args);
        db.close();
        Toast.makeText(context.getApplicationContext(), context.getText(R.string.documento_eliminado).toString(), Toast.LENGTH_SHORT).show();
    }

    public void eliminarDocumentoTramiteWS(final int idDocumento, final int idTramite){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/documentoTramite.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        //Toast.makeText(context,"RelacionEliminada de MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"RelacionEliminada de MySQL",Toast.LENGTH_SHORT).show();
                    } else if (status.equals("err")) {
                        Toast.makeText(context, "Registro ya existe"
                                , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Hay un problema con la base de datos"
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
                params.put("idDocumento",String.valueOf(idDocumento));
                params.put("idTramite",String.valueOf(idTramite));
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }
}
