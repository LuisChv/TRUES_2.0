package sv.ues.fia.eisi.trues.ui.admin.tramite.actualizar;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.TramiteControl;
import sv.ues.fia.eisi.trues.db.entity.Tramite;
import sv.ues.fia.eisi.trues.ui.admin.documentos.agregar.AgregarDocumentoFragment;
import sv.ues.fia.eisi.trues.ui.admin.documentos.eliminar.EliminarDocumentoFragment;
import sv.ues.fia.eisi.trues.ui.admin.paso.agregar.AgregarPasosFragment;
import sv.ues.fia.eisi.trues.ui.admin.paso.eliminar.EliminarPasoFragment;
import sv.ues.fia.eisi.trues.ui.admin.requisito.agregar.AgregarRequisitosFragment;
import sv.ues.fia.eisi.trues.ui.admin.requisito.eliminar.EliminarRequisitoFragment;
import sv.ues.fia.eisi.trues.ui.global.MenuAdminActivity;
import sv.ues.fia.eisi.trues.ui.global.tramite.lista.TramitesFragment;

public class ActualizarTramiteFragment extends DialogFragment implements View.OnClickListener {

    private View view;
    private Context context;
    private TextView textView;
    private EditText editTextTramite;
    private CardView agregarPaso, quitarPaso, agregarRequisito, quitarRequisito, agregarDocumento, quitarDocumento;
    private CardView cardView7, cardView8, cardView9;
    private Button buttonCancelar, buttonGuardar;
    private SharedPreferences sharedPreferences;
    private Integer idFacultad;
    private Integer idTramite;
    private Tramite tramite;
    private TramiteControl tramiteControl;

    public static ActualizarTramiteFragment newInstance() {
        return new ActualizarTramiteFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        idFacultad = sharedPreferences.getInt("facultad", -1);

        idTramite = getArguments().getInt("idTramite", -1);
        tramiteControl = new TramiteControl(context);
        tramite = tramiteControl.consultarTramite(idTramite);

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_agregar_tramite, null);
        textView = view.findViewById(R.id.textView);
        editTextTramite = view.findViewById(R.id.editTextTramite);
        editTextTramite.setText(tramite.getNombreTramite());
        cardView7 = view.findViewById(R.id.cardView7);
        cardView8 = view.findViewById(R.id.cardView8);
        cardView9 = view.findViewById(R.id.cardView9);
        agregarDocumento = view.findViewById(R.id.agregarDocumento);
        agregarPaso = view.findViewById(R.id.agregarPaso);
        agregarRequisito = view.findViewById(R.id.agregarRequisito);
        quitarDocumento = view.findViewById(R.id.quitarDocumento);
        quitarPaso = view.findViewById(R.id.quitarPaso);
        quitarRequisito = view.findViewById(R.id.quitarRequisito);
        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        buttonCancelar = view.findViewById(R.id.buttonCancelar);
        agregarDocumento.setOnClickListener(this);
        agregarPaso.setOnClickListener(this);
        agregarRequisito.setOnClickListener(this);
        quitarDocumento.setOnClickListener(this);
        quitarPaso.setOnClickListener(this);
        quitarRequisito.setOnClickListener(this);

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!editTextTramite.getText().toString().isEmpty()){

                            tramiteControl.actualizarTramite(idTramite, idFacultad, editTextTramite.getText().toString());
                            if (verificarInternet()) {
                                tramiteControl.actualizarTramiteWS(idTramite, idFacultad, editTextTramite.getText().toString());
                            } else {
                                String nombreTramite = editTextTramite.getText().toString();
                                String parametros = "update;" + idTramite.toString()
                                        + ";" + idFacultad.toString()
                                        + ";" + nombreTramite;
                                writeToFile(parametros, context);
                            }
                            dismiss();

                            getActivity().getSupportFragmentManager().
                                    beginTransaction().replace(R.id.mainContainer, new TramitesFragment()).commit();
                            ((MenuAdminActivity) context).setActionBarTitle(getText(R.string.title_tramites).toString());
                            dismiss();

                        } else {
                            Toast.makeText(context.getApplicationContext(), getText(R.string.datos_incorrectos), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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
    public void onClick(View v) {
        DialogFragment fragment;
        switch (v.getId()){
            case R.id.quitarPaso:
                fragment = new EliminarPasoFragment();
                showDialog(fragment);
                break;
            case R.id.quitarRequisito:
                fragment = new EliminarRequisitoFragment();
                showDialog(fragment);
                break;
            case R.id.quitarDocumento:
                fragment = new EliminarDocumentoFragment();
                showDialog(fragment);
                break;
            case R.id.agregarPaso:
                fragment = new AgregarPasosFragment();
                showDialog(fragment);
                break;
            case R.id.agregarRequisito:
                fragment = new AgregarRequisitosFragment();
                showDialog(fragment);
                break;
            case R.id.agregarDocumento:
                fragment = new AgregarDocumentoFragment();
                showDialog(fragment);
                break;
        }

    }

    private void showDialog(DialogFragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putInt("idTramite", idTramite);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragment.setArguments(bundle);
        fragment.show(fragmentManager, "dialog");
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

    private void writeToFile(String data, Context context) {
        //Toast.makeText(context,readFromFile(context),Toast.LENGTH_LONG).show();
        String prueba = readFromFile(context) + data;

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Tramite.txt", Context.MODE_PRIVATE));
            outputStreamWriter.append(prueba);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        String after = readFromFile(context);
        Toast.makeText(context, after, Toast.LENGTH_SHORT).show();
    }

    private String readFromFile(Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("Tramite.txt");
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
