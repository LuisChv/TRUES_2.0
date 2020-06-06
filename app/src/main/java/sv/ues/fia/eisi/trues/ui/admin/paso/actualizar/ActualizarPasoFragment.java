package sv.ues.fia.eisi.trues.ui.admin.paso.actualizar;

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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.PasoControl;
import sv.ues.fia.eisi.trues.db.control.PersonalControl;
import sv.ues.fia.eisi.trues.db.entity.Paso;
import sv.ues.fia.eisi.trues.db.entity.Personal;
import sv.ues.fia.eisi.trues.ui.global.paso.lista.PasoFragment;

public class ActualizarPasoFragment extends DialogFragment implements View.OnClickListener {

    private View view;
    private Context context;
    private EditText editTextDescripcion;
    private Spinner spinnerPersonal;
    private SeekBar seekBarPorcentaje;
    private Button buttonGuardar, buttonCancelar;
    private SharedPreferences sharedPreferences;
    private Integer idFacultad;
    private List<Personal> personalList;
    private List<String> nombrePersonal;
    private Integer idPaso, position;
    private Paso paso;
    private PasoControl pasoControl;
    private TextView textView;

    public static ActualizarPasoFragment newInstance() {
        return new ActualizarPasoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        idFacultad = sharedPreferences.getInt("facultad", -1);
        PersonalControl personalControl = new PersonalControl(context);
        personalList = personalControl.consultarTodoPersonal(idFacultad);

        pasoControl = new PasoControl(context);

        idPaso = getArguments().getInt("idPaso", -1);
        paso = pasoControl.obtenerPaso(idPaso);

        nombrePersonal = new ArrayList<>();
        nombrePersonal.add("Seleccione a alguien del personal...");

        for (int i = 0; i<personalList.size(); i++){
            nombrePersonal.add(personalList.get(i).getNombrePersonal());
            if (personalList.get(i).getIdPersonal() == paso.getIdPersonal()){
                position = i + 1;
            }
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, nombrePersonal);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_agregar_pasos, null);
        textView = view.findViewById(R.id.textView);
        textView.setText(getText(R.string.actualizar_paso));
        editTextDescripcion = view.findViewById(R.id.editTextDescripcion);
        editTextDescripcion.setText(paso.getDescripcion());
        seekBarPorcentaje = view.findViewById(R.id.seekBarPorcentaje);
        seekBarPorcentaje.setProgress(Math.round(paso.getPorcentaje()*100));
        spinnerPersonal = view.findViewById(R.id.spinnerPersonal);
        spinnerPersonal.setAdapter(adapter);
        spinnerPersonal.setSelection(position);

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
                    pasoControl.actualizarPaso(idPaso, personalList.get(idPersonal-1).getIdPersonal(), descripcion, porcentaje);

                    Bundle bundle = new Bundle();
                    bundle.putInt("idTramite", paso.getIdTramite());

                    PasoFragment fragment = new PasoFragment();
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


}
