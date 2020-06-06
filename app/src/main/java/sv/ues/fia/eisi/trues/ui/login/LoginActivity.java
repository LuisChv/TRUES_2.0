package  sv.ues.fia.eisi.trues.ui.login;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.LlenarBD;
import sv.ues.fia.eisi.trues.db.control.UsuarioControl;
import sv.ues.fia.eisi.trues.db.entity.Usuario;
import sv.ues.fia.eisi.trues.ui.global.MenuAdminActivity;
import sv.ues.fia.eisi.trues.ui.login.invitado.FacultadPreferidaFragment;

public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private LoginViewModel loginViewModel;
    private Context context;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        context = this;

        //VERIFICAR SI HAY SESIÓN ACTIVA
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        if (sharedPreferences.getString("usuario", null) != null){
            Intent intent = new Intent(context, MenuAdminActivity.class);
            startActivity(intent);
            finish();
        }

        usernameEditText = findViewById(R.id.username);
        usernameEditText.setOnFocusChangeListener(this);
        passwordEditText = findViewById(R.id.password);
        passwordEditText.setOnFocusChangeListener(this);

        final Button loginButton = findViewById(R.id.login);
        final Button invitadoButton = findViewById(R.id.buttonInvitado);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);




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
                    loadingProgressBar.setVisibility(View.VISIBLE);

                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), context);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean exito = loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), context);

                if (exito) {
                    UsuarioControl usuarioControl = new UsuarioControl(context);
                    Usuario usuario = usuarioControl.iniciarSesion(
                            usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());

                    SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("usuario", usuario.getUsuario());
                    editor.putString("nombre", usuario.getNombre());
                    editor.putString("apellido", usuario.getApellido());
                    editor.putBoolean("tipoUsuario", usuario.getTipo());
                    editor.putInt("facultad", usuario.getIdFacultad());
                    editor.commit();

                    Intent intent = new Intent(context, MenuAdminActivity.class);

                    startActivity(intent);
                    loadingProgressBar.setVisibility(View.GONE);
                    finish();
                }
            }
        });
        invitadoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDialog();
            }
        });

    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void launchDialog(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FacultadPreferidaFragment facultadPreferidaFragment = new FacultadPreferidaFragment();
        facultadPreferidaFragment.show(fragmentManager, "Dialogo");
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
}
