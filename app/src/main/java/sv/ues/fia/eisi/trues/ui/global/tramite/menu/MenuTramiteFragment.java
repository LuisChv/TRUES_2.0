package sv.ues.fia.eisi.trues.ui.global.tramite.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.AccesoUsuarioControl;
import sv.ues.fia.eisi.trues.db.control.TramiteControl;
import sv.ues.fia.eisi.trues.db.control.UsuarioTramiteControl;
import sv.ues.fia.eisi.trues.db.entity.Tramite;
import sv.ues.fia.eisi.trues.ui.global.documento.lista.DocumentoFragment;
import sv.ues.fia.eisi.trues.ui.global.paso.lista.PasoFragment;
import sv.ues.fia.eisi.trues.ui.global.requisito.RequisitosFragment;

public class MenuTramiteFragment extends Fragment implements View.OnClickListener {

    private View view;
    private CardView cardViewPasos, cardViewRequisitos, cardViewDocs, cardViewIniciar;
    private TextView textViewNombreTramite;
    private Tramite tramite;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String usuario;
    private AccesoUsuarioControl accesoUsuarioControl;
    private UsuarioTramiteControl usuarioTramiteControl;
    private ImageView imageViewIniciar;
    private TextView textViewIniciar;

    public static MenuTramiteFragment newInstance() {
        return new MenuTramiteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu_tramite, container, false);

        context = getActivity();

        tramite = new Tramite();

        TramiteControl tramiteControl = new TramiteControl(context);
        tramite = tramiteControl.consultarTramite(getArguments().getInt("idTramite", -1));

        textViewNombreTramite = view.findViewById(R.id.textViewNombreTramite);
        textViewNombreTramite.setText(tramite.getNombreTramite());
        textViewIniciar = view.findViewById(R.id.textView5);

        cardViewPasos = view.findViewById(R.id.cardViewPasos);
        cardViewPasos.setOnClickListener(this);
        cardViewRequisitos = view.findViewById(R.id.cardViewRequisitos);
        cardViewRequisitos.setOnClickListener(this);
        cardViewDocs = view.findViewById(R.id.cardViewDocs);
        cardViewDocs.setOnClickListener(this);
        cardViewIniciar = view.findViewById(R.id.cardViewIniciar);
        cardViewIniciar.setOnClickListener(this);

        imageViewIniciar = view.findViewById(R.id.imageViewIniciar);

        sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        usuario = sharedPreferences.getString("usuario", null);

        accesoUsuarioControl = new AccesoUsuarioControl(context);
        usuarioTramiteControl = new UsuarioTramiteControl(context);

        if (usuario != null){
            if (accesoUsuarioControl.existe(usuario, "10")){
                cardViewIniciar.setVisibility(View.VISIBLE);
            }
            if (usuarioTramiteControl.existeUTramite(usuario, tramite.getIdTramite())){
                imageViewIniciar.setImageResource(R.drawable.ic_clock);
                textViewIniciar.setText("En curso");
                cardViewIniciar.setEnabled(false);
            }
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("idTramite", tramite.getIdTramite());
        Fragment fragment = null;
        switch (view.getId()){
            case R.id.cardViewPasos:
                fragment = new PasoFragment();
                fragment.setArguments(bundle);
                break;
            case R.id.cardViewRequisitos:
                fragment = new RequisitosFragment();
                fragment.setArguments(bundle);
                break;
            case R.id.cardViewDocs:
                fragment = new DocumentoFragment();
                fragment.setArguments(bundle);
                break;
            case R.id.cardViewIniciar:
                usuarioTramiteControl.crearUTramite(usuario, tramite.getIdTramite());
                imageViewIniciar.setImageResource(R.drawable.ic_clock);
                textViewIniciar.setText("En curso");
                cardViewIniciar.setEnabled(false);
                Toast.makeText(getActivity().getApplicationContext(), "Has iniciado este proceso.", Toast.LENGTH_LONG).show();
                break;
        }
        if (fragment != null){
            getActivity().getSupportFragmentManager()
                    .beginTransaction().add(R.id.mainContainer, fragment)
                    .addToBackStack(null).commit();
        }

    }
}
