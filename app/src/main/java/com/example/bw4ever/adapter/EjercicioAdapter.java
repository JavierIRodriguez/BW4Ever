package com.example.bw4ever.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bw4ever.R;
import com.example.bw4ever.modelo.Ejercicio;

import java.util.List;

public class EjercicioAdapter extends RecyclerView.Adapter<EjercicioAdapter.EjercicioHolder> {
    List<Ejercicio> lista;
    int item_layout;
    Activity activity;

    public EjercicioAdapter(List<Ejercicio> lista, int item_layout, Activity activity) {
        this.lista = lista;
        this.item_layout = item_layout;
        this.activity = activity;
    }

    @NonNull
    @Override
    public EjercicioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(item_layout, parent, false);
        return new EjercicioHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EjercicioHolder holder, int position) {
        Ejercicio ejercicio = lista.get(position);
        holder.txtnombre.setText(ejercicio.getNombre());
        holder.txtdescripcion.setText(ejercicio.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class EjercicioHolder extends RecyclerView.ViewHolder{
        TextView txtnombre, txtdescripcion;
        public EjercicioHolder(@NonNull View itemView) {
            super(itemView);
            //inicializar
            txtnombre = itemView.findViewById(R.id.item_nombre);
            txtdescripcion = itemView.findViewById(R.id.item_descripcion);
        }
    }
}
