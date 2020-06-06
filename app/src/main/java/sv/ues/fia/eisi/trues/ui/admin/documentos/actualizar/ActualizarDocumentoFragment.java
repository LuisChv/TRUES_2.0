package sv.ues.fia.eisi.trues.ui.admin.documentos.actualizar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.DocumentoControl;
import sv.ues.fia.eisi.trues.db.entity.Documento;
import sv.ues.fia.eisi.trues.ui.global.documento.DocumentoFragment;

public class ActualizarDocumentoFragment extends DialogFragment implements View.OnClickListener {
    private View view;
    private Context context;
    private EditText editTextNombre, editTextURL;
    private Integer idDocumento;
    private Documento documento;
    private DocumentoControl documentoControl;
    private TextView textView;
    private Button buttonGuardar, buttonCancelar;
    private Integer idTramite;
    private Spinner spinnerDocumento;
    private ImageView imageView;

    public static ActualizarDocumentoFragment newInstance() {
        return new ActualizarDocumentoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        documentoControl = new DocumentoControl(context);

        idDocumento = getArguments().getInt("idDocumento", -1);
        documento = documentoControl.consultarDocumeto(idDocumento);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_agregar_documento, null);
        textView = view.findViewById(R.id.textView12);
        textView.setText(getText(R.string.actualizar_documento));
        editTextNombre = view.findViewById(R.id.editTextDocumento);
        editTextNombre.setText(documento.getNombreDocumento());
        editTextURL = view.findViewById(R.id.editTextURL);
        editTextURL.setText(documento.getUrl());
        imageView = view.findViewById(R.id.imageView14);
        imageView.setVisibility(View.GONE);

        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(this);

        buttonCancelar = view.findViewById(R.id.buttonCancelar);
        buttonCancelar.setOnClickListener(this);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonGuardar:
                String nombre = editTextNombre.getText().toString();
                String url = editTextURL.getText().toString();
                if (!nombre.isEmpty() && !url.isEmpty()){
                    documentoControl.ActualizarDocumento(idDocumento, url, nombre);

                    Bundle bundle = new Bundle();
                    bundle.putInt("idTramite", getArguments().getInt("idTramite", -1));

                    DocumentoFragment fragment = new DocumentoFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainContainer, fragment).addToBackStack(null)
                            .commit();
                    dismiss();
                } else {
                    Toast.makeText(context.getApplicationContext(), getText(R.string.datos_incorrectos), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonCancelar:
                dismiss();
                break;

        }
    }


}
