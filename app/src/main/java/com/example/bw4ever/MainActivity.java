package com.example.bw4ever;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bw4ever.adapter.EjercicioAdapter;
import com.example.bw4ever.modelo.Ejercicio;
import com.example.bw4ever.modelo.EjercicioService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText txtnombre, txtdescripcion;
    RecyclerView rc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtnombre = findViewById(R.id.txtnombre);
        txtdescripcion = findViewById(R.id.txtdescripcion);
        rc = findViewById(R.id.rc);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(RecyclerView.VERTICAL);
        rc.setLayoutManager(lm);

        EjercicioAdapter adapter = new EjercicioAdapter(EjercicioService.ejercicioList,
                R.layout.item,this);
        rc.setAdapter(adapter);
        cargaEjerciciosFirebase();
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

     public void agregarEjercicio(View view) {
         Ejercicio ejercicio = new Ejercicio();
         ejercicio.setNombre(txtnombre.getText().toString());
         ejercicio.setDescripcion(txtdescripcion.getText().toString());

         FirebaseDatabase database = FirebaseDatabase.getInstance();
         DatabaseReference reference = database.getReference("Ejercicios");
         reference.push().setValue(ejercicio);

    }
}
