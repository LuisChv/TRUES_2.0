package sv.ues.fia.eisi.trues.ui.admin.ubicacion;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.PersonalUbicacionControl;
import sv.ues.fia.eisi.trues.db.control.UbicacionControl;
import sv.ues.fia.eisi.trues.db.entity.Ubicacion;

public class QuitarUbicacionFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private Context context;
    private Spinner spinnerUbicacion;
    private Integer idPersonal;
    private List<Ubicacion> ubicacionList;
    private List<String> ubicaciones;
    private PersonalUbicacionControl control;
    private UbicacionControl ubicacionControl;

    public static QuitarUbicacionFragment newInstance() {
        return new QuitarUbicacionFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idPersonal = getArguments().getInt("idPersonal", -1);
        control = new PersonalUbicacionControl(context);

        ubicacionControl = new UbicacionControl(context);
        ubicacionList = ubicacionControl.ObtenerUbicaciones(idPersonal);

        ubicaciones = new ArrayList<>();
        ubicaciones.add(getText(R.string.seleccionar_ubicacion).toString());
        for (int i = 0; i<ubicacionList.size(); i++){
            ubicaciones.add(ubicacionList.get(i).getComponenteTematica());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, ubicaciones);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_quitar_ubicacion, null);
        spinnerUbicacion = view.findViewById(R.id.spinnerUbicacion);
        spinnerUbicacion.setAdapter(adapter);
        spinnerUbicacion.setOnItemSelectedListener(this);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position>0){
            control.eliminarPersonalUbicacion(idPersonal, ubicacionList.get(position-1).getIdUbicacion());
            this.dismiss();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}