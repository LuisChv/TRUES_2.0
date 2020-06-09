package sv.ues.fia.eisi.trues.ui.global.presentacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.ui.global.presentacion.fragments.Cinco;
import sv.ues.fia.eisi.trues.ui.global.presentacion.fragments.Cuatro;
import sv.ues.fia.eisi.trues.ui.global.presentacion.fragments.Dos;
import sv.ues.fia.eisi.trues.ui.global.presentacion.fragments.Ocho;
import sv.ues.fia.eisi.trues.ui.global.presentacion.fragments.Seis;
import sv.ues.fia.eisi.trues.ui.global.presentacion.fragments.Siete;
import sv.ues.fia.eisi.trues.ui.global.presentacion.fragments.Tres;
import sv.ues.fia.eisi.trues.ui.global.presentacion.fragments.Uno;

public class PresentacionActivity extends AppCompatActivity {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);
        getSupportActionBar().hide();

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new Uno());
        fragmentList.add(new Dos());
        fragmentList.add(new Tres());
        fragmentList.add(new Cuatro());
        fragmentList.add(new Cinco());
        fragmentList.add(new Seis());
        fragmentList.add(new Siete());
        fragmentList.add(new Ocho());

        pager = findViewById(R.id.pager);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), fragmentList);
        pager.setAdapter(pagerAdapter);
    }
}