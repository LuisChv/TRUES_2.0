package sv.ues.fia.eisi.trues.ui.admin.requisito.agregar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.RequisitoControl;
import sv.ues.fia.eisi.trues.db.control.RequisitoTramiteControl;
import sv.ues.fia.eisi.trues.db.entity.Requisito;

public class AgregarRequisitosFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private Context context;
    private EditText editTextRequisito;
    private Spinner spinnerRequisito;
    private Button buttonGuardar, buttonCancelar;
    private Integer idTramite;
    private List<Requisito> requisitoList;
    private List<String> requisitos;
    private RequisitoTramiteControl control;

    public static AgregarRequisitosFragment newInstance() {
        return new AgregarRequisitosFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idTramite = getArguments().getInt("idTramite", -1);
        control = new RequisitoTramiteControl(context);

        final RequisitoControl requisitoControl = new RequisitoControl(context);
        requisitoList = requisitoControl.consultarRequisitos();

        requisitos = new ArrayList<>();
        requisitos.add(getText(R.string.seleccionar_requisito).toString());
        for (int i = 0; i<requisitoList.size(); i++){
            requisitos.add(requisitoList.get(i).getDescripcion());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, requisitos);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_agregar_requisitos, null);
        editTextRequisito = view.findViewById(R.id.editTextRequisito);
        spinnerRequisito = view.findViewById(R.id.spinnerRequisito);
        spinnerRequisito.setAdapter(adapter);
        spinnerRequisito.setOnItemSelectedListener(this);

        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String requisito = editTextRequisito.getText().toString();

                if (!requisito.isEmpty()){
                    Integer idRequisito = requisitoControl.crearRequisito(requisito);
                    if (idRequisito!=null){
                        control.crearRequisitoTramite(idTramite, idRequisito);
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
            control.crearRequisitoTramite(idTramite, requisitoList.get(position-1).getIdRequisito());
            this.dismiss();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
