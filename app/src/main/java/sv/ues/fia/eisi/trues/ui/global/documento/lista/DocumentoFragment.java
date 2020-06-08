package sv.ues.fia.eisi.trues.ui.global.documento.lista;

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

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.AccesoUsuarioControl;
import sv.ues.fia.eisi.trues.db.control.DocumentoControl;
import sv.ues.fia.eisi.trues.db.entity.Documento;
import sv.ues.fia.eisi.trues.ui.admin.documentos.actualizar.ActualizarDocumentoFragment;
import sv.ues.fia.eisi.trues.ui.admin.documentos.eliminar.CEliminarDocumentoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DocumentoFragment extends Fragment implements dRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyDocumentoRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<Documento> documentoList;
    private Integer idTramite;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sharedPreferences;
    private String usuario;
    private Context context;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DocumentoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DocumentoFragment newInstance(int columnCount) {
        DocumentoFragment fragment = new DocumentoFragment();
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
        idTramite = idTramite = this.getArguments().getInt("idTramite", -1);;

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        usuario = sharedPreferences.getString("usuario", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documento_list, container, false);

        recyclerView = view.findViewById(R.id.list);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.documentos));

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            recyclerView = (RecyclerView) view;

            documentoList = new ArrayList<>();
            DocumentoControl documentoControl = new DocumentoControl(getActivity());
            documentoList = (documentoControl.consultarDocumentos(idTramite));
            adapter = new MyDocumentoRecyclerViewAdapter(documentoList, mListener);

            AccesoUsuarioControl accesoUsuarioControl = new AccesoUsuarioControl(context);


            if (usuario != null){
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);

                //Verifica si tiene permisos para editar requisito
                if (accesoUsuarioControl.existe(usuario, "52")){
                    ItemTouchHelper.SimpleCallback simpleCallbackR = new dRecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this, context);
                    new ItemTouchHelper(simpleCallbackR).attachToRecyclerView(recyclerView);
                }

                //Verifica si tiene permisos para eliminar requisito
                if (accesoUsuarioControl.existe(usuario, "53")){
                    ItemTouchHelper.SimpleCallback simpleCallbackL = new dRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this, context);
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
        bundle.putInt("idDocumento", documentoList.get(viewHolder.getAdapterPosition()).getIdDocumento());
        bundle.putInt("idTramite", idTramite);
        switch (direction){
            case ItemTouchHelper.LEFT:
                fragment = new CEliminarDocumentoFragment();
                break;
            case ItemTouchHelper.RIGHT:
                fragment = new ActualizarDocumentoFragment();
                break;
        }
        fragment.setArguments(bundle);
        fragment.show(fragmentManager, "dialog");
        actualizar();
    }

    private void actualizar() {
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
        void onListFragmentInteraction(Documento item);
    }
}
