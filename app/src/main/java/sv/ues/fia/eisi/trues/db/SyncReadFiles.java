package sv.ues.fia.eisi.trues.db;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.AccesoUsuarioControl;
import sv.ues.fia.eisi.trues.db.control.ActividadControl;
import sv.ues.fia.eisi.trues.db.control.ActividadTramiteControl;
import sv.ues.fia.eisi.trues.db.control.CargoControl;
import sv.ues.fia.eisi.trues.db.control.DocumentoControl;
import sv.ues.fia.eisi.trues.db.control.DocumentoTramiteControl;
import sv.ues.fia.eisi.trues.db.control.FacultadControl;
import sv.ues.fia.eisi.trues.db.control.OpcionCrudControl;
import sv.ues.fia.eisi.trues.db.control.PasoControl;
import sv.ues.fia.eisi.trues.db.control.PersonalCargoControl;
import sv.ues.fia.eisi.trues.db.control.PersonalControl;
import sv.ues.fia.eisi.trues.db.control.PersonalUbicacionControl;
import sv.ues.fia.eisi.trues.db.control.PersonalUnidadAdminControl;
import sv.ues.fia.eisi.trues.db.control.RequisitoControl;
import sv.ues.fia.eisi.trues.db.control.RequisitoTramiteControl;
import sv.ues.fia.eisi.trues.db.control.TramiteControl;
import sv.ues.fia.eisi.trues.db.control.UbicacionControl;
import sv.ues.fia.eisi.trues.db.control.UnidadAdminControl;

