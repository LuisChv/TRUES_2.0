package sv.ues.fia.eisi.trues.ui.global.paso.detalle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.PasoControl;
import sv.ues.fia.eisi.trues.db.control.PersonalControl;
import sv.ues.fia.eisi.trues.db.entity.Paso;
import sv.ues.fia.eisi.trues.db.entity.Personal;
import sv.ues.fia.eisi.trues.ui.global.personal.DetallePersonalFragment;

public class DetallePasoFragment extends DialogFragment implements View.OnClickListener {

    private View view;
    private TextView textViewDescripcion, textViewResponsable;
    private Integer idPaso;
    private Paso paso;
    private Personal personal;
    private CardView cardViewResponsable;

    public static DetallePasoFragment newInstance() {
        return new DetallePasoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        idPaso = getArguments().getInt("idPaso", -1);

        PasoControl pasoControl = new PasoControl(getActivity());
        paso = pasoControl.obtenerPaso(idPaso);

        PersonalControl personalControl = new PersonalControl(getActivity());
        personal = personalControl.consultarPersonal(paso.getIdPersonal());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_detalle_paso, null);
        textViewDescripcion = view.findViewById(R.id.textViewDescripcion);
        textViewDescripcion.setText(paso.getDescripcion());
        textViewResponsable = view.findViewById(R.id.textViewResponsable);
        textViewResponsable.setText(personal.getNombrePersonal());
        cardViewResponsable = view.findViewById(R.id.cardView3);
        cardViewResponsable.setOnClickListener(this);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("idPersonal", personal.getIdPersonal());
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DetallePersonalFragment fragment = new DetallePersonalFragment();
        fragment.setArguments(bundle);
        fragment.show(fragmentManager, "dialog");
    }
}
