package sv.ues.fia.eisi.trues.ui.admin.permisos;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.AccesoUsuarioControl;
import sv.ues.fia.eisi.trues.db.control.OpcionCrudControl;
import sv.ues.fia.eisi.trues.db.control.UsuarioControl;
import sv.ues.fia.eisi.trues.db.entity.OpcionCrud;
import sv.ues.fia.eisi.trues.db.entity.Usuario;

public class AsignarPermisoFragment extends DialogFragment {
    private Boolean accion;
    private SharedPreferences sharedPreferences;
    private Integer idFacultad;
    private Button buttonCancelar, buttonGuardar;
    private Spinner spinnerUsuario, spinnerPermiso;
    private List<Usuario> usuarioList;
    private List<OpcionCrud> opcionList;
    private List<String> usuarios, permisos;
    private View view;
    private String usuario;
    private TextView textView;

    public static AsignarPermisoFragment newInstance() {
        return new AsignarPermisoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        idFacultad = sharedPreferences.getInt("facultad", -1);

        accion = getArguments().getBoolean("accion");

        UsuarioControl usuarioControl = new UsuarioControl(getActivity());
        usuarioList = usuarioControl.obtenerUsuarios(idFacultad);

        usuarios = new ArrayList<>();
        usuarios.add(getText(R.string.seleccionar_usuario).toString());
        for (int i = 0; i<usuarioList.size(); i++){
            usuarios.add(usuarioList.get(i).getUsuario());
        }

        ArrayAdapter<CharSequence> adapterU = new  ArrayAdapter(getActivity(), R.layout.style_spinner, usuarios);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_asignar_permiso, null);
        if (!accion) {
            textView = view.findViewById(R.id.textView15);
            textView.setText(getText(R.string.quitar_permisos));
        }
        spinnerPermiso = view.findViewById(R.id.spinnerPermiso);
        spinnerUsuario = view.findViewById(R.id.spinnerUsuario);
        spinnerUsuario.setAdapter(adapterU);
        spinnerUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    usuario = usuarioList.get(position-1).getUsuario();

                    spinnerPermiso.setVisibility(View.VISIBLE);
                    OpcionCrudControl opcionCrudControl = new OpcionCrudControl(getActivity());
                    if (accion) {
                        opcionList = opcionCrudControl.obtenerPermisos();
                    } else {
                        opcionList = opcionCrudControl.obtenerPermisos(usuario);
                    }
                    permisos = new ArrayList<>();
                    permisos.add("Seleccione un permiso...");
                    for (int i = 0; i<opcionList.size(); i++){
                        permisos.add(opcionList.get(i).getDesOpcion());
                    }

                    ArrayAdapter<CharSequence> adapterP = new  ArrayAdapter(getActivity(), R.layout.style_spinner, permisos);
                    spinnerPermiso.setAdapter(adapterP);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer posicionPermiso = spinnerPermiso.getSelectedItemPosition();
                Integer posicionUsuario = spinnerUsuario.getSelectedItemPosition();
                if (posicionPermiso>0 && posicionUsuario>0){
                    AccesoUsuarioControl accesoUsuarioControl = new AccesoUsuarioControl(getActivity());
                    String usuario = usuarioList.get(posicionUsuario-1).getUsuario();
                    String permiso = opcionList.get(posicionPermiso-1).getIdOpcion();
                    if (accion) {
                        accesoUsuarioControl.CrearAccesoUsuario2(usuario, permiso);
                        if(verificarInternet()){
                            accesoUsuarioControl.CrearAccesoUsuario2WS(usuario, permiso);
                        }else {
                            String parametros = "asignar;"+usuario+";"+permiso;
                            writeToFile(parametros, getActivity().getApplicationContext(),"AccesoUsuario");
                        }
                    } else {
                        accesoUsuarioControl.eliminarPermiso(usuario, permiso);
                        if(verificarInternet()){
                            accesoUsuarioControl.eliminarPermisoWS(usuario, permiso);
                        }else {
                            String parametros = "eliminar;"+usuario+";"+permiso;
                            writeToFile(parametros, getActivity().getApplicationContext(),"AccesoUsuario");
                        }
                    }
                    dismiss();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), getText(R.string.datos_incorrectos), Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCancelar = view.findViewById(R.id.buttonCancelar);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        builder.setView(view);

        return builder.create();
    }
    private boolean verificarInternet() {
        Boolean HayInternet;
        if (isOnlineNet()) {
            //Toast.makeText(context, "Hay internet y está conectado", Toast.LENGTH_SHORT).show();
            HayInternet = true;
        } else {
            //Toast.makeText(context, "No hay internet y está conectado", Toast.LENGTH_SHORT).show();
            HayInternet = false;
        }
        return HayInternet;
    }
    private boolean isOnlineNet() {
        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");
            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void writeToFile(String data, Context context,String archivo) {
        //Toast.makeText(context,readFromFile(context),Toast.LENGTH_LONG).show();
        String prueba = readFromFile(context,archivo) + data;

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(archivo+".txt", Context.MODE_PRIVATE));
            outputStreamWriter.append(prueba);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        String after = readFromFile(context,archivo);
        Toast.makeText(context, after, Toast.LENGTH_SHORT).show();
    }

    private String readFromFile(Context context,String archivo) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(archivo+".txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    //stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("AgregarActividFragment", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarActividFragment", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }

}
