package sv.ues.fia.eisi.trues.ui.global;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.AccesoUsuarioControl;
import sv.ues.fia.eisi.trues.db.control.FacultadControl;
import sv.ues.fia.eisi.trues.db.entity.Actividad;
import sv.ues.fia.eisi.trues.db.entity.Documento;
import sv.ues.fia.eisi.trues.db.entity.Paso;
import sv.ues.fia.eisi.trues.db.entity.PasoEstado;
import sv.ues.fia.eisi.trues.db.entity.Requisito;
import sv.ues.fia.eisi.trues.db.entity.Tramite;
import sv.ues.fia.eisi.trues.db.entity.UsuarioTramite;
import sv.ues.fia.eisi.trues.ui.admin.AdministracionFragment;
import sv.ues.fia.eisi.trues.ui.admin.calendario.agregar.AgregarActividadFragment;
import sv.ues.fia.eisi.trues.ui.admin.permisos.PermisosFragment;
import sv.ues.fia.eisi.trues.ui.admin.tramite.agregar.AgregarTramiteFragment;
import sv.ues.fia.eisi.trues.ui.alumno.mispaso.PasoEstadoFragment;
import sv.ues.fia.eisi.trues.ui.global.calendario.lista.ActividadesFragment;

import sv.ues.fia.eisi.trues.ui.alumno.mistramites.lista.MisTramitesFragment;

import sv.ues.fia.eisi.trues.ui.global.documento.lista.DocumentoFragment;
import sv.ues.fia.eisi.trues.ui.global.paso.lista.PasoFragment;
import sv.ues.fia.eisi.trues.ui.global.requisito.RequisitosFragment;
import sv.ues.fia.eisi.trues.ui.global.tramite.lista.TramitesFragment;
import sv.ues.fia.eisi.trues.ui.login.LoginActivity;