public class SyncReadFiles {
    private Boolean ok;
    private ActividadControl actividadControl;
    private AccesoUsuarioControl accesoUsuarioControl;
    private ActividadTramiteControl actividadTramiteControl;
    private CargoControl cargoControl;
    private DocumentoControl documentoControl;
    private DocumentoTramiteControl documentoTramiteControl;
    private FacultadControl facultadControl;
    private OpcionCrudControl opcionCrudControl;
    private PasoControl pasoControl;
    private PersonalCargoControl personalCargoControl;
    private PersonalControl personalControl;
    private PersonalUbicacionControl personalUbicacionControl;
    private PersonalUnidadAdminControl personalUnidadAdminControl;
    private RequisitoControl requisitoControl;
    private RequisitoTramiteControl requisitoTramiteControl;
    private TramiteControl tramiteControl;
    private UbicacionControl ubicacionControl;
    private UnidadAdminControl unidadAdminControl;
    public SyncReadFiles() {

    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public void SubirCambios(Context context){
        readFromFileActividad(context, "Actividad");
        deleteFromFile(context, "Actividad");
        getActivities(context);

        readFromFileTramite(context, "Tramite");
        deleteFromFile(context, "Tramite");
        getTramites(context);

        readFromFilePersonal(context, "Personal");
        deleteFromFile(context, "Personal");
        getPersonal(context);

        readFromFileCargo(context, "Cargo");
        deleteFromFile(context, "Cargo");
        getCargo(context);


        readFromFilePersonalCargo(context, "PersonalCargo");
        deleteFromFile(context, "PersonalCargo");
        getPersonalCargo(context);

        readFromFileUnidadAdmin(context, "UnidadAdmin");
        deleteFromFile(context, "UnidadAdmin");
        getUnidadAdmin(context);

        readFromFilePersonalUnidadAdmin(context, "PersonalUnidadAdmin");
        deleteFromFile(context, "PersonalUnidadAdmin");
        getPersonalUnidadAdmin(context);

        readFromFileUbicacion(context, "Ubicacion");
        deleteFromFile(context, "Ubicacion");
        getUbicacion(context);

        readFromFilePersonalUbicacion(context, "PersonalUbicacion");
        deleteFromFile(context, "PersonalUbicacion");
        getPersonalUbicacion(context);

        readFromFilePaso(context, "Paso");
        deleteFromFile(context, "Paso");
        getPaso(context);

        readFromFileRequisito(context, "Requisito");
        deleteFromFile(context, "Requisito");
        getRequisito(context);

        readFromFileRequisitoTramite(context, "RequisitoTramite");
        deleteFromFile(context, "RequisitoTramite");
        getRequisitoTramite(context);

        readFromFileDocumento(context, "Documento");
        deleteFromFile(context, "Documento");
        getDocumento(context);

        readFromFileDocumentoTramite(context, "DocumentoTramite");
        deleteFromFile(context, "DocumentoTramite");
        getDocumentoTramite(context);

        readFromFileActividadTramite(context, "ActividadTramite");
        deleteFromFile(context, "ActividadTramite");
        getActividadTramite(context);

        readFromFileAccesoUsuario(context, "AccesoUsuario");
        deleteFromFile(context, "AccesoUsuario");
        getAccesoUsuario(context);

        Toast.makeText(context, "Finalizó la Sincronización",Toast.LENGTH_SHORT).show();
    }

    public void getAccesoUsuario(final Context context){
        accesoUsuarioControl = new AccesoUsuarioControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/accesoUsuario.php";

        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        int idAccesoUsuario=datos.getJSONObject(i).getInt("IDACCESOUSUARIO");
                        String usuario=datos.getJSONObject(i).getString("USUARIO");
                        String idoption=datos.getJSONObject(i).getString("IDOPCION");
                                /*Toast.makeText(context,idActividad+", "+
                                                idFacultad+", "+
                                                nombreActividad+", "+
                                                inicio+", "+
                                                finall+"."
                                        ,Toast.LENGTH_SHORT).show();*/
                        try{
                            accesoUsuarioControl.CrearAccesoUsuarioDwnl(idAccesoUsuario,usuario,idoption);
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();

                        }catch(Exception e){
                            //  Toast.makeText(context,"Registro no descargado",Toast.LENGTH_SHORT).show();
                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "AccesoUsuario Hay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "AccesoUsuario Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getActividadTramite(final Context context){
        actividadTramiteControl = new ActividadTramiteControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/actividadTramite.php";

        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        int idActividadTramite=datos.getJSONObject(i).getInt("IDACTIVIDADTRAMITE");
                        int idActividad=datos.getJSONObject(i).getInt("IDACTIVIDAD");
                        int idTramite=datos.getJSONObject(i).getInt("IDTRAMITE");

                                /*Toast.makeText(context,idActividad+", "+
                                                idFacultad+", "+
                                                nombreActividad+", "+
                                                inicio+", "+
                                                finall+"."
                                        ,Toast.LENGTH_SHORT).show();*/
                        try{
                            actividadTramiteControl.CrearActividadTramiteDwld(idActividadTramite,idActividad, idTramite);
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();

                        }catch(Exception e){
                            //Toast.makeText(context,"Registro no descargado",Toast.LENGTH_SHORT).show();
                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "ActividadTremiteHay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "ActividadTremiteHay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getDocumentoTramite(final Context context){
        documentoTramiteControl = new DocumentoTramiteControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/documentoTramite.php";

        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        int idDocumentoTramite=datos.getJSONObject(i).getInt("IDDOCUMENTOTRAMITE");
                        int idDocumento=datos.getJSONObject(i).getInt("IDDOCUMENTO");
                        int idTramite=datos.getJSONObject(i).getInt("IDTRAMITE");

                                /*Toast.makeText(context,idActividad+", "+
                                                idFacultad+", "+
                                                nombreActividad+", "+
                                                inicio+", "+
                                                finall+"."
                                        ,Toast.LENGTH_SHORT).show();*/
                        try{
                            documentoTramiteControl.crearDocumentoTramiteDlwd(idDocumentoTramite,idDocumento,idTramite);
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();

                        }catch(Exception e){
                            //Toast.makeText(context,"Registro no descargado",Toast.LENGTH_SHORT).show();
                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "DocumentoTramite Hay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "DocumentoTramite Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getDocumento(final Context context){
        documentoControl = new DocumentoControl(context);
        String ip = context.getString(R.string.IP);
        final String URL = ip+"/TRUES/documento.php";

        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        String idDocumento=datos.getJSONObject(i).getString("IDDOCUMENTO");
                        String url=datos.getJSONObject(i).getString("URL");
                        String nombreDocumento=datos.getJSONObject(i).getString("NOMBREDOCUMENTO");

                                /*Toast.makeText(context,idActividad+", "+
                                                idFacultad+", "+
                                                nombreActividad+", "+
                                                inicio+", "+
                                                finall+"."
                                        ,Toast.LENGTH_SHORT).show();*/
                        try{
                            int id = documentoControl.CrearDocumentoDwld(idDocumento,url,nombreDocumento);
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();

                        }catch(Exception e){
                            //Toast.makeText(context,"Registro no descargado",Toast.LENGTH_SHORT).show();
                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Documento Hay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "Documento Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getActivities(final Context context){
        actividadControl = new ActividadControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/actividad.php";

        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        int idActividad=datos.getJSONObject(i).getInt("IDACTIVIDAD");
                        int idFacultad=datos.getJSONObject(i).getInt("IDFACULTAD");
                        String nombreActividad=datos.getJSONObject(i).getString("NOMBREACTIVIDAD");
                        String inicio=datos.getJSONObject(i).getString("INICIO");
                        String finall=datos.getJSONObject(i).getString("FINAL");

                        try{
                            int id = actividadControl.CrearActividadDwld(idActividad,idFacultad, nombreActividad, inicio, finall);
                            //Toast.makeText(context,"Registro descargado"+response.toString(),Toast.LENGTH_SHORT).show();

                        }catch(Exception e){
                            //Toast.makeText(context,"Registro no descargado"+response.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Actividad Hay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "Actividad Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getTramites(final Context context){
        tramiteControl = new TramiteControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/tramite.php";

        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        int idTramite=datos.getJSONObject(i).getInt("IDTRAMITE");
                        int idFacultad=datos.getJSONObject(i).getInt("IDFACULTAD");
                        String nombreTramite=datos.getJSONObject(i).getString("NOMBRETRAMITE");

                                /*Toast.makeText(context,idActividad+", "+
                                                idFacultad+", "+
                                                nombreActividad+", "+
                                                inicio+", "+
                                                finall+"."
                                        ,Toast.LENGTH_SHORT).show();*/
                        try{
                            tramiteControl.crearTramiteDwld(idTramite,idFacultad, nombreTramite);
                            //Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show();

                        }catch(Exception e){
                            //Toast.makeText(context,"Registro no descargado",Toast.LENGTH_SHORT).show();
                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Tramite Hay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "Tramite Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getCargo(final Context context){
        cargoControl = new CargoControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/cargo.php";

        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        int idCargo=datos.getJSONObject(i).getInt("IDCARGO");
                        String nombreCargo=datos.getJSONObject(i).getString("NOMBRECARGO");

                                /*Toast.makeText(context,idActividad+", "+
                                                idFacultad+", "+
                                                nombreActividad+", "+
                                                inicio+", "+
                                                finall+"."
                                        ,Toast.LENGTH_SHORT).show();*/
                        try{
                            int id = cargoControl.CrearCargoDwld(idCargo,nombreCargo);
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();
                        }catch(Exception e){
                            // Toast.makeText(context,"Registro no descargado",Toast.LENGTH_SHORT).show();
                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Cargo Hay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "Cargo Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getPersonal(final Context context){
        personalControl = new PersonalControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/personal.php";

        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        int idPersonal=datos.getJSONObject(i).getInt("IDPERSONAL");
                        String nombrePersonal=datos.getJSONObject(i).getString("NOMBREPERSONAL");

                        //String finall=datos.getJSONObject(i).getString("FINAL");
                                /*Toast.makeText(context,idActividad+", "+
                                                idFacultad+", "+
                                                nombreActividad+", "+
                                                inicio+", "+
                                                finall+"."
                                        ,Toast.LENGTH_SHORT).show();*/
                        try{
                            int id = personalControl.crearPersonalDwld(idPersonal,nombrePersonal);
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();
                        }catch(Exception e){
                            //Toast.makeText(context,"Registro no descargado",Toast.LENGTH_SHORT).show();
                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Personal Hay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "Personal Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.networkResponse.statusCode
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getPersonalCargo(final Context context){
        personalCargoControl = new PersonalCargoControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/personalCargo.php";

        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        int idPersonalCargo=datos.getJSONObject(i).getInt("IDPERSONALCARGO");
                        int idPersonal=datos.getJSONObject(i).getInt("IDPERSONAL");
                        int idCargo = datos.getJSONObject(i).getInt("IDCARGO");
                                /*Toast.makeText(context,idActividad+", "+
                                                idFacultad+", "+
                                                nombreActividad+", "+
                                                inicio+", "+
                                                finall+"."
                                        ,Toast.LENGTH_SHORT).show();*/
                        try{
                            personalCargoControl.crearPersonalCargoDwnl(idPersonalCargo,idPersonal, idCargo);
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();

                        }catch(Exception e){

                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "PersoalCargo Hay un problema con la base de datos:\n"+e+"\n" + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "PersoalCargo Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                +error.networkResponse.statusCode
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getUnidadAdmin(final Context context){
        unidadAdminControl = new UnidadAdminControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/unidadAdmin.php";

        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    JSONArray datos = jsonObject.getJSONArray("result");
                    for(int i=0;i<datos.length();i++){
                        int idUnidadAdimin=datos.getJSONObject(i).getInt("IDUNIDAD");
                        int idFacultad=datos.getJSONObject(i).getInt("IDFACULTAD");
                        String nombreUnidadAdmin=datos.getJSONObject(i).getString("NOMBREUNIDADADMIN");
                        try{
                            unidadAdminControl.crearUAdminDwnl(idUnidadAdimin,idFacultad, nombreUnidadAdmin);
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();

                        }catch(Exception e){

                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "UnidadAdmin Hay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "UnidadAdmin Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getPersonalUnidadAdmin(final Context context){
        personalUnidadAdminControl = new PersonalUnidadAdminControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/personalUnidadAdmin.php";

        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        int idPersonalUnidad=datos.getJSONObject(i).getInt("IDPERSONALADMIN");
                        int idPersonal=datos.getJSONObject(i).getInt("IDPERSONAL");
                        int idUnidad=datos.getJSONObject(i).getInt("IDUNIDAD");
                        try{
                            personalUnidadAdminControl.crearPersonalUnidadDwnl(idPersonalUnidad,idPersonal, idUnidad);
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();
                        }catch(Exception e){

                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "PersonalUnidadAdmin Hay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "PersonalUnidadAdmin Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getUbicacion(final Context context){
        ubicacionControl = new UbicacionControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/ubicacion.php";

        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        int idUbicacion = datos.getJSONObject(i).getInt("IDUBICACION");
                        float longitud=(float) datos.getJSONObject(i).getDouble("LONGITUD");
                        float latitud=(float) datos.getJSONObject(i).getDouble("LATITUD");
                        float altitud=(float) datos.getJSONObject(i).getDouble("ALTITUD");
                        String componenteTematica=datos.getJSONObject(i).getString("COMPONENTETEMATICA");

                        try{
                            ubicacionControl.CrearUbicacionDwnl(idUbicacion,longitud, latitud, altitud, componenteTematica);
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();

                        }catch(Exception e){

                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Ubicacion Hay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "Ubicacion Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getPersonalUbicacion(final Context context){
        personalUbicacionControl = new PersonalUbicacionControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/personalUbicacion.php";

        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        int idPersonalUbicacion=datos.getJSONObject(i).getInt("IDPERSONALUBICACION");
                        int idPersonal=datos.getJSONObject(i).getInt("IDPERSONAL");
                        int idUbicacion=datos.getJSONObject(i).getInt("IDUBICACION");

                        try{
                            personalUbicacionControl.crearPersonalUbicacionDwnl(idPersonalUbicacion,idPersonal, idUbicacion);
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();

                        }catch(Exception e){

                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "PersonalUbicacion Hay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "HPersonalUbicacion ay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getPaso(final Context context){
        pasoControl = new PasoControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/paso.php";

        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        int idPaso=datos.getJSONObject(i).getInt("IDPASO");
                        int idUbicacion=datos.getJSONObject(i).getInt("IDPERSONAL");
                        int idTramite=datos.getJSONObject(i).getInt("IDTRAMITE");
                        String descripcionPaso=datos.getJSONObject(i).getString("DESCRIPCIONPASO");
                        float porcentaje=(float) datos.getJSONObject(i).getDouble("PORCENTAJE");
                                /*Toast.makeText(context,idActividad+", "+
                                                idFacultad+", "+
                                                nombreActividad+", "+
                                                inicio+", "+
                                                finall+"."
                                        ,Toast.LENGTH_SHORT).show();*/
                        try{
                            pasoControl.crearPasoDwnl(idPaso,idUbicacion, idTramite, descripcionPaso, porcentaje);
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();

                        }catch(Exception e){

                            Toast.makeText(context,"Registro actualizado",Toast.LENGTH_SHORT).show();
                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Paso Hay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "Paso Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getRequisito(final Context context){
        requisitoControl = new RequisitoControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/requisito.php";

        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        int idrequisito=datos.getJSONObject(i).getInt("IDREQUISITO");
                        String descripcion=datos.getJSONObject(i).getString("DESCRIPCION");

                                /*Toast.makeText(context,idActividad+", "+
                                                idFacultad+", "+
                                                nombreActividad+", "+
                                                inicio+", "+
                                                finall+"."
                                        ,Toast.LENGTH_SHORT).show();*/
                        try{
                            requisitoControl.crearRequisitoDwnl(idrequisito,descripcion);
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();

                        }catch(Exception e){

                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Requisito Hay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "Requisito Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public void getRequisitoTramite(final Context context){
        requisitoTramiteControl = new RequisitoTramiteControl(context);
        String ip = context.getString(R.string.IP);
        String URL = ip+"/TRUES/requisitoTramite.php";

        //RequestQueue requestQueue = new Volley.newRequestQueue(context);
        //RequestQueue requestQueue = new Volley().newRequestQueue(context);
        Response.Listener responseListener = new Response.Listener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    //String status = jsonObject.getString("status");

                    JSONArray datos = jsonObject.getJSONArray("result");

                    //JSONObject datos = jsonObject.getJSONObject("result");
                    for(int i=0;i<datos.length();i++){
                        int idRequisitoTramite=datos.getJSONObject(i).getInt("IDREQUISITOTRAMITE");
                        int idRequisito=datos.getJSONObject(i).getInt("IDREQUISITO");
                        int idTramite=datos.getJSONObject(i).getInt("IDTRAMITE");

                                /*Toast.makeText(context,idActividad+", "+
                                                idFacultad+", "+
                                                nombreActividad+", "+
                                                inicio+", "+
                                                finall+"."
                                        ,Toast.LENGTH_SHORT).show();*/
                        try{
                            requisitoTramiteControl.crearRequisitoTramiteDwnl(idRequisitoTramite,idTramite, idRequisito);
                            //Toast.makeText(context,"Registro descargado",Toast.LENGTH_SHORT).show();

                        }catch(Exception e){

                        }
                    }
                    //Toast.makeText(context,"Guardado"+response.toString(),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "RequisitoTremite Hay un problema con la base de datos:\n"+e+"\n"
                            + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener ErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,
                        "RequisitoTremite Hay un problema con nuestros servidores.\n" +
                                "Estamos trabajando para corregirlo.\n"
                                + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                responseListener,ErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operacion","0");
                return params;
            }
        };
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    //---------------------------------------------------------------------------------------------------

    public void deleteFromFile(Context context, String tabla) {
        String prueba = "";

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(tabla + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.append(prueba);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        //String after = readFromFile(context);
        //Toast.makeText(context,after,Toast.LENGTH_SHORT).show();
    }

    public String readFromFileActividad(final Context context, String tabla) {
        String ret = "";
        setOk(false);
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int idActividad, idFacultad, nombreActividad, inicio, finall,op,idActividadDelete;
                    final int nombreUpdate,inicioUpdate,finalUpdate;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/actividad.php";
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
                                    setOk(true);
                                    //Toast.makeText(context, "Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "Actividad Sincronizada", Toast.LENGTH_SHORT).show();

                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"
                                            , Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Hay un problema con la base de datos remota."
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

                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    //final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    idActividadDelete = finalReceiveString.indexOf(finalReceiveString.length()-1,op+1);
                    idActividad = finalReceiveString.indexOf(";",op+1);

                    idFacultad = finalReceiveString.indexOf(";", idActividad + 1);
                    nombreActividad = finalReceiveString.indexOf(";", idFacultad + 1);
                    inicio = finalReceiveString.indexOf(";", nombreActividad + 1);
                    //finall = finalReceiveString.indexOf("\n", inicio + 1);

                    nombreUpdate = finalReceiveString.indexOf(";",idActividad+1);
                    inicioUpdate = finalReceiveString.indexOf(";",nombreUpdate+1);
                    //finalUpdate = finalReceiveString.indexOf("\n",inicioUpdate+1);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<>();
                            params.put("operacion", operacion);
                            if (operacion.equals("delete")){
                                params.put("idActividad", finalReceiveString.substring(op+1, finalReceiveString.length()));
                            }else{
                                params.put("idActividad", finalReceiveString.substring(op+1, idActividad));
                            }
                            if (operacion.equals("insert")){
                                params.put("idFacultad", finalReceiveString.substring(idActividad + 1, idFacultad));
                                params.put("nombreActividad", finalReceiveString.substring(idFacultad + 1, nombreActividad));
                                params.put("inicio", finalReceiveString.substring(nombreActividad + 1, inicio));
                                params.put("final", finalReceiveString.substring(inicio + 1, finalReceiveString.length()));
                            }else if (operacion.equals("update")){
                                params.put("nombreActividad", finalReceiveString.substring(idActividad + 1, nombreUpdate));
                                params.put("inicio", finalReceiveString.substring(nombreUpdate + 1, inicioUpdate));
                                params.put("final", finalReceiveString.substring(inicioUpdate + 1, finalReceiveString.length()));
                            }
                            /*params.put("idFacultad", finalReceiveString.substring(idActividad + 1, idFacultad));
                            params.put("nombreActividad", finalReceiveString.substring(idFacultad + 1, nombreActividad));
                            params.put("inicio", finalReceiveString.substring(nombreActividad + 1, inicio));
                            params.put("final", finalReceiveString.substring(inicio + 1, finalReceiveString.length()));*/
                            //Toast.makeText(context,params.toString(),Toast.LENGTH_SHORT).show();
                            /*Toast.makeText(context, finalReceiveString.substring(0, idActividad)
                                            + finalReceiveString.substring(idActividad + 1, idFacultad)
                                            + finalReceiveString.substring(idFacultad + 1, nombreActividad)
                                            + finalReceiveString.substring(nombreActividad + 1, inicio)
                                            + finalReceiveString.substring(inicio + 1, finalReceiveString.length())
                                    , Toast.LENGTH_SHORT).show();*/
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFileTramite(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int idTramite, idFacultad, nombreTramite,op,idTramiteDelete;

                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/tramite.php";
                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");

                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    //Toast.makeText(context, "Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "Tramite Sindronizado", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"
                                            , Toast.LENGTH_SHORT).show();
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
                                            + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    };

                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    idTramite = finalReceiveString.indexOf(";",op+1);
                    idTramiteDelete = finalReceiveString.indexOf(finalReceiveString.length()-1,op+1);
                    idFacultad = finalReceiveString.indexOf(";", idTramite + 1);
                    nombreTramite = finalReceiveString.indexOf("\n", idFacultad + 1);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<>();
                            params.put("operacion", operacion);
                            if (operacion.equals("delete")){
                                params.put("idTramite", finalReceiveString.substring(op+1, finalReceiveString.length()));
                            }else{
                                params.put("idTramite", finalReceiveString.substring(op+1, idTramite));
                            }
                            params.put("idFacultad", finalReceiveString.substring(idTramite + 1, idFacultad));
                            params.put("nombreTramite", finalReceiveString.substring(idFacultad + 1, finalReceiveString.length()));
                            //Toast.makeText(context,params.toString(),Toast.LENGTH_SHORT).show();
                            /*Toast.makeText(context, finalReceiveString.substring(0, idTramite)
                                            + finalReceiveString.substring(idTramite + 1, idFacultad)
                                            + finalReceiveString.substring(idFacultad + 1, finalReceiveString.length())
                                    , Toast.LENGTH_SHORT).show();*/
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFilePersonal(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int op,idPersonal, nombrePersonal,idPersonalDelete;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/personal.php";
                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    //Toast.makeText(context, "Sincronizado" + response.toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "Personal Sincronizado", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"+response.toString()
                                            , Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota."
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
                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    idPersonal = finalReceiveString.indexOf(";",op+1);
                    idPersonalDelete = finalReceiveString.indexOf(finalReceiveString.length()-1,op+1);
                    nombrePersonal = finalReceiveString.indexOf("\n", idPersonal + 1);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("operacion", operacion);
                            if (operacion.equals("delete")){
                                params.put("idPersonal", finalReceiveString.substring(op+1,finalReceiveString.length()));
                            }else{
                                params.put("idPersonal", finalReceiveString.substring(0, idPersonal));
                            }
                            params.put("nombrePersonal", finalReceiveString.substring(idPersonal + 1, finalReceiveString.length()));
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("AgregarPerdonal", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarPerdonal", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFileCargo(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int op,idCargo, nombreCargo;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/cargo.php";
                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    //Toast.makeText(context, "Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "Cargo Sincronizado", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"+response.toString()
                                            , Toast.LENGTH_SHORT).show();
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
                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    idCargo = finalReceiveString.indexOf(";",op+1);
                    nombreCargo = finalReceiveString.indexOf(finalReceiveString.length()-1,idCargo+1);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("operacion", operacion);
                            params.put("idCargo", finalReceiveString.substring(op + 1, idCargo));
                            params.put("nombreCargo", finalReceiveString.substring(idCargo + 1, finalReceiveString.length()));
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("AgregarPerdonal", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarPerdonal", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFilePersonalCargo(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int op,idPersonal, idCargo;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/personalCargo.php";
                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    //Toast.makeText(context, "Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "Relacion PersonalCargo Sincronizado", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"+response.toString()
                                            , Toast.LENGTH_SHORT).show();
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
                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    idPersonal = finalReceiveString.indexOf(";",op+1);
                    idCargo = finalReceiveString.indexOf(finalReceiveString.length()-1,idPersonal+1);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("operacion", operacion);
                            params.put("idPersonal", finalReceiveString.substring(op + 1, idPersonal));
                            params.put("idCargo", finalReceiveString.substring(idPersonal + 1, finalReceiveString.length()));
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("PersonalCargo", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("PesonalCargo", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFileUnidadAdmin(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int op,idUnidad, idFacultad, nombreUnidad;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/unidadAdmin.php";
                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    //Toast.makeText(context, "Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "Unidad Administrativa Sincronizada", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"+response.toString()
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
                                            "Estamos trabajando para corregirlo."
                                    //+ error.toString(), Toast.LENGTH_SHORT).show();
                                    , Toast.LENGTH_SHORT).show();
                        }
                    };
                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    idUnidad = finalReceiveString.indexOf(";",op+1);
                    idFacultad = finalReceiveString.indexOf(";",idUnidad+1);
                    nombreUnidad = finalReceiveString.indexOf(finalReceiveString.length()-1,idFacultad+1);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("operacion", operacion);
                            params.put("idUnidad", finalReceiveString.substring(op + 1, idUnidad));
                            params.put("idFacultad", finalReceiveString.substring(idUnidad + 1, idFacultad));
                            params.put("nombreUnidadAdmin", finalReceiveString.substring(idFacultad + 1, finalReceiveString.length()));
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("AgregarPerdonal", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarPerdonal", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFilePersonalUnidadAdmin(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int op,idPersonal, idUnidadAdmin,idUnidadAdminDelete;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/personalUnidadAdmin.php";
                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    //Toast.makeText(context, "Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "Relacion Personal Unidad Administrativa Sincronizado", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"+response.toString()
                                            , Toast.LENGTH_SHORT).show();
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
                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    idPersonal = finalReceiveString.indexOf(";",op+1);
                    idUnidadAdmin = finalReceiveString.indexOf(finalReceiveString.length()-1,idPersonal+1);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("operacion", operacion);
                            params.put("idPersonal", finalReceiveString.substring(op + 1, idPersonal));
                            params.put("idUnidad", finalReceiveString.substring(idPersonal + 1, finalReceiveString.length()));
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("PersonalCargo", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("PesonalCargo", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFileUbicacion(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int op,idUbicacion, longitud, latitud,altitud,descripcion;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/ubicacion.php";
                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    //Toast.makeText(context, "Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "Ubicacion Sincronizada", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"+response.toString()
                                            , Toast.LENGTH_SHORT).show();
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
                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    idUbicacion = finalReceiveString.indexOf(";",op+1);
                    longitud = finalReceiveString.indexOf(";",idUbicacion+1);
                    latitud = finalReceiveString.indexOf(";",longitud+1);
                    altitud = finalReceiveString.indexOf(";",latitud+1);
                    descripcion = finalReceiveString.indexOf(finalReceiveString.length()-1,altitud+1);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("operacion", operacion);
                            params.put("idUbicacion", finalReceiveString.substring(op + 1, idUbicacion));
                            params.put("longitud", finalReceiveString.substring(idUbicacion + 1, longitud));
                            params.put("latitud", finalReceiveString.substring(longitud + 1, latitud));
                            params.put("altitud", finalReceiveString.substring(latitud + 1, altitud));
                            params.put("componenteTematica", finalReceiveString.substring(altitud + 1, finalReceiveString.length()));
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("AgregarPerdonal", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarPerdonal", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFilePersonalUbicacion(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int op,idPersonal, idUbicacion,idUnidadAdminDelete;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/personalUbicacion.php";
                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    //Toast.makeText(context, "Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "Relacion PersonalUbicacion Sincronizado", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"+response.toString()
                                            , Toast.LENGTH_SHORT).show();
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
                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    idPersonal = finalReceiveString.indexOf(";",op+1);
                    idUbicacion = finalReceiveString.indexOf(finalReceiveString.length()-1,idPersonal+1);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("operacion", operacion);
                            params.put("idPersonal", finalReceiveString.substring(op + 1, idPersonal));
                            params.put("idUbicacion", finalReceiveString.substring(idPersonal + 1, finalReceiveString.length()));
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("PersonalCargo", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("PesonalCargo", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFilePaso(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int op,idPaso, idPersonalCreate,idPersonalUpdate,
                            idTramite,descripcionPasoCreate,descripcionPasoUpdate,porcentajeCreate,porcentajeUpdate;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/paso.php";
                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    //Toast.makeText(context, "Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "Paso Sincronizado", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"+response.toString()
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
                                    , Toast.LENGTH_SHORT).show();
                            //+ error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    };
                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    idPersonalCreate = finalReceiveString.indexOf(";",op+1);
                    idTramite = finalReceiveString.indexOf(";",idPersonalCreate+1);
                    descripcionPasoCreate = finalReceiveString.indexOf(";",idTramite+1);
                    porcentajeCreate = finalReceiveString.indexOf(finalReceiveString.length()-1,descripcionPasoCreate+1);

                    idPaso = finalReceiveString.indexOf(";",op+1);
                    idPersonalUpdate = finalReceiveString.indexOf(";",idPaso+1);
                    descripcionPasoUpdate = finalReceiveString.indexOf(";",idPersonalUpdate+1);
                    porcentajeUpdate = finalReceiveString.indexOf(finalReceiveString.length()-1,descripcionPasoUpdate+1);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            String mIdPaso = finalReceiveString.substring(op+1,idPaso);
                            String mIdPersonal = finalReceiveString.substring(idPaso+1,idPersonalUpdate);
                            String mDescripcion = finalReceiveString.substring(idPersonalUpdate+1,descripcionPasoUpdate);
                            String mPorcentaje = finalReceiveString.substring(descripcionPasoUpdate+1,finalReceiveString.length()-1);
                            params.put("operacion", operacion);
                            if (operacion.equals("update") ){
                                params.put("idPaso",mIdPaso);
                                params.put("idPersonal",mIdPersonal);
                                params.put("descripcionPaso",mDescripcion);
                                params.put("porcentaje",mPorcentaje);
                            }else if (operacion.equals("delete")){
                                params.put("idPaso",finalReceiveString.substring(op+1,finalReceiveString.length()));
                            }else if (operacion.equals("create")){
                                params.put("idPersonal", finalReceiveString.substring(op + 1, idPersonalCreate));
                                params.put("idTramite", finalReceiveString.substring(idPersonalCreate + 1, idTramite));
                                params.put("descripcionPaso", finalReceiveString.substring(idTramite + 1, descripcionPasoCreate));
                                params.put("porcentaje", finalReceiveString.substring(descripcionPasoCreate + 1, finalReceiveString.length()));
                            }

                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("AgregarPerdonal", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarPerdonal", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFileRequisito(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int op,idRequisito, descripcion;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/requisito.php";
                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    Toast.makeText(context, "Requisito Sincronizado", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(context, "Requisito Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"+response.toString()
                                            , Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "HHay un problema con la base de datos."
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
                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    idRequisito = finalReceiveString.indexOf(";",op+1);
                    descripcion = finalReceiveString.indexOf(finalReceiveString.length()-1,idRequisito+1);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            String mIdRequisitoDelete = finalReceiveString.substring(op + 1, finalReceiveString.length()-1);
                            String mIdRequisito = finalReceiveString.substring(op + 1, idRequisito);
                            params.put("operacion", operacion);
                            if (operacion.equals("delete")){
                                params.put("idRequisito", mIdRequisitoDelete);
                            }else {
                                params.put("idRequisito", mIdRequisito);
                                params.put("descripcion", finalReceiveString.substring(idRequisito + 1, finalReceiveString.length()));
                            }
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("AgregarPerdonal", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarPerdonal", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFileRequisitoTramite(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int op,idRequisito, idTramite;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/requisitoTramite.php";
                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    Toast.makeText(context, "Relacion RequisitoTramite Sincronizado", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(context, "RequisitoTramite Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"+response.toString()
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
                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    idRequisito = finalReceiveString.indexOf(";",op+1);
                    idTramite = finalReceiveString.indexOf(finalReceiveString.length()-1,idRequisito+1);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            String mIdRequisito = finalReceiveString.substring(op + 1, idRequisito);
                            String mIdTramite= finalReceiveString.substring(idRequisito + 1, finalReceiveString.length());
                            params.put("operacion", operacion);
                            params.put("idRequisito", mIdRequisito);
                            params.put("idTramite", mIdTramite);
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("PersonalCargo", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("PesonalCargo", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFileDocumento(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int op,idDocumento, url,nombreDocumento,idDocumentoDelete;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/documento.php";

                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    Toast.makeText(context, "Documento Sincronizado", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(context, "Requisito Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"+response.toString()
                                            , Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "HHay un problema con la base de datos."
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

                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    idDocumento = finalReceiveString.indexOf(";",op+1);
                    idDocumentoDelete = finalReceiveString.indexOf(finalReceiveString.length(),op+1);
                    url = finalReceiveString.indexOf(";",idDocumento+1);
                    //nombreDocumento = finalReceiveString.indexOf(finalReceiveString.length(),url+1);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            String mIdDocumento = "";
                            String mUrl = "";
                            String mNombreDocumento = "";
                            if (operacion.equals("create") || operacion.equals("update")){
                                mIdDocumento = finalReceiveString.substring(op+1,idDocumento);
                                mUrl = finalReceiveString.substring(idDocumento+1,url);
                                mNombreDocumento = finalReceiveString.substring(url + 1, finalReceiveString.length());
                            }
                            String mIdDocumentoDelete = finalReceiveString.substring(op + 1, finalReceiveString.length());
                            params.put("operacion", operacion);
                            if (operacion.equals("delete")){
                                params.put("idDocumento", mIdDocumentoDelete);
                            }else if (operacion.equals("create") || operacion.equals("update")) {
                                params.put("idDocumento", mIdDocumento);
                                params.put("url", mUrl);
                                params.put("nombreDocumento", mNombreDocumento);
                            }
                            //Toast.makeText(context, "mIddon"+mIdDocumentoDelete, Toast.LENGTH_SHORT).show();
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("AgregarPerdonal", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarPerdonal", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFileDocumentoTramite(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int op,idDocumento, idTramite;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/documentoTramite.php";
                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    Toast.makeText(context, "Relacion DocumentoTramite Sincronizado" , Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(context, "DocumentoTramite Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"+response.toString()
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
                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    idDocumento = finalReceiveString.indexOf(";",op+1);
                    idTramite = finalReceiveString.indexOf(finalReceiveString.length()-1,idDocumento+1);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            String mIdRequisito = finalReceiveString.substring(op + 1, idDocumento);
                            String mIdTramite= finalReceiveString.substring(idDocumento + 1, finalReceiveString.length());
                            params.put("operacion", operacion);
                            params.put("idDocumento", mIdRequisito);
                            params.put("idTramite", mIdTramite);
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("DocumentoTramite", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("DocumentoTramite", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFileActividadTramite(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int op,idRequisito, idTramite;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/actividadTramite.php";
                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    Toast.makeText(context, "Relacion ActividadTramite Sincronizado", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(context, "ActividadTramite Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"+response.toString()
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
                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    idRequisito = finalReceiveString.indexOf(";",op+1);
                    idTramite = finalReceiveString.indexOf(finalReceiveString.length()-1,idRequisito+1);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            String mIdActividad = finalReceiveString.substring(op + 1, idRequisito);
                            String mIdTramite= finalReceiveString.substring(idRequisito + 1, finalReceiveString.length());
                            params.put("operacion", operacion);
                            params.put("idActividad", mIdActividad);
                            params.put("idTramite", mIdTramite);
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("PersonalCargo", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("PesonalCargo", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

    public String readFromFileAccesoUsuario(final Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                    final int op,usuario, opcion;
                    //------------------------------------------------------------------------------
                    String ip = context.getString(R.string.IP);
                    String URL = ip + "/TRUES/accesoUsuario.php";
                    Response.Listener responseListener = new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject((String) response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    //JSONObject datos = jsonObject.getJSONObject("result");
                                    Toast.makeText(context, "AccesoUsuario Sincronizado", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(context, "Guardado" + response.toString(), Toast.LENGTH_SHORT).show();
                                } else if (status.equals("err")) {
                                    Toast.makeText(context, "Parece que alguien ya subió este cambio a la base remota"+response.toString()
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
                    final String finalReceiveString = receiveString;
                    final String operacion = finalReceiveString.substring(0,finalReceiveString.indexOf(";"));
                    op = finalReceiveString.indexOf(";",0);
                    usuario = finalReceiveString.indexOf(";",op+1);
                    //nombreCargo = finalReceiveString.indexOf(finalReceiveString.length()-1,idCargo+1);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            responseListener, ErrorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("operacion", operacion);
                            params.put("usuario", finalReceiveString.substring(op + 1, usuario));
                            params.put("idOption", finalReceiveString.substring(usuario + 1, finalReceiveString.length()));
                            return params;
                        }
                    };
                    VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("AgregarPerdonal", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarPerdonal", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }
}
