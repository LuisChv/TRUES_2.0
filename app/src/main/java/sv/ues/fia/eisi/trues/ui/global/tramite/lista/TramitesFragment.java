package sv.ues.fia.eisi.trues.ui.global.tramite.lista;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.AccesoUsuarioControl;
import sv.ues.fia.eisi.trues.db.control.TramiteControl;
import sv.ues.fia.eisi.trues.db.entity.Tramite;
import sv.ues.fia.eisi.trues.ui.admin.tramite.actualizar.ActualizarTramiteFragment;
import sv.ues.fia.eisi.trues.ui.admin.tramite.eliminar.EliminarTramiteFragment;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TramitesFragment extends Fragment implements tRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyTramiteRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private List<Tramite> tramiteList;
    private SharedPreferences sharedPreferences;
    private String usuario;
    private Integer facultad;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TramitesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TramitesFragment newInstance(int columnCount) {
        TramitesFragment fragment = new TramitesFragment();
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

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        usuario = sharedPreferences.getString("usuario", null);
        facultad = sharedPreferences.getInt("facultad", -1);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tramites_list, container, false);

        recyclerView = view.findViewById(R.id.list);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.title_tramites));

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            Integer actividad = null;
            try {
                Bundle bundle = this.getArguments();
                actividad = bundle.getInt("actividad", -1);
            }catch (Exception e){
            }

            TramiteControl tramiteControl = new TramiteControl(getActivity());
            tramiteList = new ArrayList<>();
            if (actividad != null){
                tramiteList = tramiteControl.obtenerTramiteActividad(actividad);
            }else {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                Integer facultad = sharedPreferences.getInt("facultad", -1);
                if (facultad>0){
                    tramiteList = (tramiteControl.ObtenerTramites(facultad));
                }
            }

            adapter = new MyTramiteRecyclerViewAdapter(tramiteList, mListener);

            AccesoUsuarioControl accesoUsuarioControl = new AccesoUsuarioControl(context);


            if (usuario != null){
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);

                //Verifica si tiene permisos para editar actividad
                if (accesoUsuarioControl.existe(usuario, "02")){
                    ItemTouchHelper.SimpleCallback simpleCallbackR = new tRecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this, context);
                    new ItemTouchHelper(simpleCallbackR).attachToRecyclerView(recyclerView);
                }

                //Verifica si tiene permisos para eliminar actividad
                if (accesoUsuarioControl.existe(usuario, "03")){
                    ItemTouchHelper.SimpleCallback simpleCallbackL = new tRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this, context);
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
        Bundle bundle = new Bundle();
        bundle.putInt("idTramite", tramiteList.get(viewHolder.getAdapterPosition()).getIdTramite());
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogFragment fragment = null;
        switch (direction){
            case ItemTouchHelper.RIGHT:
                fragment = new ActualizarTramiteFragment();
                break;
            case ItemTouchHelper.LEFT:
                fragment = new EliminarTramiteFragment();
                break;
        }
        fragment.setArguments(bundle);
        fragment.show(fragmentManager, "dialog");
        adapter.actualizar();
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
        void onListFragmentInteraction(Tramite item);
    }
}
