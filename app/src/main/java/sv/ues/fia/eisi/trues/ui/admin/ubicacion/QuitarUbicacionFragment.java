package sv.ues.fia.eisi.trues.ui.admin.ubicacion;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import sv.ues.fia.eisi.trues.db.control.PersonalUbicacionControl;
import sv.ues.fia.eisi.trues.db.control.UbicacionControl;
import sv.ues.fia.eisi.trues.db.entity.Ubicacion;

public class QuitarUbicacionFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private Context context;
    private Spinner spinnerUbicacion;
    private Integer idPersonal;
    private List<Ubicacion> ubicacionList;
    private List<String> ubicaciones;
    private PersonalUbicacionControl control;
    private UbicacionControl ubicacionControl;

    public static QuitarUbicacionFragment newInstance() {
        return new QuitarUbicacionFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idPersonal = getArguments().getInt("idPersonal", -1);
        control = new PersonalUbicacionControl(context);

        ubicacionControl = new UbicacionControl(context);
        ubicacionList = ubicacionControl.ObtenerUbicaciones(idPersonal);

        ubicaciones = new ArrayList<>();
        ubicaciones.add(getText(R.string.seleccionar_ubicacion).toString());
        for (int i = 0; i<ubicacionList.size(); i++){
            ubicaciones.add(ubicacionList.get(i).getComponenteTematica());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, ubicaciones);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_quitar_ubicacion, null);
        spinnerUbicacion = view.findViewById(R.id.spinnerUbicacion);
        spinnerUbicacion.setAdapter(adapter);
        spinnerUbicacion.setOnItemSelectedListener(this);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position>0){
            control.eliminarPersonalUbicacion(idPersonal, ubicacionList.get(position-1).getIdUbicacion());
            if(verificarInternet()){
                control.eliminarPersonalUbicacionWS(idPersonal, ubicacionList.get(position-1).getIdUbicacion());
            }else {
                String idUbicacion = String.valueOf(ubicacionList.get(position-1).getIdUbicacion());
                String parametros = "delete;"+idPersonal.toString()
                        + ";" + idUbicacion;
                writeToFile(parametros, context,"PersonalUbicacion");
            }
            this.dismiss();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
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

    private void writeToFile(String data, Context context,String archivo) {
        //Toast.makeText(context,readFromFile(context),Toast.LENGTH_LONG).show();
        String prueba = readFromFile(context,archivo) + data;

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(archivo+".txt", Context.MODE_PRIVATE));
            outputStreamWriter.append(prueba);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        String after = readFromFile(context,archivo);
        Toast.makeText(context, after, Toast.LENGTH_SHORT).show();
    }

    private String readFromFile(Context context,String archivo) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(archivo+".txt");
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
            Log.e("QuitarUbicacionFragment", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("QuitarUbicacionFragment", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }
}