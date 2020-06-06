package sv.ues.fia.eisi.trues.ui.admin.personal.lista;

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

import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.PersonalControl;
import sv.ues.fia.eisi.trues.db.entity.Personal;
import sv.ues.fia.eisi.trues.ui.admin.personal.ActualizarPersonalFragment;
import sv.ues.fia.eisi.trues.ui.admin.personal.EliminarPersonalFragment;

/**
 * A fragment representing a list of Items.
 */
public class PersonalFragment extends Fragment implements pRecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private Context context;
    private MyPersonalRecyclerViewAdapter adapter;
    private List<Personal> personalList;
    private PersonalControl control;
    private Integer idFacultad;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    public PersonalFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PersonalFragment newInstance(int columnCount) {
        PersonalFragment fragment = new PersonalFragment();
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
        idFacultad = sharedPreferences.getInt("facultad", -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_list, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.title_personal));

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            control = new PersonalControl(getActivity());
            personalList = control.consultarTodoPersonal(idFacultad);
            ItemTouchHelper.SimpleCallback simpleCallbackR = new pRecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this, context);
            new ItemTouchHelper(simpleCallbackR).attachToRecyclerView(recyclerView);
            ItemTouchHelper.SimpleCallback simpleCallbackL = new pRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this, context);
            new ItemTouchHelper(simpleCallbackL).attachToRecyclerView(recyclerView);

            adapter = new MyPersonalRecyclerViewAdapter(personalList);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        DialogFragment fragment = null;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putInt("idPersonal", personalList.get(viewHolder.getAdapterPosition()).getIdPersonal());
        switch (direction){
            case ItemTouchHelper.RIGHT:
                fragment = new ActualizarPersonalFragment();
                break;
            case ItemTouchHelper.LEFT:
                fragment = new EliminarPersonalFragment();
                break;
        }
        if (fragment != null){
            fragment.setArguments(bundle);
            fragment.show(fragmentManager, "dialog");
        }
        adapter.notifyDataSetChanged();
    }
}