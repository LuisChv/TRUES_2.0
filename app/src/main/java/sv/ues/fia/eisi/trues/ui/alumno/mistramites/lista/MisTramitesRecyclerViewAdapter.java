package sv.ues.fia.eisi.trues.ui.alumno.mistramites.lista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.TramiteControl;
import sv.ues.fia.eisi.trues.db.control.UsuarioPasoControl;
import sv.ues.fia.eisi.trues.db.entity.PasoEstado;
import sv.ues.fia.eisi.trues.db.entity.Tramite;
import sv.ues.fia.eisi.trues.db.entity.UsuarioTramite;
import sv.ues.fia.eisi.trues.ui.alumno.mispaso.PasoEstadoFragment;
import sv.ues.fia.eisi.trues.ui.alumno.mistramites.eliminar.EliminarMiTramiteFragment;
import sv.ues.fia.eisi.trues.ui.alumno.mistramites.lista.MisTramitesFragment.OnListFragmentInteractionListener;

import java.util.List;


public class MisTramitesRecyclerViewAdapter extends RecyclerView.Adapter<MisTramitesRecyclerViewAdapter.ViewHolder> {

    private final List<UsuarioTramite> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MisTramitesRecyclerViewAdapter(List<UsuarioTramite> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_mi_tramite, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        String usuario =  sharedPreferences.getString("usuario", null);

        TramiteControl tramiteControl = new TramiteControl(context);
        Tramite tramite = tramiteControl.consultarTramite(mValues.get(position).getIdTramite());

        UsuarioPasoControl usuarioPasoControl = new UsuarioPasoControl(context);
        List<PasoEstado> pasoEstadoList = usuarioPasoControl.obtenerMisPasos(usuario, tramite.getIdTramite());
        Float progreso = 0f;
        Float total = 0f;

        for (int i = 0; i < pasoEstadoList.size(); i++){
            total = total + pasoEstadoList.get(i).getPorcentaje();
            if (pasoEstadoList.get(i).isEstado()){
                progreso = progreso + pasoEstadoList.get(i).getPorcentaje();
            }
        }

        Integer porcentaje = Math.round((progreso/total)*100);

        holder.mItem = mValues.get(position);
        holder.idUsuarioT.setText(String.valueOf(position + 1));
        holder.nombreTramite.setText(tramite.getNombreTramite());
        holder.textViewProgreso.setText(String.valueOf(porcentaje));
        holder.progreso.setProgress(porcentaje);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("idTramite", holder.mItem.getIdTramite());
                PasoEstadoFragment pasoEstadoFragment = new PasoEstadoFragment();
                pasoEstadoFragment.setArguments(bundle);

                ((AppCompatActivity)context).getSupportFragmentManager()
                        .beginTransaction().add(R.id.mainContainer, pasoEstadoFragment)
                        .addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView idUsuarioT;
        public final TextView nombreTramite;
        public final TextView textViewProgreso;
        public final ProgressBar progreso;
        public final CardView cardView;
        public final RelativeLayout layoutABorrar;
        public final RelativeLayout layoutRojo;
        public final RelativeLayout layoutAzul;

        public UsuarioTramite mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            idUsuarioT = (TextView) view.findViewById(R.id.textViewIDUT);
            progreso = view.findViewById(R.id.progressBarUT);
            nombreTramite = (TextView) view.findViewById(R.id.textViewNombreUT);
            textViewProgreso = view.findViewById(R.id.textViewProgreso);
            cardView = view.findViewById(R.id.cardViewMiTramite);
            layoutABorrar = view.findViewById(R.id.layoutABorrar);
            layoutRojo = view.findViewById(R.id.layoutRojo);
            layoutAzul = view.findViewById(R.id.layoutAzul);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + idUsuarioT.getText() + "'";
        }
    }

    public void removeItem(int position){
        Bundle bundle = new Bundle();
        bundle.putInt("idUsuarioTramite", mValues.get(position).getIdUsuarioT());
        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        EliminarMiTramiteFragment fragment = new EliminarMiTramiteFragment();
        fragment.setArguments(bundle);
        fragment.show(fragmentManager, "dialogo");

        notifyDataSetChanged();
    }

}
