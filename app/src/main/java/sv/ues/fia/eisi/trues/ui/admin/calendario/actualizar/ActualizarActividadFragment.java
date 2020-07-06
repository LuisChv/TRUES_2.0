package sv.ues.fia.eisi.trues.ui.admin.calendario.actualizar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.ActividadControl;
import sv.ues.fia.eisi.trues.db.control.FacultadControl;
import sv.ues.fia.eisi.trues.db.entity.Actividad;
import sv.ues.fia.eisi.trues.db.entity.Facultad;
import sv.ues.fia.eisi.trues.ui.admin.calendario.SeleccionarTramitesFragment;
import sv.ues.fia.eisi.trues.ui.global.DatePickerFragment;
import sv.ues.fia.eisi.trues.ui.global.calendario.lista.ActividadesFragment;

public class ActualizarActividadFragment extends DialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private View view;
    private EditText editTextNombreActividad, editTextInicio, editTextFin;
    private Actividad actividad;
    private FacultadControl facultadControl;
    private List<String> facultades;
    private List<Facultad> facultadList;
    private CardView cardView5, cardView6;
    private Context context;
    private Button buttonGuardar, buttonCancelar;
    private ActividadControl actividadControl;

    public static ActualizarActividadFragment newInstance() {
        return new ActualizarActividadFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        actividadControl = new ActividadControl(getActivity());
        actividad = actividadControl.ObtenerActividad(getArguments().getInt("actividad"));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_actualizar_actividad, null);
        editTextNombreActividad = view.findViewById(R.id.editTextNombreActividad);
        editTextNombreActividad.setText(actividad.getNombreActividad());
        editTextInicio = view.findViewById(R.id.editTextInicio);
        editTextInicio.setText(actividad.getInicio());
        editTextFin = view.findViewById(R.id.editTextFin);
        editTextFin.setText(actividad.getFin());
        cardView5 = view.findViewById(R.id.cardView5);
        cardView5.setOnClickListener(this);
        cardView6 = view.findViewById(R.id.cardView6);
        cardView6.setOnClickListener(this);
        editTextInicio.setOnClickListener(this);
        editTextFin.setOnClickListener(this);
        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextNombreActividad.getText().toString();
                String inicio = editTextInicio.getText().toString();
                String fin = editTextFin.getText().toString();

                actividadControl.ActualizarActividad(actividad.getIdActividad(), nombre, inicio, fin);
                if(verificarInternet()){
                    actividadControl.ActualizarActividadWS(actividad.getIdActividad(), nombre, inicio, fin);
                }else {
                    String idActividad = String.valueOf(actividad.getIdActividad());
                    String parametros = "update;"+idActividad
                            + ";" + nombre
                            + ";" + inicio
                            + ";" + fin;
                    writeToFile(parametros, context,"Actividad");
                }

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, new ActividadesFragment()).commit();
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

        context = getActivity();
        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        SeleccionarTramitesFragment fragment = new SeleccionarTramitesFragment();
        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putInt("idActividad", actividad.getIdActividad());
        switch (view.getId()){
            case R.id.editTextInicio:
                showDatePickerDialog(editTextInicio);
                break;
            case R.id.editTextFin:
                showDatePickerDialog(editTextFin);
                break;
            case R.id.cardView5:
                bundle.putBoolean("accion", true);
                fragment.setArguments(bundle);
                fragment.show(fragmentManager, "dialogo");
                break;
            case R.id.cardView6:
                fragment.setArguments(bundle);
                fragment.show(fragmentManager, "dialogo");
                break;
        }
    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + "-" + (month+1) + "-" + year;
                editText.setText(selectedDate);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
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
            Log.e("AgregarCargoFragment", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarCargoFragment", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }
}
