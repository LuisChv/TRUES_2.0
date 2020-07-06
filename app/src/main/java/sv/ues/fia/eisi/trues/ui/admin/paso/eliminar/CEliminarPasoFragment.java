package sv.ues.fia.eisi.trues.ui.admin.paso.eliminar;

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
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.PasoControl;
import sv.ues.fia.eisi.trues.db.entity.Paso;
import sv.ues.fia.eisi.trues.ui.global.paso.lista.PasoFragment;

public class CEliminarPasoFragment extends DialogFragment {

    public static CEliminarPasoFragment newInstance() {
        return new CEliminarPasoFragment();
    }

    private View view;
    private Context context;
    private Integer idPaso;
    private Button buttonEliminar, buttonCancelar;
    private Paso paso;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idPaso = getArguments().getInt("idPaso", -1);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_eliminar_tramite, null);
        buttonEliminar = view.findViewById(R.id.buttonEliminar);
        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasoControl control = new PasoControl(getActivity());
                paso = control.obtenerPaso(idPaso);

                control.eliminarPaso(idPaso);
                if(verificarInternet()){
                    control.eliminarPasoWS(idPaso);
                }else {
                    String parametros = "delete;"+idPaso.toString();
                    writeToFile(parametros, context,"Paso");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("idTramite", paso.getIdTramite());
                PasoFragment pasoFragment = new PasoFragment();
                pasoFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, pasoFragment).addToBackStack(null).commit();
                dismiss();
            }
        });
        buttonCancelar = view.findViewById(R.id.buttonCancelar);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }
    private boolean verificarInternet() {
        Boolean HayInternet;
        if (isOnlineNet()) {
            HayInternet = true;
        } else {
            HayInternet = false;
        }
        return HayInternet;
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
            Log.e("AgregarUbicacionFragmnt", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarUbicacionFragmnt", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }
}
