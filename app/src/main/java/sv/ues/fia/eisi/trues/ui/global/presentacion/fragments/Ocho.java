package sv.ues.fia.eisi.trues.ui.global.presentacion.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.ui.login.LoginActivity;

public class Ocho extends Fragment implements View.OnClickListener {

    private Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.ocho, container, false);

        button = rootView.findViewById(R.id.button2);
        button.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}