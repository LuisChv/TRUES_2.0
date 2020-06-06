package sv.ues.fia.eisi.trues.ui.alumno.mispaso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.control.UsuarioPasoControl;
import sv.ues.fia.eisi.trues.db.entity.PasoEstado;
import sv.ues.fia.eisi.trues.ui.alumno.mispaso.PasoEstadoFragment.OnListFragmentInteractionListener;
import sv.ues.fia.eisi.trues.ui.global.paso.detalle.DetallePasoFragment;

import java.util.List;

public class MyPasoEstadoRecyclerViewAdapter extends RecyclerView.Adapter<MyPasoEstadoRecyclerViewAdapter.ViewHolder> {

    private final List<PasoEstado> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MyPasoEstadoRecyclerViewAdapter(List<PasoEstado> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_paso_estado, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.textViewIdPaso.setText(String.valueOf(position+1));
        holder.textViewDescripcion.setText(holder.mItem.getDescripcionPaso());

        if (holder.mItem.isEstado()){
            holder.imageViewEstado.setImageResource(R.drawable.ic_check);
        }
        else {
            holder.imageViewEstado.setImageResource(R.drawable.ic_clock);
        }

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
        public final TextView textViewIdPaso;
        public final TextView textViewDescripcion;
        public final ImageView imageViewEstado;
        public final RelativeLayout layoutABorrar;
        public final RelativeLayout layoutRojo;
        public final RelativeLayout layoutAzul;
        public PasoEstado mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewIdPaso = view.findViewById(R.id.textViewIdPaso);
            textViewDescripcion = view.findViewById(R.id.textViewPasoDescripcion);
            imageViewEstado = view.findViewById(R.id.imageViewEstado);
            layoutABorrar = view.findViewById(R.id.layoutABorrar);
            layoutRojo = view.findViewById(R.id.layoutRojo);
            layoutAzul = view.findViewById(R.id.layoutAzul);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewIdPaso.getText() + "'";
        }
    }

    public void setEstado(int position){
        UsuarioPasoControl usuarioPasoControl = new UsuarioPasoControl(context);
        usuarioPasoControl.setEstado(mValues.get(position).getIdUsuarioPaso(), !mValues.get(position).isEstado());
        mValues.get(position).setEstado(!mValues.get(position).isEstado());
        notifyDataSetChanged();
    }
}
