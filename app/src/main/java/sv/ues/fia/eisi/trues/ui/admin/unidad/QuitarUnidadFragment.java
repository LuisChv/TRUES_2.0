package sv.ues.fia.eisi.trues.ui.admin.unidad;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.PersonalUnidadAdminControl;
import sv.ues.fia.eisi.trues.db.control.UnidadAdminControl;
import sv.ues.fia.eisi.trues.db.entity.UnidadAdmin;

public class QuitarUnidadFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private Context context;
    private Spinner spinnerUnidad;
    private Integer idPersonal;
    private List<UnidadAdmin> unidadList;
    private List<String> unidades;
    private PersonalUnidadAdminControl control;
    private UnidadAdminControl unidadControl;
    private Integer idFacultad;
    private SharedPreferences sharedPreferences;
    private View view;

    public static QuitarUnidadFragment newInstance() {
        return new QuitarUnidadFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idPersonal = getArguments().getInt("idPersonal", -1);
        control = new PersonalUnidadAdminControl(context);

        unidadControl = new UnidadAdminControl(context);
        unidadList = unidadControl.consultar(idPersonal);

        unidades = new ArrayList<>();
        unidades.add("Seleccione una unidad...");
        for (int i = 0; i<unidadList.size(); i++){
            unidades.add(unidadList.get(i).getNombreUAdmin());
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, unidades);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_quitar_unidad, null);
        spinnerUnidad = view.findViewById(R.id.spinnerUnidad);
        spinnerUnidad.setAdapter(adapter);
        spinnerUnidad.setOnItemSelectedListener(this);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position>0){
            control.eliminarPersonalUnidad(idPersonal, unidadList.get(position-1).getIdUAdmin());
            this.dismiss();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}