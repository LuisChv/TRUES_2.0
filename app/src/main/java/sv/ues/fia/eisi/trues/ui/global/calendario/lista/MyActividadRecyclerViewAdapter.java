package sv.ues.fia.eisi.trues.ui.global.calendario.lista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.ActividadControl;
import sv.ues.fia.eisi.trues.db.control.ActividadTramiteControl;
import sv.ues.fia.eisi.trues.db.entity.Actividad;
import sv.ues.fia.eisi.trues.db.entity.ActividadTramite;
import sv.ues.fia.eisi.trues.ui.global.calendario.lista.ActividadesFragment.OnListFragmentInteractionListener;
import sv.ues.fia.eisi.trues.ui.global.tramite.lista.TramitesFragment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
/**
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyActividadRecyclerViewAdapter extends RecyclerView.Adapter<MyActividadRecyclerViewAdapter.ViewHolder> {

    private final List<Actividad> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MyActividadRecyclerViewAdapter(List<Actividad> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_actividad, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String inicio = mValues.get(position).getInicio();
        String fin = mValues.get(position).getFin();

        holder.mItem = mValues.get(position);
        holder.textViewID.setText(String.valueOf(position + 1));
        holder.textViewNombre.setText(mValues.get(position).getNombreActividad());
        holder.textViewInicio.setText(inicio);
        holder.textViewFin.setText(fin);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("actividad", mValues.get(position).getIdActividad());

                TramitesFragment tramitesFragment = new TramitesFragment();
                tramitesFragment.setArguments(bundle);

                ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, tramitesFragment).addToBackStack(null).commit();

                Toast.makeText(context.getApplicationContext(), "Aquí tienes los trámites que puedes realizar durante este periodo", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewID;
        public final TextView textViewNombre;
        public final TextView textViewInicio;
        public final TextView textViewFin;
        public final RelativeLayout layoutABorrar;
        public final RelativeLayout layoutRojo;
        public final RelativeLayout layoutAzul;
        public Actividad mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewID = (TextView) view.findViewById(R.id.textViewID);
            textViewNombre = (TextView) view.findViewById(R.id.textViewNombre);
            textViewInicio = view.findViewById(R.id.textViewInicio);
            textViewFin = view.findViewById(R.id.textViewFin);
            layoutABorrar = view.findViewById(R.id.layoutABorrar);
            layoutRojo = view.findViewById(R.id.layoutRojo);
            layoutAzul = view.findViewById(R.id.layoutAzul);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewID.getText() + "'";
        }
    }

    public void removeItem(int position){
        ActividadControl actividadControl = new ActividadControl(context);
        actividadControl.EliminarActividad(mValues.get(position).getIdActividad());
        if (verificarInternet()) {
            actividadControl.EliminarActividadWS(mValues.get(position).getIdActividad());
        } else {
            String idActividad = String.valueOf(mValues.get(position).getIdActividad());
            String parametros = "delete;" + idActividad;
            writeToFile(parametros, context, "Actividad");
        }

        mValues.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Actividad actividad, int position, List<ActividadTramite> actividadTramites){
        mValues.add(position, actividad);
        notifyItemInserted(position);

        ActividadControl actividadControl = new ActividadControl(context);
        actividadControl.CrearActividadID(
                actividad.getIdActividad(), actividad.getIdFacultad(),
                actividad.getNombreActividad(), actividad.getInicio(), actividad.getFin());

        ActividadTramiteControl actividadTramiteControl = new ActividadTramiteControl(context);
        if (verificarInternet()) {
            actividadControl.CrearActividadWS(actividad.getIdActividad(), actividad.getIdFacultad(),
                    actividad.getNombreActividad(), actividad.getInicio(), actividad.getFin());
        } else {
            String parametros = "insert;" + actividad.getIdActividad().toString()
                    + ";" + actividad.getIdFacultad().toString()
                    + ";" + actividad.getNombreActividad() + ";" + actividad.getInicio()
                    + ";" + actividad.getFin();
            writeToFile(parametros, context, "Actividad");
        }
        for (int i = 0; i<actividadTramites.size(); i++){
            actividadTramiteControl.CrearActividadTramite(
                    actividadTramites.get(i).getIdActividad(), actividadTramites.get(i).getIdTramite());
        }

    }

    public void actualizar(){
        notifyDataSetChanged();
    }
    private boolean verificarInternet() {
        Boolean HayInternet;
        if (isNetDisponible() && isOnlineNet()) {
            //Toast.makeText(context, "Hay internet y está conectado", Toast.LENGTH_SHORT).show();
            HayInternet = true;
        } else {
            //Toast.makeText(context, "No hay internet y está conectado", Toast.LENGTH_SHORT).show();
            HayInternet = false;
        }
        return HayInternet;
    }

    private boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
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

    private void writeToFile(String data, Context context, String archivo) {
        //Toast.makeText(context,readFromFile(context),Toast.LENGTH_LONG).show();
        String prueba = readFromFile(context, archivo) + data;

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(archivo + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.append(prueba);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        String after = readFromFile(context, archivo);
        Toast.makeText(context, after, Toast.LENGTH_SHORT).show();
    }

    private String readFromFile(Context context, String archivo) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(archivo + ".txt");
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
            Log.e("AgregarCargoFragment", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("AgregarCargoFragment", "Can not read file: " + e.toString());
        }
        //Toast.makeText(context,ret,Toast.LENGTH_LONG).show();
        return ret;
    }
}
