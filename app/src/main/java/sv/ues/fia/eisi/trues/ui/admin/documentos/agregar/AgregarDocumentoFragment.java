package sv.ues.fia.eisi.trues.ui.admin.documentos.agregar;

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
import android.widget.Button;
import android.widget.EditText;
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

public class AgregarDocumentoFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private Context context;
    private EditText editTextDocumento, editTextURL;
    private Spinner spinnerDocumento;
    private Button buttonGuardar, buttonCancelar;
    private Integer idTramite;
    private List<Documento> documentoList;
    private List<String> documentos;
    private DocumentoTramiteControl control;

    public static AgregarDocumentoFragment newInstance() {
        return new AgregarDocumentoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idTramite = getArguments().getInt("idTramite", -1);
        control = new DocumentoTramiteControl(context);

        final DocumentoControl documentoControl = new DocumentoControl(context);
        documentoList = documentoControl.consultarDocumentos();

        documentos = new ArrayList<>();
        documentos.add(getText(R.string.seleccionar_documento).toString());
        for (int i = 0; i<documentoList.size(); i++){
            documentos.add(documentoList.get(i).getNombreDocumento());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, documentos);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_agregar_documento, null);
        editTextDocumento = view.findViewById(R.id.editTextDocumento);
        editTextURL = view.findViewById(R.id.editTextURL);
        spinnerDocumento = view.findViewById(R.id.spinnerDocumento);
        spinnerDocumento.setAdapter(adapter);
        spinnerDocumento.setOnItemSelectedListener(this);

        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String documento = editTextDocumento.getText().toString();
                String url = editTextURL.getText().toString();

                if (!documento.isEmpty() && !url.isEmpty()){
                    Integer idDocumento = documentoControl.CrearDocumento(url, documento);
                    if (idDocumento!=null){
                        control.crearDocumentoTramite(idDocumento, idTramite);
                        if(verificarInternet()){
                            documentoControl.CrearDocumentoWS(idDocumento,url, documento);
                            control.crearDocumentoTramiteWS(idDocumento, idTramite);
                        }else {
                            //String nombreTramite = editTextTramite.getText().toString();
                            String parametrosDocumento = "create;"+idDocumento.toString()
                                    + ";" + url
                                    + ";" + documento;
                            String parametrosDocumentoTramite = "create;"+idDocumento.toString()+";"+idTramite.toString();
                            writeToFile(parametrosDocumento, context,"Documento");
                            writeToFile(parametrosDocumentoTramite, context,"DocumentoTramite");
                        }
                        dismiss();
                    }
                } else {
                    Toast.makeText(context.getApplicationContext(), getText(R.string.datos_incorrectos), Toast.LENGTH_SHORT).show();
                }

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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position>0){
            control.crearDocumentoTramite(documentoList.get(position-1).getIdDocumento(), idTramite);
            if(verificarInternet()){
                control.crearDocumentoTramiteWS(documentoList.get(position-1).getIdDocumento(), idTramite);
            }else {
                String idDocumento = String .valueOf(documentoList.get(position-1).getIdDocumento());
                String parametrosDocumentoTramite = "create;"+idDocumento+";"+idTramite.toString();
                writeToFile(parametrosDocumentoTramite, context,"DocumentoTramite");
            }
            dismiss();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

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
            Log.e("AgregarTramiteFragment", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarTramiteFragment", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }
}
