package sv.ues.fia.eisi.trues.ui.admin.requisito.eliminar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
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
import sv.ues.fia.eisi.trues.db.control.RequisitoControl;
import sv.ues.fia.eisi.trues.db.control.RequisitoTramiteControl;
import sv.ues.fia.eisi.trues.db.entity.Requisito;

public class EliminarRequisitoFragment extends DialogFragment {

    private View view;
    private Context context;
    private Integer idTramite;
    private List<Requisito> requisitoList;
    private List<String> requisitos;
    private Spinner spinnerRequisito;

    public static EliminarRequisitoFragment newInstance() {
        return new EliminarRequisitoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idTramite = getArguments().getInt("idTramite", -1);

        final RequisitoControl requisitoControl = new RequisitoControl(context);
        requisitoList = requisitoControl.consultarRequisitos(idTramite);

        requisitos = new ArrayList<>();
        requisitos.add(getText(R.string.seleccionar_requisito).toString());
        for (int i = 0; i<requisitoList.size(); i++){
            requisitos.add(requisitoList.get(i).getDescripcion());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, requisitos);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_eliminar_requisito, null);
        spinnerRequisito = view.findViewById(R.id.spinnerRequisito);
        spinnerRequisito.setAdapter(adapter);
        spinnerRequisito.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    RequisitoTramiteControl control = new RequisitoTramiteControl(context);
                    control.eliminarRequisitoTramite(requisitoList.get(position-1).getIdRequisito(), idTramite);
                    if(verificarInternet()){
                        control.eliminarRequisitoTramiteWS(requisitoList.get(position-1).getIdRequisito(), idTramite);
                    }else {
                        String idRequisito = String.valueOf(requisitoList.get(position-1).getIdRequisito());
                        String parametros = "delete;"+idRequisito+";"+idTramite.toString();
                        writeToFile(parametros, context,"RequisitoTramite");
                    }
                    dismiss();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        builder.setView(view);
        return builder.create();
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
            Log.e("EliminarRequisitoFrgmnt", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("EliminarRequisitoFrgmnt", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }
}
