package sv.ues.fia.eisi.trues.ui.admin.paso.eliminar;

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
import sv.ues.fia.eisi.trues.db.control.PasoControl;
import sv.ues.fia.eisi.trues.db.entity.Paso;

public class EliminarPasoFragment extends DialogFragment {

    private View view;
    private Context context;
    private Integer idTramite;
    private List<Paso> pasoList;
    private List<String> pasos;
    private Spinner spinnerPaso;

    public static EliminarPasoFragment newInstance() {
        return new EliminarPasoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idTramite = getArguments().getInt("idTramite", -1);

        final PasoControl pasoControl = new PasoControl(context);
        pasoList = pasoControl.obtenerPasos(idTramite);

        pasos = new ArrayList<>();
        pasos.add(getText(R.string.seleccionar_paso).toString());
        for (int i = 0; i<pasoList.size(); i++){
            pasos.add(pasoList.get(i).getDescripcion());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, pasos);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_eliminar_paso, null);
        spinnerPaso = view.findViewById(R.id.spinnerPaso);
        spinnerPaso.setAdapter(adapter);
        spinnerPaso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    pasoControl.eliminarPaso(pasoList.get(position-1).getIdPaso());
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
