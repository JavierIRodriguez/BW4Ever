package com.example.bw4ever;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.bw4ever.adapter.RutinaAdapter;
import com.example.bw4ever.modelo.Rutina;
import com.example.bw4ever.modelo.RutinaService;
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

        RutinaAdapter adapter = new RutinaAdapter(RutinaService.rutinaList,
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
                 Rutina rutina = dataSnapshot.getValue(Rutina.class);
                 rutina.setId(dataSnapshot.getKey());

                 if (!RutinaService.rutinaList.contains(rutina)) {
                     RutinaService.addRutina(rutina);
                 }

                 rc.getAdapter().notifyDataSetChanged();
             }

             @Override
             public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                 Rutina rutina = dataSnapshot.getValue(Rutina.class);
                 rutina.setId(dataSnapshot.getKey());

                 if (RutinaService.rutinaList.contains(rutina)) {
                     RutinaService.updateRutina(rutina);
                 }

                 rc.getAdapter().notifyDataSetChanged();

             }

             @Override
             public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                 Rutina rutina = dataSnapshot.getValue(Rutina.class);
                 rutina.setId(dataSnapshot.getKey());

                 if (RutinaService.rutinaList.contains(rutina)) {
                     RutinaService.removeRutina(rutina);
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
         Rutina rutina = new Rutina();
         rutina.setNombre(txtnombre.getText().toString());
         rutina.setDescripcion(txtdescripcion.getText().toString());

         FirebaseDatabase database = FirebaseDatabase.getInstance();
         DatabaseReference reference = database.getReference("Ejercicios");
         reference.push().setValue(rutina);

    }


}
