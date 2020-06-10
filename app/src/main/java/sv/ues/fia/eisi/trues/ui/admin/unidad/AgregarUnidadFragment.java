package sv.ues.fia.eisi.trues.ui.admin.unidad;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.PersonalUnidadAdminControl;
import sv.ues.fia.eisi.trues.db.control.RequisitoControl;
import sv.ues.fia.eisi.trues.db.control.RequisitoTramiteControl;
import sv.ues.fia.eisi.trues.db.control.UnidadAdminControl;
import sv.ues.fia.eisi.trues.db.entity.Requisito;
import sv.ues.fia.eisi.trues.db.entity.UnidadAdmin;

public class AgregarUnidadFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private Context context;
    private EditText editTextUnidad;
    private Spinner spinnerUnidad;
    private Button buttonGuardar, buttonCancelar;
    private Integer idPersonal;
    private List<UnidadAdmin> unidadList;
    private List<String> unidades;
    private PersonalUnidadAdminControl control;
    private UnidadAdminControl unidadControl;
    private Integer idFacultad;
    private SharedPreferences sharedPreferences;

    public static AgregarUnidadFragment newInstance() {
        return new AgregarUnidadFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        idFacultad = sharedPreferences.getInt("facultad", -1);

        idPersonal = getArguments().getInt("idPersonal", -1);
        control = new PersonalUnidadAdminControl(context);

        unidadControl = new UnidadAdminControl(context);
        unidadList = unidadControl.obtenerUAdmin(idFacultad);

        unidades = new ArrayList<>();
        unidades.add(getText(R.string.seleccionar_unidad).toString());
        for (int i = 0; i<unidadList.size(); i++){
            unidades.add(unidadList.get(i).getNombreUAdmin());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, unidades);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_agregar_unidad, null);
        editTextUnidad = view.findViewById(R.id.editTextUnidad);
        spinnerUnidad = view.findViewById(R.id.spinnerUnidad);
        spinnerUnidad.setAdapter(adapter);
        spinnerUnidad.setOnItemSelectedListener(this);

        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unidad = editTextUnidad.getText().toString();

                if (!unidad.isEmpty()){
                    Integer idUnidad = unidadControl.crearUAdmin(idFacultad, unidad);
                    if (idUnidad!=null){
                        control.crearPersonalUnidad(idPersonal, idUnidad);
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
            control.crearPersonalUnidad(idPersonal, unidadList.get(position-1).getIdUAdmin());
            this.dismiss();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}