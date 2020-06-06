package sv.ues.fia.eisi.trues.ui.global.personal;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.PasoControl;
import sv.ues.fia.eisi.trues.db.control.PersonalCargoControl;
import sv.ues.fia.eisi.trues.db.control.PersonalControl;
import sv.ues.fia.eisi.trues.db.control.PersonalUnidadAdminControl;
import sv.ues.fia.eisi.trues.db.entity.Cargo;
import sv.ues.fia.eisi.trues.db.entity.Personal;
import sv.ues.fia.eisi.trues.db.entity.PersonalUnidadAdmin;
import sv.ues.fia.eisi.trues.db.entity.UnidadAdmin;

public class DetallePersonalFragment extends DialogFragment {

    private Integer idPersonal;
    private Personal personal;
    private List<Cargo> cargos;
    private View view;
    private TextView textViewNombre, textViewCargos, textViewUnidad;
    private List<UnidadAdmin> unidades;

    public static DetallePersonalFragment newInstance() {
        return new DetallePersonalFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idPersonal = getArguments().getInt("idPersonal", -1);

        PersonalControl personalControl = new PersonalControl(getActivity());
        personal = personalControl.consultarPersonal(idPersonal);

        PersonalCargoControl personalCargoControl = new PersonalCargoControl(getActivity());
        cargos = personalCargoControl.consultarCargo(idPersonal);

        PersonalUnidadAdminControl personalUnidadControl = new PersonalUnidadAdminControl(getActivity());
        unidades = personalUnidadControl.consultarUnidades(idPersonal);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_detalle_personal, null);
        textViewNombre = view.findViewById(R.id.textViewNombre);
        textViewNombre.setText(personal.getNombrePersonal());
        textViewCargos = view.findViewById(R.id.textViewCargos);
        String stringCargos = null;

        for (int i = 0; i<cargos.size(); i++){
            if (stringCargos != null){
                stringCargos = stringCargos + "\n" + cargos.get(i).getNombreCargo();
            } else {
                stringCargos = cargos.get(i).getNombreCargo();
            }
        }
        if (stringCargos != null){
            textViewCargos.setText(stringCargos);
        }else {
            textViewCargos.setText("---");
        }

        textViewUnidad = view.findViewById(R.id.textViewUnidad);
        String stringUnidades = null;

        for (int i = 0; i<unidades.size(); i++){
            if (stringUnidades != null){
                stringUnidades = stringUnidades + "\n" + unidades.get(i).getNombreUAdmin();
            } else {
                stringUnidades = unidades.get(i).getNombreUAdmin();
            }
        }
        if (stringUnidades != null){
            textViewUnidad.setText(stringUnidades);
        }else {
            textViewUnidad.setText("---");
        }

        builder.setView(view);
        return builder.create();
    }

}
