package sv.ues.fia.eisi.trues.ui.admin.documentos.eliminar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.DocumentoControl;
import sv.ues.fia.eisi.trues.db.entity.Documento;
import sv.ues.fia.eisi.trues.ui.global.documento.lista.DocumentoFragment;

public class CEliminarDocumentoFragment extends DialogFragment {

    private View view;
    private Context context;
    private Integer idDocumento;
    private Button buttonEliminar, buttonCancelar;
    private Documento documento;

    public static CEliminarDocumentoFragment newInstance() {
        return new CEliminarDocumentoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idDocumento = getArguments().getInt("idDocumento", -1);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_eliminar_tramite, null);
        buttonEliminar = view.findViewById(R.id.buttonEliminar);
        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentoControl control = new DocumentoControl(context);
                documento = control.consultarDocumeto(idDocumento);

                control.EliminarDocumento(idDocumento);

                Bundle bundle = new Bundle();
                bundle.putInt("idTramite", getArguments().getInt("idTramite", -1));
                DocumentoFragment documentoFragment = new DocumentoFragment();
                documentoFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, documentoFragment).addToBackStack(null).commit();
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
