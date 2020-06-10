package sv.ues.fia.eisi.trues.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.widget.Toast;

import sv.ues.fia.eisi.trues.ui.login.data.LoginRepository;
import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.UsuarioControl;
import sv.ues.fia.eisi.trues.db.entity.Usuario;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public Boolean login(String username, String password, Context context) {
        // can be launched in a separate asynchronous job
        UsuarioControl usuarioControl = new UsuarioControl(context);
        Usuario usuario = usuarioControl.iniciarSesion(username, password);
        String mensaje;
        Boolean exito = false;

        if (usuario != null){
            exito = true;
        }
        else {
            mensaje = context.getText(R.string.usuario_incorrecto).toString();
            Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
        }

        return exito;
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }else {
            return username.trim().length() > 4;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
