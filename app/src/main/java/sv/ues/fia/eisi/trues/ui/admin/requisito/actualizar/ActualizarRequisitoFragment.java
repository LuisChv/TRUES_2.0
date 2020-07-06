package sv.ues.fia.eisi.trues.ui.admin.requisito.actualizar;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.PasoControl;
import sv.ues.fia.eisi.trues.db.control.PersonalControl;
import sv.ues.fia.eisi.trues.db.control.RequisitoControl;
import sv.ues.fia.eisi.trues.db.entity.Paso;
import sv.ues.fia.eisi.trues.db.entity.Requisito;
import sv.ues.fia.eisi.trues.ui.global.paso.lista.PasoFragment;
import sv.ues.fia.eisi.trues.ui.global.requisito.RequisitosFragment;

public class ActualizarRequisitoFragment extends DialogFragment {
    private View view;
    private Context context;
    private EditText editTextDescripcion;
    private Integer idRequisito;
    private Requisito requisito;
    private RequisitoControl requisitoControl;
    private TextView textView;
    private Button buttonGuardar, buttonCancelar;
    private Integer idTramite;
    private Spinner spinnerRequisito;
    private ImageView imageView;

    public static ActualizarRequisitoFragment newInstance() {
        return new ActualizarRequisitoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        requisitoControl = new RequisitoControl(context);

        idRequisito = getArguments().getInt("idRequisito", -1);
        requisito = requisitoControl.consultarRequisito(idRequisito);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_agregar_requisitos, null);
        textView = view.findViewById(R.id.textView12);
        textView.setText(getText(R.string.actualizar_requisito));
        editTextDescripcion = view.findViewById(R.id.editTextRequisito);
        editTextDescripcion.setText(requisito.getDescripcion());
        spinnerRequisito = view.findViewById(R.id.spinnerRequisito);
        spinnerRequisito.setVisibility(View.GONE);
        imageView = view.findViewById(R.id.imageView14);
        imageView.setVisibility(View.GONE);

        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descripcion = editTextDescripcion.getText().toString();
                if (!descripcion.isEmpty()){
                    requisitoControl.actualizarRequisito(idRequisito, descripcion);
                    if (verificarInternet()) {
                        requisitoControl.actualizarRequisitoWS(idRequisito, descripcion);
                    } else {
                        //String idPersonall = String.valueOf(personalList.get(idPersonal-1).getIdPersonal());
                        String parametros = "update;" +idRequisito.toString()
                                + ";" + descripcion;
                        writeToFile(parametros, context, "Requisito");
                    }

                    Bundle bundle = new Bundle();
                    bundle.putInt("idTramite", getArguments().getInt("idTramite", -1));

                    RequisitosFragment fragment = new RequisitosFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainContainer, fragment).addToBackStack(null)
                            .commit();
                    dismiss();
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
            Log.e("AgregarRequisitoFragmnt", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarRequisitoFragmnt", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

}
