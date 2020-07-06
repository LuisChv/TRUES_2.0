package sv.ues.fia.eisi.trues.ui.admin.permisos;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.AccesoUsuarioControl;
import sv.ues.fia.eisi.trues.db.control.ActividadControl;
import sv.ues.fia.eisi.trues.ui.global.calendario.lista.ActividadesFragment;

public class PermisosFragment extends DialogFragment {

    private View view;
    private Context context;
    private CardView cardView5, cardView6;
    private SharedPreferences sharedPreferences;
    private Integer idFacultad;
    private Bundle bundle;
    private String usuario;

    public static PermisosFragment newInstance() {
        return new PermisosFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        idFacultad = sharedPreferences.getInt("facultad", -1);
        usuario = sharedPreferences.getString("usuario", null);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_permisos, null);

        final DialogFragment fragment = new AsignarPermisoFragment();
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        bundle = new Bundle();

        cardView5 = view.findViewById(R.id.cardView5);
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putBoolean("accion", false);
                fragment.setArguments(bundle);
                fragment.show(fragmentManager, "dialogo");
            }
        });
        cardView6 = view.findViewById(R.id.cardView6);
        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putBoolean("accion", true);
                fragment.setArguments(bundle);
                fragment.show(fragmentManager, "dialogo");
            }
        });

        AccesoUsuarioControl control = new AccesoUsuarioControl(getActivity());
        if (!control.existe(usuario, "120")){
            cardView6.setEnabled(false);
        }
        if (!control.existe(usuario, "123")){
            cardView5.setEnabled(false);
        }


        builder.setView(view);

        context = getActivity();
        return builder.create();
    }

}
