package sv.ues.fia.eisi.trues.ui.global.documento.lista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.entity.Documento;
import sv.ues.fia.eisi.trues.ui.global.documento.PdfFragment;
import sv.ues.fia.eisi.trues.ui.global.documento.lista.DocumentoFragment.OnListFragmentInteractionListener;


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

        holder.imageViewDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(holder.mItem.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
        holder.imageViewVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfFragment pdfFragment = new PdfFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", holder.mItem.getUrl());
                pdfFragment.setArguments(bundle);
                ((AppCompatActivity)context)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainContainer, pdfFragment)
                        .addToBackStack(null)
                        .commit();
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
        public final ImageView imageViewVer;
        public final ImageView imageViewDescargar;
        public Documento mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewId = view.findViewById(R.id.textViewIdDocumento);
            textViewUrl = view.findViewById(R.id.textViewUrl);
            layoutABorrar = view.findViewById(R.id.layoutABorrar);
            layoutRojo = view.findViewById(R.id.layoutRojo);
            layoutAzul = view.findViewById(R.id.layoutAzul);
            imageViewVer = view.findViewById(R.id.imageView6);
            imageViewDescargar = view.findViewById(R.id.imageView4);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewId .getText() + "'";
        }
    }
}
