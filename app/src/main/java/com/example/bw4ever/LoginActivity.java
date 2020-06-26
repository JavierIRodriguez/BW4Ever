package com.example.bw4ever;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText txtcorreo, txtpassword;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtcorreo = findViewById(R.id.txt_loginMail);
        txtpassword = findViewById(R.id.txt_loginPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public void iniciarSesion(View view) {
        final String mail = txtcorreo.getText().toString();
        final String pass = txtpassword.getText().toString();

        if (mail.isEmpty() || pass.isEmpty()){
            Toast.makeText(this,"Verifique Datos Ingresados", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setMessage("Conectando con la Base de datos");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(LoginActivity.this, PrincipalActivity.class));
                            }else{
                                Toast.makeText(LoginActivity.this, "Usuario no existe", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }

    }

    public void cargarRegistro(View view) {
        startActivity(new Intent(this, RegistroActivity.class));
    }
}
