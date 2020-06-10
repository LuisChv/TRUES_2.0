package  sv.ues.fia.eisi.trues.ui.login;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.net.Authenticator;
import java.security.AuthProvider;
import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.LlenarBD;
import sv.ues.fia.eisi.trues.db.control.UsuarioControl;
import sv.ues.fia.eisi.trues.db.entity.Usuario;
import sv.ues.fia.eisi.trues.ui.global.MenuAdminActivity;
import sv.ues.fia.eisi.trues.ui.login.invitado.FacultadGFragment;
import sv.ues.fia.eisi.trues.ui.login.invitado.FacultadPreferidaFragment;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private LoginViewModel loginViewModel;
    private Context context;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ImageView imageViewBarcode;
    private String barcode;
    private ImageView imageViewFb, imageViewG;
    private GoogleApiClient googleApiClient;
    private UsuarioControl usuarioControl;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        context = this;

        usuarioControl = new UsuarioControl(context);

        //VERIFICAR SI HAY SESIÓN ACTIVA
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        if (sharedPreferences.getString("usuario", null) != null){
            Intent intent = new Intent(context, MenuAdminActivity.class);
            startActivity(intent);
            finish();
        }

        LoginManager.getInstance().logOut();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setLoginBehavior(LoginBehavior.WEB_ONLY);
        LoginManager.getInstance().registerCallback(callbackManager,new FacebookCallback<LoginResult>(){
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        iniciarFB();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                iniciarFB();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    iniciarFB();
                }
            }
        };

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();



        usernameEditText = findViewById(R.id.username);
        usernameEditText.setOnFocusChangeListener(this);
        passwordEditText = findViewById(R.id.password);
        passwordEditText.setOnFocusChangeListener(this);
        imageViewBarcode = findViewById(R.id.imageViewBarcode);
        imageViewBarcode.setOnClickListener(this);
        imageViewG = findViewById(R.id.imageViewG);
        imageViewG.setOnClickListener(this);

        final Button loginButton = findViewById(R.id.login);
        final Button invitadoButton = findViewById(R.id.buttonInvitado);

        getSupportActionBar().hide();

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), context);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(this);
        invitadoButton.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (requestCode == 777){

            GoogleSignInResult resultG = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(resultG);

        }
        else if (result != null){

            if(result.getContents() != null){
                barcode = result.getContents();
                Usuario usuario = usuarioControl.iniciarSesionBC(barcode);
                if(usuario != null){
                    iniciarSesion(usuario);
                } else {
                    Toast.makeText(this, getText(R.string.error_usuario), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, getText(R.string.error_barra), Toast.LENGTH_SHORT).show();
            }

        }
        else if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void iniciarFB() {
        if (AccessToken.getCurrentAccessToken() != null) {
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                Usuario usuario = usuarioControl.iniciarSesionBC(profile.getFirstName()+profile.getLastName() + "@facebook.com");
                if (usuario != null){
                    iniciarSesion(usuario);
                }
                else {
                    String foto = null;
                    if (profile.getProfilePictureUri(150, 150) != null){
                        foto = profile.getProfilePictureUri(150, 150).toString();
                    }
                    registrarUsuarioG(
                            profile.getFirstName()+profile.getLastName() + "@facebook.com",
                            profile.getName(), profile.getId(),
                            foto);
                }

            } else {
                Profile.fetchProfileForCurrentAccessToken();
            }
        }
    }


    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void launchDialog(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FacultadPreferidaFragment facultadPreferidaFragment = new FacultadPreferidaFragment();
        facultadPreferidaFragment.show(fragmentManager, "Dialogo");
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()){

            GoogleSignInAccount account = result.getSignInAccount();
            Usuario usuario = usuarioControl.iniciarSesionBC(account.getEmail());

            if (usuario != null){
                iniciarSesion(usuario);
            } else {
                String foto = null;
                if (account.getPhotoUrl() != null){
                    foto = account.getPhotoUrl().toString();
                }
                registrarUsuarioG(account.getEmail(), account.getDisplayName(), account.getId(), foto);
            }

        } else {
            Toast.makeText(this, getText(R.string.error_conexion), Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarUsuarioG(String email, String displayName, String id, String  foto) {
        Bundle bundle = new Bundle();
        bundle.putString("username", email);
        bundle.putString("contraseña", id);
        bundle.putString("nombre", displayName);
        bundle.putString("foto", foto);

        FacultadGFragment fragment = new FacultadGFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment.setArguments(bundle);
        try {
            fragment.show(fragmentManager, "dialog");
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), getText(R.string.revisar_conexion), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            case R.id.username:
                if (view.hasFocus()){
                    usernameEditText.setBackgroundResource(R.drawable.edit_text_focus);
                } else {
                    usernameEditText.setBackgroundResource(R.drawable.edit_text);
                }
                break;
            case R.id.password:
                if (view.hasFocus()){
                    passwordEditText.setBackgroundResource(R.drawable.edit_text_focus);
                } else {
                    passwordEditText.setBackgroundResource(R.drawable.edit_text);
                }
                break;
        }
    }

    public void iniciarSesion(Usuario usuario){
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", usuario.getUsuario());
        editor.putString("nombre", usuario.getNombre());
        editor.putString("apellido", usuario.getApellido());
        editor.putBoolean("tipoUsuario", usuario.getTipo());
        editor.putInt("facultad", usuario.getIdFacultad());
        editor.commit();

        Intent intent = new Intent(context, MenuAdminActivity.class);

        Toast.makeText(this, getText(R.string.bienvenido2) + usuario.getNombre() + "!", Toast.LENGTH_SHORT).show();

        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewBarcode:
                new IntentIntegrator(LoginActivity.this).initiateScan();
                break;
            case R.id.login:
                Boolean exito = loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), context);

                if (exito) {
                    Usuario usuario = usuarioControl.iniciarSesion(
                            usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                    iniciarSesion(usuario);
                }
                break;
            case R.id.buttonInvitado:
                launchDialog();
                break;
            case R.id.imageViewG:
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, 777);
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, getText(R.string.error_conexion), Toast.LENGTH_SHORT).show();
    }
}
