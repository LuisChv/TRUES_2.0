package sv.ues.fia.eisi.trues.ui.admin.documentos.eliminar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.DocumentoControl;
import sv.ues.fia.eisi.trues.db.control.DocumentoTramiteControl;
import sv.ues.fia.eisi.trues.db.entity.Documento;

public class EliminarDocumentoFragment extends DialogFragment {

    private View view;
    private Context context;
    private Integer idTramite;
    private List<Documento> documentoList;
    private List<String> documentos;
    private Spinner spinnerDocumento;

    public static EliminarDocumentoFragment newInstance() {
        return new EliminarDocumentoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idTramite = getArguments().getInt("idTramite", -1);

        final DocumentoControl documentoControl = new DocumentoControl(context);
        documentoList = documentoControl.consultarDocumentos(idTramite);

        documentos = new ArrayList<>();
        documentos.add(getText(R.string.seleccionar_documento).toString());
        for (int i = 0; i<documentoList.size(); i++){
            documentos.add(documentoList.get(i).getNombreDocumento());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, documentos);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_eliminar_documento, null);
        spinnerDocumento = view.findViewById(R.id.spinnerDocumento);
        spinnerDocumento.setAdapter(adapter);
        spinnerDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    DocumentoTramiteControl control = new DocumentoTramiteControl(context);
                    control.eliminarDocumentoTramite(documentoList.get(position-1).getIdDocumento(), idTramite);
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
