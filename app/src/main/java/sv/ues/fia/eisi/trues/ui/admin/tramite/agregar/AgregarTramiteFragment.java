package sv.ues.fia.eisi.trues.ui.admin.tramite.agregar;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.TramiteControl;
import sv.ues.fia.eisi.trues.ui.admin.documentos.agregar.AgregarDocumentoFragment;
import sv.ues.fia.eisi.trues.ui.admin.documentos.eliminar.EliminarDocumentoFragment;
import sv.ues.fia.eisi.trues.ui.admin.paso.agregar.AgregarPasosFragment;
import sv.ues.fia.eisi.trues.ui.admin.paso.eliminar.EliminarPasoFragment;
import sv.ues.fia.eisi.trues.ui.admin.requisito.agregar.AgregarRequisitosFragment;
import sv.ues.fia.eisi.trues.ui.admin.requisito.eliminar.EliminarRequisitoFragment;
import sv.ues.fia.eisi.trues.ui.global.MenuAdminActivity;
import sv.ues.fia.eisi.trues.ui.global.tramite.lista.TramitesFragment;

public class AgregarTramiteFragment extends DialogFragment implements View.OnClickListener {
    private View view;
    private Context context;
    private TextView textView;
    private EditText editTextTramite;
    private CardView agregarPaso, quitarPaso, agregarRequisito, quitarRequisito, agregarDocumento, quitarDocumento;
    private CardView cardView7, cardView8, cardView9;
    private Button buttonCancelar, buttonGuardar;
    private SharedPreferences sharedPreferences;
    private Integer idFacultad;
    private Integer accion;
    private Integer idTramite;

    public static AgregarTramiteFragment newInstance() {
        return new AgregarTramiteFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        idFacultad = sharedPreferences.getInt("facultad", -1);

        accion = getArguments().getInt("accion", 0);

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_agregar_tramite, null);
        textView = view.findViewById(R.id.textView);
        textView.setText(getText(R.string.menu_agregar_tramite));
        editTextTramite = view.findViewById(R.id.editTextTramite);
        cardView7 = view.findViewById(R.id.cardView7);
        cardView8 = view.findViewById(R.id.cardView8);
        cardView9 = view.findViewById(R.id.cardView9);
        agregarDocumento = view.findViewById(R.id.agregarDocumento);
        agregarPaso = view.findViewById(R.id.agregarPaso);
        agregarRequisito = view.findViewById(R.id.agregarRequisito);
        quitarDocumento = view.findViewById(R.id.quitarDocumento);
        quitarPaso = view.findViewById(R.id.quitarPaso);
        quitarRequisito = view.findViewById(R.id.quitarRequisito);
        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        buttonCancelar = view.findViewById(R.id.buttonCancelar);

        switch (accion){
            case 0:
                cardView7.setVisibility(View.GONE);
                cardView8.setVisibility(View.GONE);
                cardView9.setVisibility(View.GONE);
                agregarDocumento.setVisibility(View.GONE);
                agregarPaso.setVisibility(View.GONE);
                agregarRequisito.setVisibility(View.GONE);
                quitarDocumento.setVisibility(View.GONE);
                quitarPaso.setVisibility(View.GONE);
                quitarRequisito.setVisibility(View.GONE);
                buttonGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!editTextTramite.getText().toString().isEmpty()){
                            TramiteControl tramiteControl = new TramiteControl(context);
                            idTramite = tramiteControl.crearTramite(idFacultad, editTextTramite.getText().toString());

                            if (idTramite != null){
                                getActivity().getSupportFragmentManager().
                                        beginTransaction().replace(R.id.mainContainer, new TramitesFragment()).commit();
                                ((MenuAdminActivity) context).setActionBarTitle(getText(R.string.title_tramites).toString());

                                Bundle bundle = new Bundle();
                                bundle.putInt("idTramite", idTramite);
                                bundle.putInt("accion", 1);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                AgregarTramiteFragment fragment = new AgregarTramiteFragment();
                                fragment.setArguments(bundle);
                                dismiss();
                                fragment.show(fragmentManager, "dialog");
                            }

                        } else {
                            Toast.makeText(context.getApplicationContext(), getText(R.string.datos_incorrectos), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                break;

            case 1:
                idTramite = getArguments().getInt("idTramite");
                editTextTramite.setVisibility(View.GONE);
                editTextTramite.setVisibility(View.GONE);
                buttonCancelar.setVisibility(View.INVISIBLE);
                buttonGuardar.setVisibility(View.INVISIBLE);

                agregarDocumento.setOnClickListener(this);
                agregarPaso.setOnClickListener(this);
                agregarRequisito.setOnClickListener(this);
                quitarDocumento.setOnClickListener(this);
                quitarPaso.setOnClickListener(this);
                quitarRequisito.setOnClickListener(this);
                break;
        }

        builder.setView(view);
        return builder.create();
    }


    @Override
    public void onClick(View v) {
        DialogFragment fragment;
        switch (v.getId()){
            case R.id.quitarPaso:
                fragment = new EliminarPasoFragment();
                showDialog(fragment);
                break;
            case R.id.quitarRequisito:
                fragment = new EliminarRequisitoFragment();
                showDialog(fragment);
                break;
            case R.id.quitarDocumento:
                fragment = new EliminarDocumentoFragment();
                showDialog(fragment);
                break;
            case R.id.agregarPaso:
                fragment = new AgregarPasosFragment();
                showDialog(fragment);
                break;
            case R.id.agregarRequisito:
                fragment = new AgregarRequisitosFragment();
                showDialog(fragment);
                break;
            case R.id.agregarDocumento:
                fragment = new AgregarDocumentoFragment();
                showDialog(fragment);
                break;
        }

    }

    private void showDialog(DialogFragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putInt("idTramite", idTramite);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragment.setArguments(bundle);
        fragment.show(fragmentManager, "dialog");
    }
}
