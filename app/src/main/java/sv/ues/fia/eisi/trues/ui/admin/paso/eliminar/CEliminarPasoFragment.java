package sv.ues.fia.eisi.trues.ui.admin.paso.eliminar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.PasoControl;
import sv.ues.fia.eisi.trues.db.entity.Paso;
import sv.ues.fia.eisi.trues.ui.global.paso.lista.PasoFragment;

public class CEliminarPasoFragment extends DialogFragment {

    public static CEliminarPasoFragment newInstance() {
        return new CEliminarPasoFragment();
    }

    private View view;
    private Context context;
    private Integer idPaso;
    private Button buttonEliminar, buttonCancelar;
    private Paso paso;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idPaso = getArguments().getInt("idPaso", -1);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_eliminar_tramite, null);
        buttonEliminar = view.findViewById(R.id.buttonEliminar);
        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasoControl control = new PasoControl(getActivity());
                paso = control.obtenerPaso(idPaso);

                control.eliminarPaso(idPaso);

                Bundle bundle = new Bundle();
                bundle.putInt("idTramite", paso.getIdTramite());
                PasoFragment pasoFragment = new PasoFragment();
                pasoFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, pasoFragment).addToBackStack(null).commit();
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
        return builder.create();
    }

}