public class MenuAdminActivity extends AppCompatActivity implements
        ActividadesFragment.OnListFragmentInteractionListener,
        MisTramitesFragment.OnListFragmentInteractionListener,
        TramitesFragment.OnListFragmentInteractionListener,
        MenuItem.OnMenuItemClickListener,
        PasoFragment.OnListFragmentInteractionListener,
        RequisitosFragment.OnListFragmentInteractionListener,
        DocumentoFragment.OnListFragmentInteractionListener,
        PasoEstadoFragment.OnListFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener, TextToSpeech.OnInitListener {

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private SharedPreferences sharedPreferences;
    private String usuario, nombreFacultad, nombre;
    private Integer facultad;
    private AccesoUsuarioControl accesoUsuarioControl;
    private GoogleApiClient googleApiClient;
    private TextToSpeech mTts;
    private String mensaje = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentActionRecognizeSpeech = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intentActionRecognizeSpeech.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-MX");
                try {
                    startActivityForResult(intentActionRecognizeSpeech, 1);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Tú dispositivo no soporta el reconocimiento por voz", Toast.LENGTH_SHORT).show();
                }
            }

        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_calendario, R.id.nav_agregar_actividad,
                R.id.nav_agregar_tramite, R.id.nav_mis_tramites, R.id.nav_tramites)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        administrarOpciones();

        FacultadControl facultadControl = new FacultadControl(this);
        nombreFacultad = facultadControl.consultarFacultad(facultad).getNombreFacultad();

        View headerView = navigationView.getHeaderView(0);
        TextView textViewHeaderFacultad = headerView.findViewById(R.id.textViewHeaderFacultad);
        textViewHeaderFacultad.setText("Facultad de " + nombreFacultad);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();
    }

    private void administrarOpciones() {
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        usuario = sharedPreferences.getString("usuario", null);
        facultad = sharedPreferences.getInt("facultad", 0);
        nombre = sharedPreferences.getString("nombre", null);
        accesoUsuarioControl = new AccesoUsuarioControl(this);

        navigationView.getMenu().findItem(R.id.nav_calendario).setOnMenuItemClickListener(this);
        navigationView.getMenu().findItem(R.id.nav_agregar_actividad).setOnMenuItemClickListener(this);
        navigationView.getMenu().findItem(R.id.nav_agregar_tramite).setOnMenuItemClickListener(this);
        navigationView.getMenu().findItem(R.id.nav_mis_tramites).setOnMenuItemClickListener(this);
        navigationView.getMenu().findItem(R.id.nav_tramites).setOnMenuItemClickListener(this);
        navigationView.getMenu().findItem(R.id.nav_administracion).setOnMenuItemClickListener(this);
        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(this);

        if(usuario != null){
            if (accesoUsuarioControl.existe(usuario, "00")){
                navigationView.getMenu().findItem(R.id.nav_agregar_tramite).setVisible(true);
            }
            if (accesoUsuarioControl.existe(usuario, "100")){
                navigationView.getMenu().findItem(R.id.nav_agregar_actividad).setVisible(true);
            }
            if (accesoUsuarioControl.existe(usuario, "20")){
                navigationView.getMenu().findItem(R.id.nav_administracion).setVisible(true);
            }
            if (accesoUsuarioControl.existe(usuario,"10")){
                navigationView.getMenu().findItem(R.id.nav_mis_tramites).setVisible(true);
            }
            navigationView.getMenu().findItem(R.id.nav_calendario).setChecked(true);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);

                    if(strSpeech2Text != null){
                        actionSpeech(strSpeech2Text);
                    }
                }
                break;
            case 2:
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    // success, create the TTS instance
                    mTts = new TextToSpeech(this, this);
                } else {
                    // missing data, install it
                    Intent installIntent = new Intent();
                    installIntent.setAction(
                            TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onInit(int status) {
        mTts.setLanguage(new Locale("es", "MX"));
        mTts.speak(mensaje, TextToSpeech.QUEUE_ADD, null);
    }

    private void actionSpeech(String strSpeech2Text) {
        Fragment fragment;
        DialogFragment dialogFragment = null;
        switch (strSpeech2Text){
            ////////////////////////////////////////////////////////////////////////////////////////
            case "Hola trues":
            case "Hola trolls":
            case "Hola":
            case "Hola 3":
                if(nombre != null) {
                    mensaje = getText(R.string.hola) + nombre + "!" + getText(R.string.ayuda).toString();
                } else {
                    mensaje = getText(R.string.hola) + "!";
                }
                break;
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            case "Muéstrame el calendario":
            case "calendario":
                fragment = new ActividadesFragment();
                cambiarFragment(fragment);
                mensaje = getText(R.string.speech_calendario).toString();
                break;
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            case "agregar actividad":
            case "Quiero agregar una actividad":
                if(usuario != null) {
                    if (accesoUsuarioControl.existe(usuario, "100")) {
                        dialogFragment = new AgregarActividadFragment();
                        showDialog(dialogFragment);
                        mensaje = getText(R.string.speech_agregar_actividad).toString();
                    } else {
                        mensaje = getText(R.string.sin_permisos).toString();
                    }
                } else {
                    mensaje = getText(R.string.sin_permisos).toString();
                }
                break;
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            case "tramites":
            case "Muéstrame los trámites":
                fragment = new TramitesFragment();
                cambiarFragment(fragment);
                mensaje = getText(R.string.speech_tramite).toString();
                break;
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            case "agregar trámite":
            case "Quiero agregar un trámite":
                if(usuario != null) {
                    if (accesoUsuarioControl.existe(usuario, "00")) {
                        dialogFragment = new AgregarTramiteFragment();
                        showDialog(dialogFragment);
                        mensaje = getText(R.string.speech_agregar_tramite).toString();
                    } else {
                        mensaje = getText(R.string.sin_permisos).toString();
                    }
                } else {
                    mensaje = getText(R.string.sin_permisos).toString();
                }
                break;
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            case "mis trámites":
            case "Muéstrame mis trámites":
                if(usuario != null) {
                    if (accesoUsuarioControl.existe(usuario,"10")) {
                        fragment = new MisTramitesFragment();
                        cambiarFragment(fragment);
                        mensaje = getText(R.string.speech_mis_tramites).toString();
                    } else {
                        mensaje = getText(R.string.sin_permisos).toString();
                    }
                } else {
                    mensaje = getText(R.string.sin_permisos).toString();
                }
                break;
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            case "administración":
            case "Llévame a la administración":
                if(usuario != null) {
                    if (accesoUsuarioControl.existe(usuario,"20")) {
                        fragment = new AdministracionFragment();
                        cambiarFragment(fragment);
                        mensaje = getText(R.string.speech_administracion).toString();
                    } else {
                        mensaje = getText(R.string.sin_permisos).toString();
                    }
                } else {
                    mensaje = getText(R.string.sin_permisos).toString();
                }
                break;
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            case "cerrar sesión":
            case "quiero cerrar sesión":
                mensaje = getText(R.string.speech_cerrar_sesion).toString();
                logout();
                break;
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            case "ayuda":
            case "quién eres":
            case "Háblame de ti":
            case "cuál es tu función":
            case "cuál es su función":
                mensaje = getText(R.string.sobre_mi).toString();
                break;
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            case "Quién es tu padre":
                mensaje = getText(R.string.padre).toString();
                break;
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            default:
                mensaje = getText(R.string.speech_lo_siento).toString();
                break;
            ////////////////////////////////////////////////////////////////////////////////////////

        }
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, 2);
        //Toast.makeText(getApplicationContext(), strSpeech2Text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        if (usuario!=null){
            if (accesoUsuarioControl.existe(usuario, "121")){
                menu.findItem(R.id.gestionarPermisos).setVisible(true);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.gestionarPermisos:
                PermisosFragment permisosFragment = new PermisosFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                permisosFragment.show(fragmentManager,"dialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Fragment fragment;
        DialogFragment dialogFragment = null;
        switch (menuItem.getItemId()){
            case R.id.nav_calendario:
                fragment = new ActividadesFragment();
                cambiarFragment(fragment);
                break;
            case R.id.nav_agregar_actividad:
                dialogFragment = new AgregarActividadFragment();
                showDialog(dialogFragment);
                break;
            case R.id.nav_agregar_tramite:
                dialogFragment = new AgregarTramiteFragment();
                showDialog(dialogFragment);
                break;
            case R.id.nav_mis_tramites:
                fragment = new MisTramitesFragment();
                cambiarFragment(fragment);
                break;
            case R.id.nav_tramites:
                fragment = new TramitesFragment();
                cambiarFragment(fragment);
                break;
            case R.id.nav_administracion:
                fragment = new AdministracionFragment();
                cambiarFragment(fragment);
                break;
            case R.id.nav_logout:
                logout();
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cambiarFragment(Fragment fragment) {
        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, fragment).commit();
        }
    }
    private void showDialog(DialogFragment dialogFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentManager, "dialog");
    }


    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", null);
        editor.putBoolean("tipoUsuario", false);
        editor.putString("nombre", null);
        editor.putString("apellido", null);
        editor.putInt("facultad", -1);

        editor.commit();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });

        LoginManager.getInstance().logOut();

        Intent intentLogin = new Intent(this, LoginActivity.class);
        startActivity(intentLogin);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onListFragmentInteraction(Actividad item) {

    }

    @Override
    public void onListFragmentInteraction(Tramite item) {

    }

    @Override
    public void onListFragmentInteraction(Paso item) {

    }

    @Override
    public void onListFragmentInteraction(Requisito item) {

    }

    @Override
    public void onListFragmentInteraction(Documento item) {

    }

    @Override
    public void onListFragmentInteraction(UsuarioTramite item) {

    }

    @Override
    public void onListFragmentInteraction(PasoEstado item) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
