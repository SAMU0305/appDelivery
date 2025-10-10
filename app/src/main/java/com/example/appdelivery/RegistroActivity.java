package com.example.appdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroActivity extends AppCompatActivity {

    EditText txtRmail;
    EditText txtRcontrasenia;
    EditText txtVcontrasenia;
    Button btnConfirmarRegistro;

    private String email;
    private String contrasenia;
    private String contraseniaVerif;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        // Inicializar variables después de setContentView siempre
        txtRmail = findViewById(R.id.txtRmail);
        txtRcontrasenia = findViewById(R.id.txtRcontrasenia);
        txtVcontrasenia = findViewById(R.id.txtVcontrasenia);
        btnConfirmarRegistro = findViewById(R.id.btnConfirmarRegistro);

        btnConfirmarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Leer valores dentro del click
                email = txtRmail.getText().toString().trim();
                contrasenia = txtRcontrasenia.getText().toString().trim();
                contraseniaVerif = txtVcontrasenia.getText().toString().trim();


                if (validar(email, contrasenia, contraseniaVerif)) {

                    // metodo para registrar usuario con firebase
                    FirebaseAuth.getInstance()
                            .createUserWithEmailAndPassword(email, contrasenia)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete( Task<AuthResult> task) {

                                    if (task.isSuccessful()) {


                                        Toast.makeText(RegistroActivity.this,
                                                "Registro exitoso",
                                                Toast.LENGTH_SHORT).show();

                                        Intent intentoLogin = new Intent(RegistroActivity.this, LoginActivity.class);


                                        startActivity(intentoLogin);
                                    }

                                    else {
                                        Toast.makeText(RegistroActivity.this,
                                                "Error: " + task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }




                                }
                            });
                }
            }
        });

        // Ajuste de padding al tamaño de las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean validar(String email, String contrasenia, String contraseniaVerif) {
        if (email.isEmpty()) {
            txtRmail.setError("El email es obligatorio");
            return false;
        }

        if (!email.contains("@") || !email.endsWith("gmail.com")) {
            txtRmail.setError("El email no es válido");
            return false;
        }

        if (contrasenia.isEmpty()) {
            txtRcontrasenia.setError("La contraseña es obligatoria");
            return false;
        }

        if (contraseniaVerif.isEmpty()) {
            txtVcontrasenia.setError("La verificación de la contraseña es obligatoria");
            return false;
        }

        if (!contrasenia.equals(contraseniaVerif)) {
            txtVcontrasenia.setError("Las contraseñas no coinciden");
            return false;
        }

        return true;
    }
}