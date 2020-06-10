package sv.ues.fia.eisi.trues.ui.admin.documentos.agregar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

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
import sv.ues.fia.eisi.trues.db.control.DocumentoControl;
import sv.ues.fia.eisi.trues.db.control.DocumentoTramiteControl;
import sv.ues.fia.eisi.trues.db.entity.Documento;

public class AgregarDocumentoFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private Context context;
    private EditText editTextDocumento, editTextURL;
    private Spinner spinnerDocumento;
    private Button buttonGuardar, buttonCancelar;
    private Integer idTramite;
    private List<Documento> documentoList;
    private List<String> documentos;
    private DocumentoTramiteControl control;

    public static AgregarDocumentoFragment newInstance() {
        return new AgregarDocumentoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idTramite = getArguments().getInt("idTramite", -1);
        control = new DocumentoTramiteControl(context);

        final DocumentoControl documentoControl = new DocumentoControl(context);
        documentoList = documentoControl.consultarDocumentos();

        documentos = new ArrayList<>();
        documentos.add(getText(R.string.seleccionar_documento).toString());
        for (int i = 0; i<documentoList.size(); i++){
            documentos.add(documentoList.get(i).getNombreDocumento());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, documentos);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_agregar_documento, null);
        editTextDocumento = view.findViewById(R.id.editTextDocumento);
        editTextURL = view.findViewById(R.id.editTextURL);
        spinnerDocumento = view.findViewById(R.id.spinnerDocumento);
        spinnerDocumento.setAdapter(adapter);
        spinnerDocumento.setOnItemSelectedListener(this);

        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String documento = editTextDocumento.getText().toString();
                String url = editTextURL.getText().toString();

                if (!documento.isEmpty() && !url.isEmpty()){
                    Integer idDocumento = documentoControl.CrearDocumento(url, documento);
                    if (idDocumento!=null){
                        control.crearDocumentoTramite(idDocumento, idTramite);
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
            control.crearDocumentoTramite(documentoList.get(position-1).getIdDocumento(), idTramite);
            dismiss();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
