package sv.ues.fia.eisi.trues.ui.login.invitado;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.FacultadControl;
import sv.ues.fia.eisi.trues.db.entity.Facultad;
import sv.ues.fia.eisi.trues.ui.global.MenuAdminActivity;

public class FacultadPreferidaFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private Spinner spinnerFacultad;
    private FacultadControl facultadControl;
    private List<Facultad> facultadList;
    private List<String> facultades;
    private SharedPreferences sharedPreferences;

    public static FacultadPreferidaFragment newInstance() {
        return new FacultadPreferidaFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_facultad_preferida, null);
        spinnerFacultad = view.findViewById(R.id.spinnerFacultad);

        facultadControl = new FacultadControl(getActivity());
        facultadList = facultadControl.consultarFacultades();

        facultades = new ArrayList<>();
        facultades.add("Seleccione una facultad...");
        for (int i = 0; i<facultadList.size(); i++){
            facultades.add(facultadList.get(i).getNombreFacultad());
        }

        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter(getActivity(), R.layout.style_spinner, facultades);
        spinnerFacultad.setAdapter(arrayAdapter);
        spinnerFacultad.setOnItemSelectedListener(this);

        builder.setView(view);
        // Create the AlertDialog object and return it

        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i>0){
            sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("facultad", (i));
            editor.commit();

            Intent intent = new Intent(getActivity(), MenuAdminActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
