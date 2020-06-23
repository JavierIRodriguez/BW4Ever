package com.example.bw4ever;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bw4ever.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroActivity extends AppCompatActivity {

    EditText txtNombre, txtCorreo, txtPassword;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtNombre = findViewById(R.id.txt_registroNombre);
        txtCorreo = findViewById(R.id.txt_registroMail);
        txtPassword = findViewById(R.id.txt_registroPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    public void registrarUsuario(View view) {
        final String nombre = txtNombre.getText().toString();
        final String correo = txtCorreo.getText().toString();
        final String password = txtPassword.getText().toString();

        if(correo.isEmpty() || nombre.isEmpty() || password.isEmpty() || password.length() < 6){
            Toast.makeText(this, "Verifique datos!", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setMessage("Conectado con la Base de Datos...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(correo, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Usuario user = new Usuario();
                                user.setNombre(nombre);
                                user.setCorreo(correo);
                                user.setPassword(password);

                                DatabaseReference ref = firebaseDatabase.getReference("Usuarios");
                                ref.push().setValue(user);

                                Toast.makeText(RegistroActivity.this, "Usuario registrado con exito!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(RegistroActivity.this, "Este correo ya se encuentra registrado!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(RegistroActivity.this, "Ocurrió un error!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressDialog.dismiss();
                        }
                    });
        }

    }
}
