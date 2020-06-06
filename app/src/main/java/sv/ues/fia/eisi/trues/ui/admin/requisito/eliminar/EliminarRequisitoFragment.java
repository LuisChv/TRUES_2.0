package sv.ues.fia.eisi.trues.ui.admin.requisito.eliminar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.RequisitoControl;
import sv.ues.fia.eisi.trues.db.control.RequisitoTramiteControl;
import sv.ues.fia.eisi.trues.db.entity.Requisito;

public class EliminarRequisitoFragment extends DialogFragment {

    private View view;
    private Context context;
    private Integer idTramite;
    private List<Requisito> requisitoList;
    private List<String> requisitos;
    private Spinner spinnerRequisito;

    public static EliminarRequisitoFragment newInstance() {
        return new EliminarRequisitoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idTramite = getArguments().getInt("idTramite", -1);

        final RequisitoControl requisitoControl = new RequisitoControl(context);
        requisitoList = requisitoControl.consultarRequisitos(idTramite);

        requisitos = new ArrayList<>();
        requisitos.add("Seleccione un requisito...");
        for (int i = 0; i<requisitoList.size(); i++){
            requisitos.add(requisitoList.get(i).getDescripcion());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, requisitos);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_eliminar_requisito, null);
        spinnerRequisito = view.findViewById(R.id.spinnerRequisito);
        spinnerRequisito.setAdapter(adapter);
        spinnerRequisito.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    RequisitoTramiteControl control = new RequisitoTramiteControl(context);
                    control.eliminarRequisitoTramite(requisitoList.get(position-1).getIdRequisito(), idTramite);
                    dismiss();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        builder.setView(view);
        return builder.create();
    }

}
