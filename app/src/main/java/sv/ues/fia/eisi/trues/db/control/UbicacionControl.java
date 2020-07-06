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
import sv.ues.fia.eisi.trues.db.entity.Ubicacion;

public class UbicacionControl {
    private final Context context;
    private DatabaseHelper databaseHelper;


    public UbicacionControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    public Integer CrearUbicacion(float longitud, float latitud, float altitud, String componenteTamatica)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;
        String[] args = {String.valueOf(longitud),String.valueOf(latitud),String.valueOf(altitud),componenteTamatica};
        Cursor cursor= db.query("ubicacion", null, "longitud = ? AND latitud = ? AND altitud = ? AND componenteTematica = ?", args, null,null, null);
        ContentValues values =  new ContentValues();
        values.put("longitud", longitud);
        values.put("latitud", latitud);
        values.put("altitud", altitud);
        values.put("componenteTematica", componenteTamatica);
        if (cursor.moveToFirst()){
            mensaje = context.getText(R.string.error_duplicado).toString();
        }else {
            db.insert("ubicacion", null, values);
            mensaje = context.getText(R.string.ubicacion_creada).toString();

            Cursor cursor2 = db.rawQuery("SELECT MAX(idUbicacion) FROM ubicacion ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();
        }
        db.close();
        cursor.close();
        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        return ultimoId;
    }
    public void CrearUbicacionDwnl(int idUbicacion,float longitud, float latitud, float altitud, String componenteTamatica)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        Integer ultimoId = null;
        String[] args = {String.valueOf(idUbicacion)};
        Cursor cursor= db.query("ubicacion", null, "idUbicacion = ?", args, null,null, null);
        ContentValues values =  new ContentValues();
        values.put("idUbicacion",idUbicacion);
        values.put("longitud", longitud);
        values.put("latitud", latitud);
        values.put("altitud", altitud);
        values.put("componenteTematica", componenteTamatica);
        if (cursor.moveToFirst()){
            mensaje = "Error: la ubicacion ya existe";
            ActualizarUbicacion(idUbicacion,longitud,latitud,altitud,componenteTamatica);
        }else {
            db.insert("ubicacion", null, values);
            mensaje = "Ubicacion guardada con exito";

            Cursor cursor2 = db.rawQuery("SELECT MAX(idUbicacion) FROM ubicacion ",null);

            cursor2.moveToFirst();
            ultimoId = cursor2.getInt(0);
            cursor2.close();
        }
        db.close();
        cursor.close();
        ///Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void CrearUbicacionWS(final Integer idUbicacion, final float longitud, final float latitud, final float altitud, final String componenteTamatica){
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/ubicacion.php";
        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {
                        //JSONObject datos = jsonObject.getJSONObject("result");
                        //Toast.makeText(context,"Ubicacion guardada correctamente en MySQL"+response.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"Ubicacion guardada correctamente en MySQL",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();
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
                params.put("idUbicacion",String.valueOf(idUbicacion));
                params.put("longitud",String.valueOf(longitud));
                params.put("latitud",String.valueOf(latitud));
                params.put("altitud",String.valueOf(altitud));
                params.put("componenteTematica",componenteTamatica);
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public  String ActualizarUbicacion(int idUbicacion, float longitud, float latitud, float altitud, String componenteTamatica)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String[] args = {String.valueOf(idUbicacion)};

        Cursor cursor= db.query("ubicacion", null, "idUbicacion = ?", args, null,null, null);

        ContentValues values =  new ContentValues();
        values.put("idUbicacion", idUbicacion);
        values.put("longitud", longitud);
        values.put("latitud", latitud);
        values.put("altitud", altitud);
        values.put("componenteTematica", componenteTamatica);

        if (cursor.moveToFirst()){
            db.update("ubicacion",values, "idUbicacion = ?",args);
            mensaje = "Ubicacion actualizada";
        }
        else {
            mensaje = "Error, la ubicación no existe";

        }
        db.close();
        cursor.close();
        return mensaje;
    }

