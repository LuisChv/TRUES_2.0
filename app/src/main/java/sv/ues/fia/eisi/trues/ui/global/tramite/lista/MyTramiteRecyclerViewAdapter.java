package sv.ues.fia.eisi.trues.ui.global.tramite.lista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.ActividadControl;
import sv.ues.fia.eisi.trues.db.control.ActividadTramiteControl;
import sv.ues.fia.eisi.trues.db.entity.Actividad;
import sv.ues.fia.eisi.trues.db.entity.ActividadTramite;
import sv.ues.fia.eisi.trues.db.entity.Tramite;
import sv.ues.fia.eisi.trues.ui.global.tramite.lista.TramitesFragment.OnListFragmentInteractionListener;
import sv.ues.fia.eisi.trues.ui.global.tramite.menu.MenuTramiteFragment;

import java.util.List;

public class MyTramiteRecyclerViewAdapter extends RecyclerView.Adapter<MyTramiteRecyclerViewAdapter.ViewHolder> {

    private final List<Tramite> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MyTramiteRecyclerViewAdapter(List<Tramite> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tramite, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.textViewIDTramite.setText(String.valueOf(position + 1));
        holder.textViewNombreTramite.setText(mValues.get(position).getNombreTramite());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("idTramite", holder.mItem.getIdTramite());
                MenuTramiteFragment menuTramiteFragment = new MenuTramiteFragment();
                menuTramiteFragment.setArguments(bundle);

                ((AppCompatActivity)context).getSupportFragmentManager().
                        beginTransaction().add(R.id.mainContainer, menuTramiteFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void actualizar() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView textViewIDTramite;
        private final TextView textViewNombreTramite;
        public final RelativeLayout layoutABorrar;
        public final RelativeLayout layoutRojo;
        public final RelativeLayout layoutAzul;
        private Tramite mItem;

        private ViewHolder(View view) {
            super(view);
            mView = view;
            textViewIDTramite =  view.findViewById(R.id.textViewTramiteID);
            textViewNombreTramite = view.findViewById(R.id.textViewNombreTramite);
            layoutABorrar = view.findViewById(R.id.layoutABorrar);
            layoutRojo = view.findViewById(R.id.layoutRojo);
            layoutAzul = view.findViewById(R.id.layoutAzul);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewIDTramite.getText() + "'";
        }
    }

    public void removeItem(int position){

    }

    public void restoreItem(Actividad actividad, int position, List<ActividadTramite> actividadTramites){

    }
}
