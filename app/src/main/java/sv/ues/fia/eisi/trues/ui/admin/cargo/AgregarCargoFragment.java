package sv.ues.fia.eisi.trues.ui.admin.cargo;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import sv.ues.fia.eisi.trues.db.control.CargoControl;
import sv.ues.fia.eisi.trues.db.control.PersonalCargoControl;
import sv.ues.fia.eisi.trues.db.control.PersonalUnidadAdminControl;
import sv.ues.fia.eisi.trues.db.control.UnidadAdminControl;
import sv.ues.fia.eisi.trues.db.entity.Cargo;
import sv.ues.fia.eisi.trues.db.entity.UnidadAdmin;

public class AgregarCargoFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private Context context;
    private EditText editTextCargo;
    private Spinner spinnerCargo;
    private Button buttonGuardar, buttonCancelar;
    private Integer idPersonal;
    private List<Cargo> cargoList;
    private List<String> cargos;
    private PersonalCargoControl control;
    private CargoControl cargoControl;

    public static AgregarCargoFragment newInstance() {
        return new AgregarCargoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idPersonal = getArguments().getInt("idPersonal", -1);
        control = new PersonalCargoControl(context);

        cargoControl = new CargoControl(context);
        cargoList = cargoControl.ObtenerCargos();

        cargos = new ArrayList<>();
        cargos.add(getText(R.string.seleccionar_cargo).toString());
        for (int i = 0; i<cargoList.size(); i++){
            cargos.add(cargoList.get(i).getNombreCargo());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, cargos);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_agregar_cargo, null);
        editTextCargo = view.findViewById(R.id.editTextCargo);
        spinnerCargo = view.findViewById(R.id.spinnerCargo);
        spinnerCargo.setAdapter(adapter);
        spinnerCargo.setOnItemSelectedListener(this);

        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cargo = editTextCargo.getText().toString();

                if (!cargo.isEmpty()){
                    Integer idCargo = cargoControl.CrearCargo(cargo);
                    if (idCargo!=null){
                        if(verificarInternet()){
                            cargoControl.CrearCargoWS(idCargo,cargo);
                        }else {
                            String parametros = "create;"+idCargo.toString()
                                    + ";" + cargo;
                            writeToFile(parametros, context,"Cargo");
                        }
                        control.crearPersonalCargo(idPersonal, idCargo);
                        if(verificarInternet()){
                            control.crearPersonalCargoWS(idPersonal, idCargo);
                        }else {
                            String parametros = "create;"+idPersonal.toString()
                                    + ";" + idCargo.toString();
                            writeToFile(parametros, context,"PersonalCargo");
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
            control.crearPersonalCargo(idPersonal, cargoList.get(position-1).getIdCargo());
            this.dismiss();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

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
            Log.e("AgregarCargoFragment", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarCargoFragment", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

}