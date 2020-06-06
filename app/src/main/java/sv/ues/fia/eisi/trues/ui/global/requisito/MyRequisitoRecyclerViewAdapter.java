package sv.ues.fia.eisi.trues.ui.global.requisito;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.entity.Requisito;
import sv.ues.fia.eisi.trues.ui.global.requisito.RequisitosFragment.OnListFragmentInteractionListener;

import java.util.List;


public class MyRequisitoRecyclerViewAdapter extends RecyclerView.Adapter<MyRequisitoRecyclerViewAdapter.ViewHolder> {

    private final List<Requisito> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyRequisitoRecyclerViewAdapter(List<Requisito> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_requisitos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.textViewIdRequisito.setText(String.valueOf(position+1));
        holder.textViewDescripcion.setText(holder.mItem.getDescripcion());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewIdRequisito;
        public final TextView textViewDescripcion;
        public final RelativeLayout layoutABorrar;
        public final RelativeLayout layoutRojo;
        public final RelativeLayout layoutAzul;
        public Requisito mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewIdRequisito = view.findViewById(R.id.textViewIdRequisito);
            textViewDescripcion = view.findViewById(R.id.textViewDescripcion);
            layoutABorrar = view.findViewById(R.id.layoutABorrar);
            layoutRojo = view.findViewById(R.id.layoutRojo);
            layoutAzul = view.findViewById(R.id.layoutAzul);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewIdRequisito.getText() + "'";
        }
    }
}
