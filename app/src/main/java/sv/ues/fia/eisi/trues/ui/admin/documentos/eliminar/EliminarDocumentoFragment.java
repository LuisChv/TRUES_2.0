package sv.ues.fia.eisi.trues.ui.admin.documentos.eliminar;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

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
import sv.ues.fia.eisi.trues.db.control.DocumentoControl;
import sv.ues.fia.eisi.trues.db.control.DocumentoTramiteControl;
import sv.ues.fia.eisi.trues.db.entity.Documento;

public class EliminarDocumentoFragment extends DialogFragment {

    private View view;
    private Context context;
    private Integer idTramite;
    private List<Documento> documentoList;
    private List<String> documentos;
    private Spinner spinnerDocumento;

    public static EliminarDocumentoFragment newInstance() {
        return new EliminarDocumentoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idTramite = getArguments().getInt("idTramite", -1);

        final DocumentoControl documentoControl = new DocumentoControl(context);
        documentoList = documentoControl.consultarDocumentos(idTramite);

        documentos = new ArrayList<>();
        documentos.add(getText(R.string.seleccionar_documento).toString());
        for (int i = 0; i<documentoList.size(); i++){
            documentos.add(documentoList.get(i).getNombreDocumento());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, documentos);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_eliminar_documento, null);
        spinnerDocumento = view.findViewById(R.id.spinnerDocumento);
        spinnerDocumento.setAdapter(adapter);
        spinnerDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    DocumentoTramiteControl control = new DocumentoTramiteControl(context);
                    control.eliminarDocumentoTramite(documentoList.get(position-1).getIdDocumento(), idTramite);
                    if (verificarInternet()) {
                        control.eliminarDocumentoTramiteWS(documentoList.get(position-1).getIdDocumento(),idTramite);
                    } else {
                        String idDocumento = String.valueOf(documentoList.get(position-1).getIdDocumento());
                        String parametros = "delete;"+idDocumento+";"+idTramite.toString();
                        writeToFile(parametros, context,"DocumentoTramite");
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

    private String readFromFile(Context context, String tabla) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(tabla+".txt");
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
            Log.e("AgregarTramiteFragment", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarTramiteFragment", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }


}
