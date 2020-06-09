package sv.ues.fia.eisi.trues.ui.global;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.FacebookSdk;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.LlenarBD;
import sv.ues.fia.eisi.trues.ui.global.presentacion.PresentacionActivity;
import sv.ues.fia.eisi.trues.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        context = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences instalacion = getSharedPreferences("Arranque", MODE_PRIVATE);
                Intent intent;
                if (!instalacion.getBoolean("instalada", false)){
                    LlenarBD llenarBD = new LlenarBD();
                    llenarBD.llenarBD(context);
                    SharedPreferences.Editor editor = instalacion.edit();
                    editor.putBoolean("instalada", true);
                    editor.commit();
                    intent = new Intent(SplashActivity.this, PresentacionActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
