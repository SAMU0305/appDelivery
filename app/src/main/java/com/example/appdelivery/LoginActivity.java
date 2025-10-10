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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private EditText txtEmail;
    private EditText txtContrasenia;
    private Button btnIngresar;
    private Button btnRegistrar;
    private String email;
    private String contrasenia;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Inicializar variables después de setContentView, siempre!!
        txtEmail = findViewById(R.id.txtEmail);
        txtContrasenia = findViewById(R.id.txtContrasenia);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnRegistrar = findViewById(R.id.btnRegistrar);



        // implementacion de firebase



        // Write a message to the database

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("message");

        //myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //Log.d("ejemplo", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w("ejemplo", "Failed to read value.", error.toException());
            }
        });






        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Navegar a la actividad de registro


                Intent intentoRegistro = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intentoRegistro);
            }

        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Leer valores dentro del click
                //.trim() para eliminar espacios en blanco al inicio y al final
                email = txtEmail.getText().toString().trim();
                contrasenia = txtContrasenia.getText().toString().trim();

                if (validar(email, contrasenia)) {

                    FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(email, contrasenia)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete( Task<AuthResult> task) {

                                    if (task.isSuccessful()) {


                                        Toast.makeText(LoginActivity.this,
                                                "bienvenido",
                                                Toast.LENGTH_SHORT).show();

                                        Intent intentoMenu = new Intent(LoginActivity.this, MenuActivity.class);


                                        startActivity(intentoMenu);
                                    }

                                    else {
                                        Toast.makeText(LoginActivity.this,
                                                "Error: " + task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }




                                }
                            });



                }


            }


        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean validar(String email, String contrasenia) {

        // Normalizar (para evitar problemas con mayúsculas/minúsculas)
        String emailNormalizado = email.toLowerCase();

        // Validar que el email no estén vacíos
        if (email.isEmpty()) {
            txtEmail.setError("El email es obligatorio");
            return false;
        }

        // Validar que el email contenga gmail.com
        if(!emailNormalizado.endsWith("@gmail.com")){

            txtEmail.setError("El email no es válido");
            return false;
        }


        // Validar que la contraseña no esté vacía
        if (contrasenia.isEmpty()) {
            txtContrasenia.setError("La contraseña es obligatoria");
            return false;
        }
        return true;
    }
}
