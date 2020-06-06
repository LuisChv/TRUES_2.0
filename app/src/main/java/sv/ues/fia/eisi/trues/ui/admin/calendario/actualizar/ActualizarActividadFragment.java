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
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

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
}
