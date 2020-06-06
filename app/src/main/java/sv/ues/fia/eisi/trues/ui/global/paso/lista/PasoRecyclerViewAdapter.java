package sv.ues.fia.eisi.trues.ui.global.paso.lista;

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

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.entity.Paso;
import sv.ues.fia.eisi.trues.ui.global.paso.detalle.DetallePasoFragment;
import sv.ues.fia.eisi.trues.ui.global.paso.lista.PasoFragment.OnListFragmentInteractionListener;

import java.util.List;



public class PasoRecyclerViewAdapter extends RecyclerView.Adapter<PasoRecyclerViewAdapter.ViewHolder> {

    private final List<Paso> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public PasoRecyclerViewAdapter(List<Paso> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_paso, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.idPaso.setText(String.valueOf(position+1));
        holder.descripcionPaso.setText(mValues.get(position).getDescripcion());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("idPaso", holder.mItem.getIdPaso());
                DetallePasoFragment detallePasoFragment = new DetallePasoFragment();
                detallePasoFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                detallePasoFragment.show(fragmentManager, "dialogo");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView idPaso;
        public final TextView descripcionPaso;
        public final RelativeLayout layoutABorrar;
        public final RelativeLayout layoutRojo;
        public final RelativeLayout layoutAzul;
        public Paso mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            idPaso = (TextView) view.findViewById(R.id.idPaso);
            descripcionPaso = (TextView) view.findViewById(R.id.descripcionPaso);
            layoutABorrar = view.findViewById(R.id.layoutABorrar);
            layoutRojo = view.findViewById(R.id.layoutRojo);
            layoutAzul = view.findViewById(R.id.layoutAzul);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + idPaso.getText() + "'";
        }
    }
}
