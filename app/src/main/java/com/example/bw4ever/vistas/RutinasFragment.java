package com.example.bw4ever.vistas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bw4ever.PrincipalActivity;
import com.example.bw4ever.R;
import com.example.bw4ever.adapter.RutinaAdapter;
import com.example.bw4ever.modelo.Rutina;
import com.example.bw4ever.modelo.RutinaService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class RutinasFragment extends Fragment {

    public RutinasFragment() {
        // Required empty public constructor
    }

    // --- Elementos del Fragment ---
    RecyclerView rc;
    EditText txtnombre, txtdescripcion;
    ImageView foto;
    Button btnagregar, btngaleria;
    Uri uri_foto;
    // --- ---

    // --- Rutina a insertar ---
    final static Rutina RUTINA = new Rutina();
    // --- ---

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rutinas, container, false);
        rc = view.findViewById(R.id.rc);

        // --- Inicializar los controles ---
        txtnombre = view.findViewById(R.id.txtnombre);
        txtdescripcion = view.findViewById(R.id.txtdescripcion);
        foto = view.findViewById(R.id.foto_rutina);
        btngaleria = view.findViewById(R.id.bt_galeria);
        // --- ---

        // --- Botón Abrir Galería ---
        btngaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //Intent que permite trabajar con elementos externos en base a URI.
                startActivityForResult(intent, PrincipalActivity.CODE_GALLERY); //Desde Alumno_Principal, obtengo el código que determina usar Galería.
            }
        });
        // --- ---

        // --- ---
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(RecyclerView.VERTICAL);
        rc.setLayoutManager(lm);

        RutinaAdapter adapter = new RutinaAdapter(RutinaService.rutinaList,
                R.layout.item,getActivity());
        rc.setAdapter(adapter);
        cargaRutinasFirebase();
        return view;
    }

    public void cargaRutinasFirebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Rutinas");
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

    // --- ---

    // --- Método que recibirá al cargar una foto ---
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // --- Extraer valores ---
        PrincipalActivity principalActivity = (PrincipalActivity) getActivity();
        // --- ---

        if (txtnombre.getText().toString().isEmpty() && txtdescripcion.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Ingrese Nombre y Descripción para la Rutina", Toast.LENGTH_SHORT).show();
        } else {
            // --- Parámetros de Conexión al Storage ---
            StorageReference storage = FirebaseStorage.getInstance().getReference(); //Inicializa el Storage de Firebase.
            StorageReference folder = storage.child("IMG_Rutinas"); //Crea carpeta donde se guardará la imagen.
            StorageReference photo = folder.child(new Date().toString().trim()+""+txtnombre.getText().toString()); //Crea el nombre de la imagen.
            // --- ---

            // --- Parámetros de Conexión a la Base de Datos
            FirebaseDatabase database = FirebaseDatabase.getInstance(); //Inicializa la Database Realtime de Firebase.
            final DatabaseReference reference = database.getReference("Rutinas"); //Raíz de la BD, es como definir Tabla.
            // --- ---

            switch (requestCode) {
                // --- Caso Usuario accede a la Galería ---
                case PrincipalActivity.CODE_GALLERY:
                    if (data != null) {
                        uri_foto = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri_foto);
                            foto.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // --- Proceso de Insert de la imagen ---
                        photo.putFile(uri_foto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isSuccessful()) ;
                                Uri downloadUri = uriTask.getResult();

                                Rutina rutina = new Rutina();
                                rutina.setId(new Date().toString().trim()+"_"+txtnombre.getText().toString());
                                rutina.setNombre(txtnombre.getText().toString());
                                rutina.setDescripcion(txtdescripcion.getText().toString());
                                rutina.setUrl_foto(downloadUri.toString()); //Ingresa la URL de la Foto a la rutina.

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Rutinas");
                                reference.push().setValue(rutina);
                            }
                        });
                        // --- Fin Proceso de Insert de la imagen ---
                    }
                    break;
                // --- Fin Caso Usuario accede a la Galería ---
            }
        }
    }
    // --- ---
}