package sv.ues.fia.eisi.trues.ui.admin.personal;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.PersonalControl;
import sv.ues.fia.eisi.trues.ui.admin.cargo.AgregarCargoFragment;
import sv.ues.fia.eisi.trues.ui.admin.cargo.QuitarCargoFragment;
import sv.ues.fia.eisi.trues.ui.admin.ubicacion.AgregarUbicacionFragment;
import sv.ues.fia.eisi.trues.ui.admin.ubicacion.QuitarUbicacionFragment;
import sv.ues.fia.eisi.trues.ui.admin.unidad.AgregarUnidadFragment;
import sv.ues.fia.eisi.trues.ui.admin.unidad.QuitarUnidadFragment;

public class AgregarPersonalFragment extends DialogFragment implements View.OnClickListener {

    private View view;
    private Context context;
    private TextView textView;
    private EditText editTextPersonal;
    private CardView agregarUnidad, quitarUnidad, agregarCargo, quitarCargo, agregarUbicacion, quitarUbicacion;
    private CardView cardView7, cardView8, cardView9;
    private Button buttonCancelar, buttonGuardar;
    private Integer idPersonal;

    public static AgregarPersonalFragment newInstance() {
        return new AgregarPersonalFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();

        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_agregar_personal, null);
        textView = view.findViewById(R.id.textView);
        textView.setText(getText(R.string.menu_agregar_personal));
        editTextPersonal = view.findViewById(R.id.editTextPersonal);
        cardView7 = view.findViewById(R.id.cardView7);
        cardView8 = view.findViewById(R.id.cardView8);
        cardView9 = view.findViewById(R.id.cardView9);
        agregarUbicacion = view.findViewById(R.id.agregarUbicacion);
        agregarUnidad = view.findViewById(R.id.agregarUnidad);
        agregarCargo = view.findViewById(R.id.agregarCargo);
        quitarUbicacion = view.findViewById(R.id.quitarUbicacion);
        quitarUnidad = view.findViewById(R.id.quitarUnidad);
        quitarCargo = view.findViewById(R.id.quitarCargo);
        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        buttonCancelar = view.findViewById(R.id.buttonCancelar);

        cardView7.setVisibility(View.GONE);
        cardView8.setVisibility(View.GONE);
        cardView9.setVisibility(View.GONE);
        agregarUbicacion.setVisibility(View.GONE);
        agregarUnidad.setVisibility(View.GONE);
        agregarCargo.setVisibility(View.GONE);
        quitarUbicacion.setVisibility(View.GONE);
        quitarUnidad.setVisibility(View.GONE);
        quitarCargo.setVisibility(View.GONE);
        buttonGuardar.setOnClickListener(this);
        buttonCancelar.setOnClickListener(this);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        DialogFragment fragment;
        switch (v.getId()){
            case R.id.buttonGuardar:

                if (!editTextPersonal.getText().toString().isEmpty()){
                    PersonalControl personalControl = new PersonalControl(context);
                    idPersonal = personalControl.crearPersonal(editTextPersonal.getText().toString());

                    if (idPersonal != null){

                        editTextPersonal.setVisibility(View.GONE);
                        editTextPersonal.setVisibility(View.GONE);
                        buttonCancelar.setVisibility(View.INVISIBLE);
                        buttonGuardar.setVisibility(View.INVISIBLE);

                        cardView7.setVisibility(View.VISIBLE);
                        cardView8.setVisibility(View.VISIBLE);
                        cardView9.setVisibility(View.VISIBLE);
                        agregarUbicacion.setVisibility(View.VISIBLE);
                        agregarUnidad.setVisibility(View.VISIBLE);
                        agregarCargo.setVisibility(View.VISIBLE);
                        quitarUbicacion.setVisibility(View.VISIBLE);
                        quitarUnidad.setVisibility(View.VISIBLE);
                        quitarCargo.setVisibility(View.VISIBLE);

                        agregarUbicacion.setOnClickListener(this);
                        agregarUnidad.setOnClickListener(this);
                        agregarCargo.setOnClickListener(this);
                        quitarUbicacion.setOnClickListener(this);
                        quitarUnidad.setOnClickListener(this);
                        quitarCargo.setOnClickListener(this);
                    }

                } else {
                    Toast.makeText(context.getApplicationContext(), getText(R.string.datos_incorrectos), Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.buttonCancelar:
                dismiss();
                break;
            case R.id.quitarUnidad:
                fragment = new QuitarUnidadFragment();
                showDialog(fragment);
                break;
            case R.id.quitarCargo:
                fragment = new QuitarCargoFragment();
                showDialog(fragment);
                break;
            case R.id.quitarUbicacion:
                fragment = new QuitarUbicacionFragment();
                showDialog(fragment);
                break;
            case R.id.agregarUnidad:
                fragment = new AgregarUnidadFragment();
                showDialog(fragment);
                break;
            case R.id.agregarCargo:
                fragment = new AgregarCargoFragment();
                showDialog(fragment);
                break;
            case R.id.agregarUbicacion:
                fragment = new AgregarUbicacionFragment();
                showDialog(fragment);
                break;
        }

    }

    private void showDialog(DialogFragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putInt("idPersonal", idPersonal);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragment.setArguments(bundle);
        fragment.show(fragmentManager, "dialog");
    }
}