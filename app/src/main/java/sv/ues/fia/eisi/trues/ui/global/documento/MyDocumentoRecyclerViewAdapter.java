package sv.ues.fia.eisi.trues.ui.global.documento;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.entity.Documento;
import sv.ues.fia.eisi.trues.ui.global.documento.DocumentoFragment.OnListFragmentInteractionListener;


import java.util.List;

public class MyDocumentoRecyclerViewAdapter extends RecyclerView.Adapter<MyDocumentoRecyclerViewAdapter.ViewHolder> {

    private final List<Documento> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MyDocumentoRecyclerViewAdapter(List<Documento> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_documento, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.textViewId.setText(String.valueOf(position+1));
        holder.textViewUrl.setText(holder.mItem.getNombreDocumento());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(holder.mItem.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewId;
        public final TextView textViewUrl;
        public final RelativeLayout layoutABorrar;
        public final RelativeLayout layoutRojo;
        public final RelativeLayout layoutAzul;
        public Documento mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewId = view.findViewById(R.id.textViewIdDocumento);
            textViewUrl = view.findViewById(R.id.textViewUrl);
            layoutABorrar = view.findViewById(R.id.layoutABorrar);
            layoutRojo = view.findViewById(R.id.layoutRojo);
            layoutAzul = view.findViewById(R.id.layoutAzul);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewId .getText() + "'";
        }
    }
}
