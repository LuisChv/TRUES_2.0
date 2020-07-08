package sv.ues.fia.eisi.trues.ui.global.paso.detalle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.PasoControl;
import sv.ues.fia.eisi.trues.db.control.PersonalControl;
import sv.ues.fia.eisi.trues.db.control.UbicacionControl;
import sv.ues.fia.eisi.trues.db.entity.Paso;
import sv.ues.fia.eisi.trues.db.entity.Personal;
import sv.ues.fia.eisi.trues.db.entity.Ubicacion;
import sv.ues.fia.eisi.trues.ui.global.personal.DetallePersonalFragment;
import sv.ues.fia.eisi.trues.ui.global.ubicacion.UbicacionFragment;

public class DetallePasoFragment extends DialogFragment implements View.OnClickListener {

    private View view;
    private TextView textViewDescripcion, textViewResponsable;
    private Integer idPaso;
    private Paso paso;
    private Personal personal;
    private CardView cardViewResponsable;
    private CardView cardViewUbicacion;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapView;
    private MapController mapController;
    private Integer idPersonal;
    private Ubicacion ubicacion;
    private UbicacionControl control;
    private List<Ubicacion> ubicacionList;
    private ImageView expand, pin;
    private MyLocationNewOverlay myLocation;
    private GpsMyLocationProvider provider;
    private Boolean mi = false;
    private GeoPoint centro;

    public static DetallePasoFragment newInstance() {
        return new DetallePasoFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        Context context = getActivity().getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        idPaso = getArguments().getInt("idPaso", -1);

        PasoControl pasoControl = new PasoControl(getActivity());
        paso = pasoControl.obtenerPaso(idPaso);

        PersonalControl personalControl = new PersonalControl(getActivity());
        personal = personalControl.consultarPersonal(paso.getIdPersonal());

        idPersonal = personal.getIdPersonal();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_detalle_paso, null);
        textViewDescripcion = view.findViewById(R.id.textViewDescripcion);
        textViewDescripcion.setText(paso.getDescripcion());
        textViewResponsable = view.findViewById(R.id.textViewResponsable);
        textViewResponsable.setText(personal.getNombrePersonal());
        cardViewResponsable = view.findViewById(R.id.cardView3);
        cardViewResponsable.setOnClickListener(this);
        cardViewUbicacion = view.findViewById(R.id.cardView4);

        control = new UbicacionControl(context);
        ubicacionList = control.ObtenerUbicaciones(idPersonal);

        mapView = view.findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setUseDataConnection(true);
        mapView.setOnClickListener(this);

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) { }
            else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        provider = new GpsMyLocationProvider(getActivity().getApplicationContext());
        myLocation = new MyLocationNewOverlay(provider, mapView);
        myLocation.setOptionsMenuEnabled(true);
        mapView.getOverlays().add(myLocation);
        myLocation.enableMyLocation();

        centro =
                new GeoPoint(ubicacionList.get(0).getLatidud(), ubicacionList.get(0).getLongitud(),
                        ubicacionList.get(0).getAltitud());

        mapController = (MapController) mapView.getController();
        mapController.setCenter(centro);
        mapController.setZoom(19);

        requestPermissionsIfNecessary(new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        mapView.setMultiTouchControls(true);

        ArrayList<OverlayItem> puntos = new ArrayList<OverlayItem>();

        for (int i = 0; i<ubicacionList.size(); i++){
            Ubicacion ubicacion = ubicacionList.get(i);
            GeoPoint punto = new GeoPoint(ubicacion.getLatidud(), ubicacion.getLongitud(), ubicacion.getAltitud());
            puntos.add(
                    new OverlayItem("Universidad de El Salvador",
                            ubicacion.getComponenteTematica(),
                            punto));
        }

        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> tap = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemLongPress(int arg0, OverlayItem arg1) {
                return true;
            }
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }
        };

        ItemizedOverlayWithFocus<OverlayItem> capa = new ItemizedOverlayWithFocus<OverlayItem>(getActivity(), puntos, tap);
        capa.setFontSize(15);
        capa.setDescriptionBoxPadding(30);
        capa.setDescriptionBoxCornerWidth(20);
        capa.setDescriptionTitleExtraLineHeight(40);
        capa.setDescriptionLineHeight(40);
        capa.setMarkerBackgroundColor(Color.WHITE);
        capa.setMarkerTitleForegroundColor(Color.rgb(127, 0, 0));
        capa.setFocusItemsOnTap(true);

        mapView.getOverlays().add(capa);
        expand = view.findViewById(R.id.imageView22);
        expand.setOnClickListener(this);
        pin = view.findViewById(R.id.imageView23);
        pin.setOnClickListener(this);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("idPersonal", personal.getIdPersonal());

        switch (v.getId()){
            case R.id.cardView3:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DetallePersonalFragment dialogFragment = new DetallePersonalFragment();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fragmentManager, "dialog");
                break;
            case R.id.imageView22:
                UbicacionFragment fragment = new UbicacionFragment();
                fragment.setArguments(bundle);
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.mainContainer, fragment)
                        .addToBackStack(null).commit();
                dismiss();
                break;
            case R.id.imageView23:
                if (mi){
                    myLocation.disableFollowLocation();
                    mapController.setCenter(centro);
                }
                else {
                    myLocation.enableFollowLocation();
                }
                mi = !mi;
                break;
        }


    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

}
