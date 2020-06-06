package sv.ues.fia.eisi.trues.ui.global.paso.lista;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.AccesoUsuarioControl;
import sv.ues.fia.eisi.trues.db.control.PasoControl;
import sv.ues.fia.eisi.trues.db.entity.Paso;
import sv.ues.fia.eisi.trues.ui.admin.paso.actualizar.ActualizarPasoFragment;
import sv.ues.fia.eisi.trues.ui.admin.paso.eliminar.CEliminarPasoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PasoFragment extends Fragment implements pRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Paso> pasoList;
    private Integer idTramite;
    private PasoRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String usuario;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PasoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PasoFragment newInstance(int columnCount) {
        PasoFragment fragment = new PasoFragment();
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
        idTramite = this.getArguments().getInt("idTramite", -1);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        usuario = sharedPreferences.getString("usuario", null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paso_list, container, false);

        recyclerView = view.findViewById(R.id.list);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.pasos));

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            pasoList=new ArrayList<>();
            PasoControl pasoControl=new PasoControl(getContext());
            pasoList = pasoControl.obtenerPasos(idTramite);

            adapter = new PasoRecyclerViewAdapter(pasoList, mListener);

            AccesoUsuarioControl accesoUsuarioControl = new AccesoUsuarioControl(context);

            if (usuario != null){
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);

                //Verifica si tiene permisos para editar paso
                if (accesoUsuarioControl.existe(usuario, "22")){
                    ItemTouchHelper.SimpleCallback simpleCallbackR = new pRecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this, context);
                    new ItemTouchHelper(simpleCallbackR).attachToRecyclerView(recyclerView);
                }

                //Verifica si tiene permisos para eliminar paso
                if (accesoUsuarioControl.existe(usuario, "23")){
                    ItemTouchHelper.SimpleCallback simpleCallbackL = new pRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this, context);
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
        DialogFragment fragment = null;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putInt("idPaso", pasoList.get(viewHolder.getAdapterPosition()).getIdPaso());
        switch (direction){
            case ItemTouchHelper.LEFT:
                fragment = new CEliminarPasoFragment();
                break;
            case ItemTouchHelper.RIGHT:
                fragment = new ActualizarPasoFragment();
                break;
        }
        fragment.setArguments(bundle);
        fragment.show(fragmentManager, "dialog");
        actualizar();
    }

    public void actualizar(){
        adapter.notifyDataSetChanged();
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
        void onListFragmentInteraction(Paso item);
    }
}
