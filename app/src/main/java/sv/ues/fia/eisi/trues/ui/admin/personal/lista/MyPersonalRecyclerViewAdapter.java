package sv.ues.fia.eisi.trues.ui.admin.personal.lista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.entity.Personal;
import sv.ues.fia.eisi.trues.ui.global.personal.DetallePersonalFragment;
import sv.ues.fia.eisi.trues.ui.global.tramite.lista.TramitesFragment;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPersonalRecyclerViewAdapter extends RecyclerView.Adapter<MyPersonalRecyclerViewAdapter.ViewHolder> {
    private Context context;

    private final List<Personal> mValues;

    public MyPersonalRecyclerViewAdapter(List<Personal> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_personal, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.textViewID.setText(String.valueOf(position+1));
        holder.textViewNombre.setText(mValues.get(position).getNombrePersonal());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("idPersonal", mValues.get(position).getIdPersonal());

                DetallePersonalFragment fragment = new DetallePersonalFragment();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                fragment.setArguments(bundle);
                fragment.show(fragmentManager, "dialog");
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
        public final RelativeLayout layoutABorrar;
        public final RelativeLayout layoutRojo;
        public final RelativeLayout layoutAzul;
        public Personal mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewID = view.findViewById(R.id.textViewID);
            textViewNombre = view.findViewById(R.id.textViewNombre);
            layoutABorrar = view.findViewById(R.id.layoutABorrar);
            layoutRojo = view.findViewById(R.id.layoutRojo);
            layoutAzul = view.findViewById(R.id.layoutAzul);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewID.getText() + "'";
        }
    }
}