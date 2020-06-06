package sv.ues.fia.eisi.trues.ui.admin.requisito.eliminar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.RequisitoControl;
import sv.ues.fia.eisi.trues.db.entity.Requisito;
import sv.ues.fia.eisi.trues.ui.global.requisito.RequisitosFragment;

public class CEliminarRequisitoFragment extends DialogFragment {

    private View view;
    private Context context;
    private Integer idRequisito;
    private Button buttonEliminar, buttonCancelar;
    private Requisito requisito;

    public static CEliminarRequisitoFragment newInstance() {
        return new CEliminarRequisitoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idRequisito = getArguments().getInt("idRequisito", -1);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_eliminar_tramite, null);
        buttonEliminar = view.findViewById(R.id.buttonEliminar);
        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequisitoControl control = new RequisitoControl(context);
                requisito = control.consultarRequisito(idRequisito);

                control.eliminarRequisito(idRequisito);

                Bundle bundle = new Bundle();
                bundle.putInt("idTramite", getArguments().getInt("idTramite", -1));
                RequisitosFragment requisitosFragment = new RequisitosFragment();
                requisitosFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, requisitosFragment).addToBackStack(null).commit();
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
