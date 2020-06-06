package sv.ues.fia.eisi.trues.ui.admin;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.ui.admin.personal.AgregarPersonalFragment;
import sv.ues.fia.eisi.trues.ui.admin.personal.lista.PersonalFragment;

public class AdministracionFragment extends Fragment implements View.OnClickListener {

    private View view;
    private CardView cardView1, cardView2;

    public static AdministracionFragment newInstance() {
        return new AdministracionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_administracion, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.title_administracion));

        cardView1 = view.findViewById(R.id.cardView1);
        cardView2 = view.findViewById(R.id.cardView2);

        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        DialogFragment dialogFragment = null;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = null;
        switch (v.getId()){
            case R.id.cardView1:
                dialogFragment = new AgregarPersonalFragment();
                break;
            case R.id.cardView2:
                fragment = new PersonalFragment();
                break;
        }
        if (dialogFragment != null){
            dialogFragment.show(fragmentManager, "dialog");
        }
        if (fragment != null){
            getActivity().getSupportFragmentManager().
                    beginTransaction().replace(R.id.mainContainer, fragment).addToBackStack(null).commit();
        }
    }
}