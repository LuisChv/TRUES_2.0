package sv.ues.fia.eisi.trues.ui.admin.calendario;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.ActividadTramiteControl;
import sv.ues.fia.eisi.trues.db.control.TramiteControl;
import sv.ues.fia.eisi.trues.db.entity.Tramite;

public class SeleccionarTramitesFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private Spinner spinnerTramite;
    private TramiteControl tramiteControl;
    private List<Tramite> tramiteList;
    private List<String> tramites;
    private SharedPreferences sharedPreferences;
    private Integer facultad;
    private Integer actividad;
    private Context context;
    private Boolean accion;

    public static SeleccionarTramitesFragment newInstance() {
        return new SeleccionarTramitesFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_seleccionar_tramites, null);
        spinnerTramite = view.findViewById(R.id.spinnerTramite);

        sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        facultad = sharedPreferences.getInt("facultad", -1);

        actividad = getArguments().getInt("idActividad", -1);
        accion = getArguments().getBoolean("accion", false); //TRUE = quitar

        tramiteControl = new TramiteControl(getActivity());
        if (accion){
            tramiteList = tramiteControl.obtenerTramiteActividad(actividad);
        } else {
            tramiteList = tramiteControl.ObtenerTramites(facultad);
        }

        tramites = new ArrayList<>();
        tramites.add(getText(R.string.seleccionar_tramite).toString());
        for (int i = 0; i<tramiteList.size(); i++){
            tramites.add(tramiteList.get(i).getNombreTramite());
        }

        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter(getActivity(), R.layout.style_spinner, tramites);
        spinnerTramite.setAdapter(arrayAdapter);
        spinnerTramite.setOnItemSelectedListener(this);

        builder.setView(view);
        // Create the AlertDialog object and return it

        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position>0){
            ActividadTramiteControl actividadTramiteControl = new ActividadTramiteControl(context);
            Integer tramite = tramiteList.get(position-1).getIdTramite();
            if (accion){
                actividadTramiteControl.EliminarActividadTramite(actividad, tramite);
                if(verificarInternet()){
                    actividadTramiteControl.EliminarActividadTramiteWS(actividad, tramite);
                }else {
                    //String idActividad = String.valueOf(mValues.get(position).getIdActividad());
                    String parametros = "delete;" + actividad.toString()+";"+tramite.toString();
                    writeToFile(parametros, context, "ActividadTramite");
                }
            }else {
                actividadTramiteControl.CrearActividadTramite(actividad, tramite);
                if(verificarInternet()){
                    actividadTramiteControl.CrearActividadTramiteWS(actividad, tramite);
                }else {
                    //String idActividad = String.valueOf(mValues.get(position).getIdActividad());
                    String parametros = "create;" + actividad.toString()+";"+tramite.toString();
                    writeToFile(parametros, context, "ActividadTramite");
                }
            }
            this.dismiss();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {    }

    private boolean verificarInternet() {
        Boolean HayInternet;
        if (isNetDisponible() && isOnlineNet()) {
            //Toast.makeText(context, "Hay internet y está conectado", Toast.LENGTH_SHORT).show();
            HayInternet = true;
        } else {
            //Toast.makeText(context, "No hay internet y está conectado", Toast.LENGTH_SHORT).show();
            HayInternet = false;
        }
        return HayInternet;
    }
    private boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }
    private boolean isOnlineNet() {
        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");
            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void writeToFile(String data, Context context, String archivo) {
        //Toast.makeText(context,readFromFile(context),Toast.LENGTH_LONG).show();
        String prueba = readFromFile(context, archivo) + data;

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(archivo + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.append(prueba);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        String after = readFromFile(context, archivo);
        Toast.makeText(context, after, Toast.LENGTH_SHORT).show();
    }

    private String readFromFile(Context context, String archivo) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(archivo + ".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("AgregarCargoFragment", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarCargoFragment", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

}
