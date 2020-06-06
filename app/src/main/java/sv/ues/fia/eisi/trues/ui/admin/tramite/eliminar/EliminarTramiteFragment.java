package sv.ues.fia.eisi.trues.ui.admin.tramite.eliminar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.TramiteControl;
import sv.ues.fia.eisi.trues.ui.global.tramite.lista.TramitesFragment;

public class EliminarTramiteFragment extends DialogFragment {

    private View view;
    private Integer idTramite;
    private Button buttonEliminar, buttonCancelar;

    public static EliminarTramiteFragment newInstance() {
        return new EliminarTramiteFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idTramite = getArguments().getInt("idTramite", -1);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_eliminar_tramite, null);
        buttonEliminar = view.findViewById(R.id.buttonEliminar);
        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TramiteControl control = new TramiteControl(getActivity());

                control.eliminarTramite(idTramite);

                TramitesFragment tramitesFragment = new TramitesFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, tramitesFragment).commit();
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
