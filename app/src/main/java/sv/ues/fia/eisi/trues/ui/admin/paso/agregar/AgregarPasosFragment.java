package sv.ues.fia.eisi.trues.ui.admin.paso.agregar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.PasoControl;
import sv.ues.fia.eisi.trues.db.control.PersonalControl;
import sv.ues.fia.eisi.trues.db.entity.Personal;

public class AgregarPasosFragment extends DialogFragment implements View.OnClickListener {

    private View view;
    private Context context;
    private EditText editTextDescripcion;
    private Spinner spinnerPersonal;
    private SeekBar seekBarPorcentaje;
    private Button buttonGuardar, buttonCancelar;
    private Integer idTramite;
    private SharedPreferences sharedPreferences;
    private Integer idFacultad;
    private List<Personal> personalList;
    private List<String> nombrePersonal;

    public static AgregarPasosFragment newInstance() {
        return new AgregarPasosFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idTramite = getArguments().getInt("idTramite", -1);
        sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        idFacultad = sharedPreferences.getInt("facultad", -1);
        PersonalControl personalControl = new PersonalControl(context);
        personalList = personalControl.consultarTodoPersonal(idFacultad);

        nombrePersonal = new ArrayList<>();
        nombrePersonal.add(getText(R.string.seleccionar_personal).toString());
        for (int i = 0; i<personalList.size(); i++){
            nombrePersonal.add(personalList.get(i).getNombrePersonal());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, nombrePersonal);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_agregar_pasos, null);
        editTextDescripcion = view.findViewById(R.id.editTextDescripcion);
        seekBarPorcentaje = view.findViewById(R.id.seekBarPorcentaje);
        spinnerPersonal = view.findViewById(R.id.spinnerPersonal);
        spinnerPersonal.setAdapter(adapter);
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
                String descripcion = editTextDescripcion.getText().toString();
                Integer idPersonal = spinnerPersonal.getSelectedItemPosition();
                Float porcentaje = (float) seekBarPorcentaje.getProgress()/100;
                if (!descripcion.isEmpty() && idPersonal >= 1 && porcentaje>0){
                    PasoControl pasoControl = new PasoControl(context);
                    pasoControl.crearPaso(personalList.get(idPersonal-1).getIdPersonal(), idTramite, descripcion, porcentaje);
                    this.dismiss();
                } else {
                    Toast.makeText(context.getApplicationContext(), getText(R.string.datos_incorrectos), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonCancelar:
                dismiss();
                break;
        }
    }
}
