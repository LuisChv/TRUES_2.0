package sv.ues.fia.eisi.trues.ui.login.invitado;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.FacultadControl;
import sv.ues.fia.eisi.trues.db.control.UsuarioControl;
import sv.ues.fia.eisi.trues.db.entity.Facultad;
import sv.ues.fia.eisi.trues.db.entity.Usuario;
import sv.ues.fia.eisi.trues.ui.global.MenuAdminActivity;
import sv.ues.fia.eisi.trues.ui.login.LoginActivity;

public class FacultadGFragment extends DialogFragment {

    private View view;
    private Spinner spinnerFacultad;
    private FacultadControl facultadControl;
    private List<Facultad> facultadList;
    private List<String> facultades;
    private SharedPreferences sharedPreferences;
    private String username, nombre, foto;
    private EditText editTextContraseña, editTextConfirmar;
    private ImageView imageView;
    private Button login;

    public static FacultadGFragment newInstance() {
        return new FacultadGFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_facultad_g, null);
        spinnerFacultad = view.findViewById(R.id.spinnerFacultad);

        facultadControl = new FacultadControl(getActivity());
        facultadList = facultadControl.consultarFacultades();

        facultades = new ArrayList<>();
        facultades.add(getText(R.string.seleccionar_facultad).toString());
        for (int i = 0; i<facultadList.size(); i++){
            facultades.add(facultadList.get(i).getNombreFacultad());
        }

        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter(getActivity(), R.layout.style_spinner, facultades);
        spinnerFacultad.setAdapter(arrayAdapter);

        builder.setView(view);
        // Create the AlertDialog object and return it

        username = getArguments().getString("username", null);
        nombre = getArguments().getString("nombre", null);
        foto = getArguments().getString("foto", null);

        editTextContraseña = view.findViewById(R.id.editTextTextPassword2);
        editTextConfirmar = view.findViewById(R.id.editTextTextConfirmar);
        imageView = view.findViewById(R.id.imageView19);
        if (foto != null){
            Picasso.with(getActivity()).load(Uri.parse(foto)).error(R.drawable.ic_user).into(imageView);
        }
        login = view.findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contraseña = editTextContraseña.getText().toString();
                String contraseña2 = editTextConfirmar.getText().toString();
                Integer idFacultad = spinnerFacultad.getSelectedItemPosition();

                if (contraseña.equals(contraseña2) && idFacultad > 0 && contraseña.length() > 5){
                    Usuario usuario = new Usuario(username, contraseña, nombre, "", false, idFacultad);

                    UsuarioControl control = new UsuarioControl(getActivity());
                    control.crearUsuario(usuario);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("usuario", usuario.getUsuario());
                    editor.putString("nombre", usuario.getNombre());
                    editor.putString("apellido", usuario.getApellido());
                    editor.putBoolean("tipoUsuario", usuario.getTipo());
                    editor.putInt("facultad", usuario.getIdFacultad());
                    editor.commit();

                    Intent intent = new Intent(getActivity(), MenuAdminActivity.class);

                    Toast.makeText(getActivity(), getText(R.string.bienvenido2) + usuario.getNombre() + "!", Toast.LENGTH_LONG).show();

                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), getText(R.string.datos_incorrectos), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return builder.create();
    }

}