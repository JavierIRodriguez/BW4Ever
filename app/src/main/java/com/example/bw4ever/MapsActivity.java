package com.example.bw4ever;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bw4ever.modelo.Ejercicio;
import com.example.bw4ever.modelo.EjercicioService;
import com.example.bw4ever.modelo.Parque;
import com.example.bw4ever.modelo.ParqueService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mDatabase;
    private List<Parque> parquesList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getParques();
        return view;
    }

    public void getParques(){
        mDatabase =FirebaseDatabase.getInstance().getReference();


        /*Parque parque = new Parque();
        parque.setNombre("UCM");
        parque.setLatitud(-35.4355136);
        parque.setLongitud(-71.6200548);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Parques");
        reference.push().setValue(parque);*/

        mDatabase.child("Parques").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        Parque p = new Parque();
                        p.setNombre(ds.child("nombre").getValue().toString());
                        p.setLatitud(Double.parseDouble(ds.child("latitud").getValue().toString()));
                        p.setLongitud(Double.parseDouble(ds.child("longitud").getValue().toString()));
                        parquesList.add(p);
                    }
                    for (Parque p: parquesList) {
                        LatLng l = new LatLng(p.getLatitud(), p.getLongitud());
                        mMap.addMarker(new MarkerOptions().position(l).title(p.getNombre()));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng inicio = new LatLng(-35.4213, -71.6746);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicio,13));

    }


}