package sv.ues.fia.eisi.trues.ui.admin.cargo;

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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.CargoControl;
import sv.ues.fia.eisi.trues.db.control.PersonalCargoControl;
import sv.ues.fia.eisi.trues.db.entity.Cargo;

public class QuitarCargoFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private Context context;
    private Spinner spinnerCargo;
    private Integer idPersonal;
    private List<Cargo> cargoList;
    private List<String> cargos;
    private PersonalCargoControl control;
    private CargoControl cargoControl;

    public static QuitarCargoFragment newInstance() {
        return new QuitarCargoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idPersonal = getArguments().getInt("idPersonal", -1);
        control = new PersonalCargoControl(context);

        cargoControl = new CargoControl(context);
        cargoList = cargoControl.ObtenerCargos(idPersonal);

        cargos = new ArrayList<>();
        cargos.add("Seleccione un cargo...");
        for (int i = 0; i<cargoList.size(); i++){
            cargos.add(cargoList.get(i).getNombreCargo());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context, R.layout.style_spinner, cargos);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_quitar_cargo, null);
        spinnerCargo = view.findViewById(R.id.spinnerCargo);
        spinnerCargo.setAdapter(adapter);
        spinnerCargo.setOnItemSelectedListener(this);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position>0){
            control.eliminarPersonalCargo(idPersonal, cargoList.get(position-1).getIdCargo());
            this.dismiss();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}