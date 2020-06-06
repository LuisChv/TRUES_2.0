package sv.ues.fia.eisi.trues.ui.alumno.mispaso;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.UsuarioPasoControl;
import sv.ues.fia.eisi.trues.db.entity.PasoEstado;
import sv.ues.fia.eisi.trues.db.entity.UsuarioPaso;
import sv.ues.fia.eisi.trues.ui.alumno.mispaso.pRecyclerItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PasoEstadoFragment extends Fragment implements pRecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyPasoEstadoRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private List<PasoEstado> pasoEstadoList;
    private String usuario;
    private Integer idTramite;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PasoEstadoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PasoEstadoFragment newInstance(int columnCount) {
        PasoEstadoFragment fragment = new PasoEstadoFragment();
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
        usuario = getActivity()
                .getSharedPreferences("login", Context.MODE_PRIVATE)
                .getString("usuario", null);
        idTramite = getArguments().getInt("idTramite", -1);
        context = getActivity();
        init();
    }


    private void init() {
        UsuarioPasoControl usuarioPasoControl = new UsuarioPasoControl(getActivity());
        pasoEstadoList = new ArrayList<>();

        if (usuario != null && idTramite >0){
            pasoEstadoList = (usuarioPasoControl.obtenerMisPasos(usuario, idTramite));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paso_estado_list, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.pasos));

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;

            adapter = new MyPasoEstadoRecyclerViewAdapter(pasoEstadoList, mListener);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            ItemTouchHelper.SimpleCallback simpleCallbackL = new pRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this, context);
            new ItemTouchHelper(simpleCallbackL).attachToRecyclerView(recyclerView);

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
        adapter.setEstado(viewHolder.getAdapterPosition());
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
        void onListFragmentInteraction(PasoEstado item);
    }
}
