package sv.ues.fia.eisi.trues.ui.global.calendario.lista;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.AccesoUsuarioControl;
import sv.ues.fia.eisi.trues.db.control.ActividadControl;
import sv.ues.fia.eisi.trues.db.control.ActividadTramiteControl;
import sv.ues.fia.eisi.trues.db.entity.Actividad;
import sv.ues.fia.eisi.trues.db.entity.ActividadTramite;
import sv.ues.fia.eisi.trues.ui.admin.calendario.actualizar.ActualizarActividadFragment;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ActividadesFragment extends Fragment implements aRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyActividadRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private List<Actividad> actividadList;
    private SharedPreferences sharedPreferences;
    private String usuario;
    private Integer facultad;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ActividadesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ActividadesFragment newInstance(int columnCount) {
        ActividadesFragment fragment = new ActividadesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        context = getActivity();

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        usuario = sharedPreferences.getString("usuario", null);
        facultad = sharedPreferences.getInt("facultad", -1);

        init();
    }

    private void init() {
        ActividadControl actividadControl = new ActividadControl(context);
        actividadList = new ArrayList<>();

        if (facultad>0){
            actividadList = actividadControl.ObtenerActividades(facultad);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actividades_list, container, false);

        recyclerView = view.findViewById(R.id.list);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.title_calendario));

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            adapter = new MyActividadRecyclerViewAdapter(actividadList, mListener);

            AccesoUsuarioControl accesoUsuarioControl = new AccesoUsuarioControl(context);

            //Verifica si ha iniciado sesión

            if (usuario != null){
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);

                //Verifica si tiene permisos para editar actividad
                if (accesoUsuarioControl.existe(usuario, "102")){
                    ItemTouchHelper.SimpleCallback simpleCallbackR = new aRecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this, context);
                    new ItemTouchHelper(simpleCallbackR).attachToRecyclerView(recyclerView);
                }

                //Verifica si tiene permisos para eliminar actividad
                if (accesoUsuarioControl.existe(usuario, "103")){
                    ItemTouchHelper.SimpleCallback simpleCallbackL = new aRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this, context);
                    new ItemTouchHelper(simpleCallbackL).attachToRecyclerView(recyclerView);
                }
            }

            recyclerView.setAdapter(adapter);

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof MyActividadRecyclerViewAdapter.ViewHolder){
            Integer id = actividadList.get(viewHolder.getAdapterPosition()).getIdActividad();
            switch (direction){
                case ItemTouchHelper.LEFT:
                    final Actividad actividad = actividadList.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    ActividadTramiteControl actividadTramiteControl = new ActividadTramiteControl(context);
                    List<ActividadTramite> actividadTramites = actividadTramiteControl.ObtenerActividadesTramites(actividad.getIdActividad());

                    adapter.removeItem(viewHolder.getAdapterPosition());
                    recuperarItemBorrado(viewHolder, id, actividad, deletedIndex, actividadTramites);
                    break;
                case ItemTouchHelper.RIGHT:
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    ActualizarActividadFragment actualizarActividadFragment = new ActualizarActividadFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("actividad", id);
                    actualizarActividadFragment.setArguments(bundle);
                    actualizarActividadFragment.show(fragmentManager, "");
                    adapter.actualizar();
                    break;

            }

        }
    }


    private void recuperarItemBorrado(RecyclerView.ViewHolder viewHolder, Integer id,
                                      final Actividad actividad, final int deletedIntex, final List<ActividadTramite> actividadTramites){

        Snackbar snackbar = Snackbar.make(((MyActividadRecyclerViewAdapter.ViewHolder)viewHolder).layoutABorrar,
                actividad.getIdActividad() + " eliminado", Snackbar.LENGTH_LONG);

        snackbar.setAction("Deshacer", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.restoreItem(actividad, deletedIntex, actividadTramites);
            }
        });

        snackbar.setActionTextColor(Color.rgb(88, 165, 240));
        snackbar.show();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Actividad item);
    }
}
