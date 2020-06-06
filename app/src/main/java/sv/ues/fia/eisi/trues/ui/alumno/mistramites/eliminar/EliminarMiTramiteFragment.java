package sv.ues.fia.eisi.trues.ui.alumno.mistramites.eliminar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.UsuarioTramiteControl;
import sv.ues.fia.eisi.trues.db.entity.UsuarioTramite;
import sv.ues.fia.eisi.trues.ui.alumno.mistramites.lista.MisTramitesFragment;

public class EliminarMiTramiteFragment extends DialogFragment implements View.OnClickListener {

    private View view;
    private Integer idUsuarioTramite;
    private UsuarioTramite usuarioTramite;
    private Button buttonEliminar, buttonCancelar;

    public static EliminarMiTramiteFragment newInstance() {
        return new EliminarMiTramiteFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idUsuarioTramite = getArguments().getInt("idUsuarioTramite", -1);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_eliminar_mi_tramite, null);
        buttonEliminar = view.findViewById(R.id.buttonEliminar);
        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsuarioTramiteControl control = new UsuarioTramiteControl(getActivity());
                usuarioTramite = control.obtenerUTramite(idUsuarioTramite);

                control.eliminarUTramite(idUsuarioTramite, usuarioTramite.getIdTramite(), usuarioTramite.getUsuario());

                MisTramitesFragment misTramitesFragment = new MisTramitesFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, misTramitesFragment).commit();
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

    @Override
    public void onClick(View v) {

    }
}
