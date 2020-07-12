package com.example.bw4ever.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bw4ever.R;
import com.example.bw4ever.modelo.Parque;

import java.util.List;

public class ParqueAdapter extends RecyclerView.Adapter<ParqueAdapter.ParqueHolder> {
    List<Parque> listaparques;
    int item_layout;
    Activity activity;

    public ParqueAdapter(List<Parque> listaparque, int item_layout, Activity activity) {
        this.listaparques = listaparque;
        this.item_layout = item_layout;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ParqueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(item_layout, parent,false);
        return new ParqueHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParqueHolder holder, int position) {
        Parque parque = listaparques.get(position);
        holder.txtnombre.setText(parque.getNombre());
        holder.txtlatitud.setText(String.valueOf(parque.getLatitud()));
        holder.txtlongitud.setText(String.valueOf(parque.getLongitud()));
    }

    @Override
    public int getItemCount() {
        return listaparques.size();
    }

    public class ParqueHolder extends RecyclerView.ViewHolder{
        TextView txtnombre, txtlatitud, txtlongitud;
        public ParqueHolder(@NonNull View itemView) {
            super(itemView);
            txtnombre = itemView.findViewById(R.id.item_nombreparque);
            txtlatitud = itemView.findViewById(R.id.item_latitud);
            txtlongitud = itemView.findViewById(R.id.item_longitud);
        }
    }
}

