package sv.ues.fia.eisi.trues.ui.global.ubicacion;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import sv.ues.fia.eisi.trues.BuildConfig;
import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.UbicacionControl;
import sv.ues.fia.eisi.trues.db.entity.Ubicacion;

public class UbicacionFragment extends Fragment {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapView;
    private MapController mapController;
    private View view;
    private Integer idPersonal;
    private Ubicacion ubicacion;
    private UbicacionControl control;
    private List<Ubicacion> ubicacionList;

    public static UbicacionFragment newInstance() {
        return new UbicacionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Context context = getActivity().getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        view = inflater.inflate(R.layout.fragment_ubicacion, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getText(R.string.ubicacion));

        control = new UbicacionControl(context);
        idPersonal = getArguments().getInt("idPersonal", -1);
        ubicacionList = control.ObtenerUbicaciones(idPersonal);

        mapView = view.findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setUseDataConnection(true);

        GeoPoint centro =
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
                return false;
            }
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }
        };

        ItemizedOverlayWithFocus<OverlayItem> capa = new ItemizedOverlayWithFocus<OverlayItem>(getActivity(), puntos, tap);
        capa.setFontSize(20);
        capa.setDescriptionBoxPadding(30);
        capa.setDescriptionBoxCornerWidth(20);
        capa.setDescriptionTitleExtraLineHeight(40);
        capa.setDescriptionLineHeight(40);
        capa.setMarkerBackgroundColor(Color.WHITE);
        capa.setMarkerTitleForegroundColor(Color.rgb(127, 0, 0));
        capa.setFocusItemsOnTap(true);
        mapView.getOverlays().add(capa);

        return view;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}