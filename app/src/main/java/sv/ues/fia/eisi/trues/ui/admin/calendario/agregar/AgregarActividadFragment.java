package sv.ues.fia.eisi.trues.ui.admin.calendario.agregar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.ActividadControl;
import sv.ues.fia.eisi.trues.db.control.FacultadControl;
import sv.ues.fia.eisi.trues.db.entity.Actividad;
import sv.ues.fia.eisi.trues.db.entity.Facultad;
import sv.ues.fia.eisi.trues.ui.admin.calendario.SeleccionarTramitesFragment;
import sv.ues.fia.eisi.trues.ui.global.DatePickerFragment;
import sv.ues.fia.eisi.trues.ui.global.calendario.lista.ActividadesFragment;

public class AgregarActividadFragment extends DialogFragment implements View.OnClickListener {
    private View view;
    private EditText editTextNombreActividad, editTextInicio, editTextFin;
    private CardView cardView5, cardView6, cardView7;
    private Context context;
    private ActividadControl actividadControl;
    private SharedPreferences sharedPreferences;
    private Integer facultad;
    private TextView textView7;
    private Button buttonGuardar, buttonCancelar;
    private Integer idActividad;

    public static AgregarActividadFragment newInstance() {
        return new AgregarActividadFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        facultad = sharedPreferences.getInt("facultad", -1);
        actividadControl = new ActividadControl(getActivity());

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_actualizar_actividad, null);
        editTextNombreActividad = view.findViewById(R.id.editTextNombreActividad);
        textView7 = view.findViewById(R.id.textView7);
        textView7.setText(getText(R.string.menu_agregar_actividad));
        editTextInicio = view.findViewById(R.id.editTextInicio);
        editTextFin = view.findViewById(R.id.editTextFin);
        cardView5 = view.findViewById(R.id.cardView5);
        cardView5.setVisibility(View.GONE);
        cardView6 = view.findViewById(R.id.cardView6);
        cardView6.setVisibility(View.GONE);
        cardView7 = view.findViewById(R.id.cardView7);
        cardView7.setVisibility(View.GONE);
        editTextInicio.setOnClickListener(this);
        editTextFin.setOnClickListener(this);

        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextNombreActividad.getText().toString();
                String inicio = editTextInicio.getText().toString();
                String fin = editTextFin.getText().toString();

                if (nombre.isEmpty() || inicio.isEmpty() || fin.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),
                            getText(R.string.datos_incorrectos), Toast.LENGTH_SHORT).show();
                } else {
                    idActividad = actividadControl.CrearActividad(facultad, nombre, inicio, fin);
                    if(idActividad != null){
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().add(R.id.mainContainer, new ActividadesFragment()).commit();
                        dismiss();

                        Bundle bundle = new Bundle();
                        bundle.putInt("idActividad", idActividad);
                        SeleccionarTramitesFragment fragment = new SeleccionarTramitesFragment();
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragment.show(fragmentManager, "dialogo");

                        Toast.makeText(getActivity().getApplicationContext(), "Agrega trámites a la nueva actividad si es necesario.", Toast.LENGTH_LONG).show();
                    }

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

        context = getActivity();
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editTextInicio:
                showDatePickerDialog(editTextInicio);
                break;
            case R.id.editTextFin:
                showDatePickerDialog(editTextFin);
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

}