    public String EliminarUbicacion(int idUbicacion){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje ;
        String[] idUbi = {String.valueOf(idUbicacion)};
        Cursor cursorUbicacion = db.query("ubicacion",null,"idUbicacion = ?",idUbi,null,null,null);
        Cursor cursorUbicacionPersonal = db.query("ubicacionPaso",null,"idUbicacion = ?",idUbi,null,null,null);
        Cursor cursorPaso = db.query("paso",null,"idUbicacion = ?",idUbi,null,null,null);


        if (cursorUbicacion.moveToFirst()){//Si existe una Ubicacion
            if (cursorUbicacionPersonal.moveToFirst()){//Y Existe UbicacionPaso con Ubicacion
                db.delete("ubicacionPersonal","idUbicacion = ?",idUbi);
                //db.delete("ubicacion","idActividad = ?",args);
            }
            if (cursorPaso.moveToFirst()){//Si existe un Paso con Ubicacion
                String[] idPaso = {String.valueOf(cursorPaso.getInt(1))};
                Cursor cursorUsuarioPaso = db.query("usuarioPaso",null,"idPaso = ?",idPaso,null,null,null);
                if (cursorUsuarioPaso.moveToFirst()){//Si existe un UsuarioPaso con un Paso
                    db.delete("usuarioPaso","idPaso = ?",idPaso);
                    db.delete("paso","idUbicacion = ?",idUbi);

                }else {
                    db.delete("paso","idUbicacion = ?",idUbi) ;
                }
                cursorUsuarioPaso.close();
            }
            db.delete("ubicacion","idUbicacion = ?",idUbi);
            mensaje = "Se eliminó el paso "+idUbicacion+" con exito";
        }else {
            mensaje = "Error al eliminar el Paso";
        }

        cursorPaso.close();
        cursorUbicacion.close();
        cursorUbicacionPersonal.close();
        db.close();
        return mensaje;
    }

    public Ubicacion ObtenerUbicacion(int idUbicacion){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idUbicacion","longitud", "latitud", "altitud", "componenteTematica"};
        String[] args = {String.valueOf(idUbicacion)};
        Cursor cursor = db.query("ubicacion",columnas,"idUbicacion = ?",args,null,null,null);
        if(cursor.moveToFirst()){
            Ubicacion ubicacion = new Ubicacion();
            ubicacion.setIdUbicacion(cursor.getInt(0));
            ubicacion.setLongitud(cursor.getFloat(1));
            ubicacion.setLatidud(cursor.getFloat(2));
            ubicacion.setAltitud(cursor.getFloat(3));
            ubicacion.setComponenteTematica(cursor.getString(4));
            cursor.close();
            return ubicacion;
        }else {
            cursor.close();
            return null;
        }


    }

    public List<Ubicacion> ObtenerUbicaciones(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columnas = {"idUbicacion","longitud", "latitud", "altitud", "componenteTematica"};
        List<Ubicacion> ubicacionList = new ArrayList<>();
        Cursor cursor = db.query("ubicacion",columnas,null,null,null,null,null);
        while (cursor.moveToNext()){
                Ubicacion ubicacion = new Ubicacion();
                ubicacion.setIdUbicacion(cursor.getInt(0));
                ubicacion.setLongitud(cursor.getFloat(1));
                ubicacion.setLatidud(cursor.getFloat(2));
                ubicacion.setAltitud(cursor.getFloat(3));
                ubicacion.setComponenteTematica(cursor.getString(4));
                ubicacionList.add(ubicacion);
        }
        cursor.close();
        return ubicacionList;
    }

    public List<Ubicacion> ObtenerUbicaciones(int idPersonal){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Ubicacion> ubicacionList = new ArrayList<>();
        String[] args = {String.valueOf(idPersonal)};
        Cursor cursor = db.rawQuery("" +
                "SELECT * FROM ubicacion u JOIN personalUbicacion pU " +
                "WHERE u.idUbicacion = pU.idUbicacion AND pu.idPersonal = ?", args);
        while (cursor.moveToNext()){
                Ubicacion ubicacion = new Ubicacion();
                ubicacion.setIdUbicacion(cursor.getInt(0));
                ubicacion.setLongitud(cursor.getFloat(1));
                ubicacion.setLatidud(cursor.getFloat(2));
                ubicacion.setAltitud(cursor.getFloat(3));
                ubicacion.setComponenteTematica(cursor.getString(4));
                ubicacionList.add(ubicacion);
        }
        cursor.close();
        return ubicacionList;
    }

}
