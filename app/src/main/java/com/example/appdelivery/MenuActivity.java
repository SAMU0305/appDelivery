package com.example.appdelivery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    private final double lat1 = -33.4378941;
    private final double lon1 = -70.6510923;
    double lat2, lon2;
    private double distancia;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView txtResultadoTotal;
    Button btnRealizarPedido;
    Button btnCalcularDistancia;
    Button btnMostrarTotal;
    private FirebaseUser usuario;
    private DatabaseReference dbRef;
    private String uid;
    private double montoTotalCompra = 0;
    private double totalCompraConEnvio = 0;
    private static boolean mensajeMostrado = false;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        // obtener a traves del intent el monto total de la compra proveniente de CompraActivity
        montoTotalCompra = getIntent().getDoubleExtra("totalCompra", 0.0);

        btnRealizarPedido = findViewById(R.id.btnRealizarPedido);
        btnCalcularDistancia = findViewById(R.id.btnCalcularDistancia);
        btnMostrarTotal = findViewById(R.id.btnMostrarTotal);
        txtResultadoTotal = findViewById(R.id.txtResultado);
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


                //para cambiar el (,) por el (.)en los miles
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setGroupingSeparator('.'); // se define el símbolo para separar miles

                // se le agrega symbols al formato
                DecimalFormat formato = new DecimalFormat("#,###", symbols);

                // if para cuando no se ha seleccionado ningun producto o no se ha realizado ninguna compra
                if (montoTotalCompra == 0) {

                    txtResultadoTotal.setText("No has realizado ninguna compra aún. \nPor favor, selecciona tus productos para calcular el total a pagar.");
                    return;
                }


               // si el monto total de la compra es mayor o igual a 49990 el envío es gratis
                if(montoTotalCompra >= 49990){


                    String total = formato.format(montoTotalCompra);

                    txtResultadoTotal.setText("Total a pagar con envío incluido:\n$ " + total + "\n\n¡Felicidades! Tu compra tiene envío gratis.");
                }


               // se calcula la distancia y el monto total de la compra incluyendo el costo de envío
                else {

                    distancia = CalcularDistanciaActivity.calcularDistancia(lat1, lon1, lat2, lon2);

                    totalCompraConEnvio = MontoTotalCompra(montoTotalCompra, distancia);

                    String totalFormateado = formato.format(totalCompraConEnvio);

                    txtResultadoTotal.setText("Total a pagar con envío incluido:\n$ " + totalFormateado);
                }


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
             // verificar si hay permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            // solicitar permisos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            return;
        }
        // obtener la ubicación actual
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {

                    if (location != null) {

                        // se guarda la ubicación actual en variables globales
                        lat2 = location.getLatitude();
                        lon2 = location.getLongitude();
                    }

                    usuario = FirebaseAuth.getInstance().getCurrentUser();

                    if (usuario != null ) {


                        uid = usuario.getUid();


                        // guardar la ubicación en la base de datos.
                        dbRef = FirebaseDatabase.getInstance().getReference("usuariosUbicacion");

                        Map<String, Object> datosUbicacion = new HashMap<>();

                        datosUbicacion.put("lat", lat2);
                        datosUbicacion.put("lon", lon2);

                        dbRef.child(uid).updateChildren(datosUbicacion)
                                .addOnCompleteListener(task -> {

                                    // !mensajeMostrado para que no se muestre más de una vez el toast

                                    if (task.isSuccessful()&& !mensajeMostrado) {
                                        Toast.makeText(this, "Ubicación guardada en la base de datos", Toast.LENGTH_SHORT).show();
                                        mensajeMostrado = true;
                                    }
                                    else  if (!task.isSuccessful()){
                                        Toast.makeText(this, "Error al guardar ubicación", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                });

    }


    // manejar el resultado de la solicitud de permisos que vienen de getCurrentLocationAndSave

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        // verifica el resultado de la solicitud de permisos de ubicacion en getCurrentLocationAndSave
        if (requestCode == 1001) {

            // verifica si hay un elemento en el arreglo grantResults y si el permiso fue concedido(0)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getCurrentLocationAndSave();
            }
        }
    }

    // metodo para calcular el monto total de la compra incluyendo el costo de envío
    public double MontoTotalCompra(double montoCompra, double distancia) {

        double costoEnvio;


        if (montoCompra >= 50000) {
            costoEnvio = 0;}

        else if (montoCompra >= 25000) {

            costoEnvio = distancia * 150;
        }

        else {
            costoEnvio = distancia * 300;
        }



        return montoCompra + costoEnvio;

    }



}