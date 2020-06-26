package com.example.bw4ever.vistas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bw4ever.R;
import com.example.bw4ever.adapter.EjercicioAdapter;
import com.example.bw4ever.modelo.Ejercicio;
import com.example.bw4ever.modelo.EjercicioService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RutinasFragment extends Fragment {

    RecyclerView rc;
    EditText txtnombre, txtdescripcion;
    Button btn;

    public RutinasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rutinas, container, false);
        rc = view.findViewById(R.id.rc);
        txtnombre = view.findViewById(R.id.txtnombre);
        txtdescripcion = view.findViewById(R.id.txtdescripcion);
        btn = view.findViewById(R.id.bt_agregar);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Ejercicio ejercicio = new Ejercicio();
                ejercicio.setNombre(txtnombre.getText().toString());
                ejercicio.setDescripcion(txtdescripcion.getText().toString());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Ejercicios");
                reference.push().setValue(ejercicio);
            }
        });

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(RecyclerView.VERTICAL);
        rc.setLayoutManager(lm);

        EjercicioAdapter adapter = new EjercicioAdapter(EjercicioService.ejercicioList,
                R.layout.item,getActivity());
        rc.setAdapter(adapter);
        cargaEjerciciosFirebase();
        return view;
    }

    public void cargaEjerciciosFirebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Ejercicios");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Ejercicio ejercicio = dataSnapshot.getValue(Ejercicio.class);
                ejercicio.setId(dataSnapshot.getKey());

                if (!EjercicioService.ejercicioList.contains(ejercicio)) {
                    EjercicioService.addEjercicio(ejercicio);
                }

                rc.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Ejercicio ejercicio = dataSnapshot.getValue(Ejercicio.class);
                ejercicio.setId(dataSnapshot.getKey());

                if (EjercicioService.ejercicioList.contains(ejercicio)) {
                    EjercicioService.updateEjercicio(ejercicio);
                }

                rc.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Ejercicio ejercicio = dataSnapshot.getValue(Ejercicio.class);
                ejercicio.setId(dataSnapshot.getKey());

                if (EjercicioService.ejercicioList.contains(ejercicio)) {
                    EjercicioService.removeEjercicio(ejercicio);
                }

                rc.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
