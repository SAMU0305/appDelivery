package com.example.appdelivery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    private final double lat1 = -33.4378941;
    private final double lon1 = -70.6510923;
    double lat2, lon2;
    double distancia;
    FusedLocationProviderClient fusedLocationClient;
    Button btnRealizarPedido;
    Button btnCalcularDistancia;
    Button btnMostrarTotal;
    FirebaseUser usuario;
    DatabaseReference dbRef;
    private String uid;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        btnRealizarPedido = findViewById(R.id.btnRealizarPedido);
        btnCalcularDistancia = findViewById(R.id.btnCalcularDistancia);
        btnMostrarTotal = findViewById(R.id.btnMostrarTotal);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getCurrentLocationAndSave();




        btnRealizarPedido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intentoRealizarCompra = new Intent(MenuActivity.this, CompraActivity.class);

                startActivity(intentoRealizarCompra);


            }


        });


        btnCalcularDistancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentoCalcularDistancia = new Intent(MenuActivity.this, CalcularDistanciaActivity.class);

                intentoCalcularDistancia.putExtra("lat2", lat2);
                intentoCalcularDistancia.putExtra("lon2", lon2);

                startActivity(intentoCalcularDistancia);
            }
        });


        btnMostrarTotal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                distancia = CalcularDistanciaActivity.calcularDistancia(lat1, lon1, lat2, lon2);


            }


        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.menu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }

    // pedir permisos y obtener la ubicación actual
    public void getCurrentLocationAndSave() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            // solicitar permisos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {

                    if (location != null) {

                        lat2 = location.getLatitude();
                        lon2 = location.getLongitude();
                    }

                    usuario = FirebaseAuth.getInstance().getCurrentUser();

                    if (usuario != null) {

                        uid = usuario.getUid();

                        dbRef = FirebaseDatabase.getInstance().getReference("usuariosUbicacion");

                        Map<String, Object> datosUbicacion = new HashMap<>();

                        datosUbicacion.put("lat", lat2);
                        datosUbicacion.put("lon", lon2);

                        dbRef.child(uid).updateChildren(datosUbicacion)
                                .addOnCompleteListener(task -> {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(this, "Ubicación guardada en la base de datos", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(this, "Error al guardar ubicación", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                });

    }


    // manejar el resultado de la solicitud de permisos que vienen de getCurrentLocationAndSave

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getCurrentLocationAndSave();
            }
        }
    }



}