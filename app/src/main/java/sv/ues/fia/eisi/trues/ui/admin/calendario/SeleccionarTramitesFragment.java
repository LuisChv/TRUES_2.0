package sv.ues.fia.eisi.trues.ui.admin.calendario;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
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
import sv.ues.fia.eisi.trues.db.control.ActividadTramiteControl;
import sv.ues.fia.eisi.trues.db.control.TramiteControl;
import sv.ues.fia.eisi.trues.db.entity.Tramite;

public class SeleccionarTramitesFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private Spinner spinnerTramite;
    private TramiteControl tramiteControl;
    private List<Tramite> tramiteList;
    private List<String> tramites;
    private SharedPreferences sharedPreferences;
    private Integer facultad;
    private Integer actividad;
    private Context context;
    private Boolean accion;

    public static SeleccionarTramitesFragment newInstance() {
        return new SeleccionarTramitesFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_seleccionar_tramites, null);
        spinnerTramite = view.findViewById(R.id.spinnerTramite);

        sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        facultad = sharedPreferences.getInt("facultad", -1);

        actividad = getArguments().getInt("idActividad", -1);
        accion = getArguments().getBoolean("accion", false); //TRUE = quitar

        tramiteControl = new TramiteControl(getActivity());
        if (accion){
            tramiteList = tramiteControl.obtenerTramiteActividad(actividad);
        } else {
            tramiteList = tramiteControl.ObtenerTramites(facultad);
        }

        tramites = new ArrayList<>();
        tramites.add("Seleccione un trámite...");
        for (int i = 0; i<tramiteList.size(); i++){
            tramites.add(tramiteList.get(i).getNombreTramite());
        }

        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter(getActivity(), R.layout.style_spinner, tramites);
        spinnerTramite.setAdapter(arrayAdapter);
        spinnerTramite.setOnItemSelectedListener(this);

        builder.setView(view);
        // Create the AlertDialog object and return it

        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position>0){
            ActividadTramiteControl actividadTramiteControl = new ActividadTramiteControl(context);
            Integer tramite = tramiteList.get(position-1).getIdTramite();
            if (accion){
                actividadTramiteControl.EliminarActividadTramite(actividad, tramite);
            }else {
                actividadTramiteControl.CrearActividadTramite(actividad, tramite);
            }
            this.dismiss();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
