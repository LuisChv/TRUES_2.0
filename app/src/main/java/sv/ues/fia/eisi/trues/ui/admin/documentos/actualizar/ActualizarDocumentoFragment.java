package sv.ues.fia.eisi.trues.ui.admin.documentos.actualizar;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.DocumentoControl;
import sv.ues.fia.eisi.trues.db.entity.Documento;
import sv.ues.fia.eisi.trues.ui.global.documento.lista.DocumentoFragment;

public class ActualizarDocumentoFragment extends DialogFragment implements View.OnClickListener {
    private View view;
    private Context context;
    private EditText editTextNombre, editTextURL;
    private Integer idDocumento;
    private Documento documento;
    private DocumentoControl documentoControl;
    private TextView textView;
    private Button buttonGuardar, buttonCancelar;
    private Integer idTramite;
    private Spinner spinnerDocumento;
    private ImageView imageView;

    public static ActualizarDocumentoFragment newInstance() {
        return new ActualizarDocumentoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        documentoControl = new DocumentoControl(context);

        idDocumento = getArguments().getInt("idDocumento", -1);
        documento = documentoControl.consultarDocumeto(idDocumento);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_agregar_documento, null);
        textView = view.findViewById(R.id.textView12);
        textView.setText(getText(R.string.actualizar_documento));
        editTextNombre = view.findViewById(R.id.editTextDocumento);
        editTextNombre.setText(documento.getNombreDocumento());
        editTextURL = view.findViewById(R.id.editTextURL);
        editTextURL.setText(documento.getUrl());
        imageView = view.findViewById(R.id.imageView14);
        imageView.setVisibility(View.GONE);

        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(this);

        buttonCancelar = view.findViewById(R.id.buttonCancelar);
        buttonCancelar.setOnClickListener(this);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonGuardar:
                String nombre = editTextNombre.getText().toString();
                String url = editTextURL.getText().toString();
                if (!nombre.isEmpty() && !url.isEmpty()){
                    documentoControl.ActualizarDocumento(idDocumento, url, nombre);
                    if (verificarInternet()) {
                        documentoControl.actualizarDocumentoWS(idDocumento, url, nombre);
                    } else {
                        //String nombreTramite = editTextTramite.getText().toString();
                        String parametros = "update;"+idDocumento.toString()
                                + ";" + url
                                + ";" + nombre;
                        writeToFile(parametros, context,"Documento");
                    }

                    Bundle bundle = new Bundle();
                    bundle.putInt("idTramite", getArguments().getInt("idTramite", -1));

                    DocumentoFragment fragment = new DocumentoFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainContainer, fragment).addToBackStack(null)
                            .commit();
                    dismiss();
                } else {
                    Toast.makeText(context.getApplicationContext(), getText(R.string.datos_incorrectos), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonCancelar:
                dismiss();
                break;

        }
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
