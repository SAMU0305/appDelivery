package com.example.appdelivery;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class CalcularDistanciaActivity extends AppCompatActivity implements  OnMapReadyCallback, GoogleMap.OnMapClickListener {

   // private FusedLocationProviderClient fusedLocationClient;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    final double lat1 = -33.4378941;
    final double lon1 = -70.6510923;
    private double lat2, lon2;
    private double distancia;
    private TextView txtMostrar;
    private LatLng plazaArmas;
    private LatLng miUbicacion;
    private Marker marcadorActual;
    private Button btnCalcular;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calcular_distancia);

        lat2 = getIntent().getDoubleExtra("lat2", 0.0);
        lon2 = getIntent().getDoubleExtra("lon2", 0.0);

        btnCalcular = findViewById(R.id.btnCalcular);
        txtMostrar = findViewById(R.id.textView);
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        // busca el fragmento del mapa en el layout(fragmento de nombre map en este caso)
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {


            // Pedimos que el mapa se inicialice de forma asíncrona
            // "this" se refiere a la actividad actual, que implementa OnMapReady
            // Cuando el mapa carga, se ejecutará el método onMapReady()
            mapFragment.getMapAsync(this);
        }

        //getCurrentLocation();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        btnCalcular.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                distancia = calcularDistancia(lat1, lon1, lat2, lon2);

                String distanciaStr = String.format(Locale.US, "Distancia: %.1f km", distancia);

                txtMostrar.setText(distanciaStr);


            }


        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        // para creae el evento click en el mapa
        this.mMap.setOnMapClickListener(this);

        plazaArmas = new LatLng(lat1, lon1);

        mMap.addMarker(new MarkerOptions().position(plazaArmas).title("Bodega central"));

        if (marcadorActual != null) {
            marcadorActual.remove();
        }

        miUbicacion = new LatLng(lat2, lon2);

        marcadorActual = mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicación"));


        // Crear un límite entre plazaArmas y mi ubicación actual
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(plazaArmas)
                .include(miUbicacion)
                .build();

        // Centra y ajusta el zoom para mostrar ambas ubicaciones
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300)); // padding 300 es el Margen en píxeles para que no queden pegadas al borde



    }




    // verifica permisos y obtiene la ubicación actual

   /* private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {

                        if (location != null) {

                            lat2 = location.getLatitude();
                            lon2 = location.getLongitude();

                            if (marcadorActual != null) {
                                marcadorActual.remove();
                            }

                            miUbicacion = new LatLng(lat2, lon2);

                            marcadorActual = mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicación"));


                            // Crear un límite entre plazaArmas y mi ubicación actual
                            LatLngBounds bounds = new LatLngBounds.Builder()
                                    .include(plazaArmas)
                                    .include(miUbicacion)
                                    .build();

                            // Centra y ajusta el zoom para mostrar ambas ubicaciones
                            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300)); // padding 300 es el Margen en píxeles para que no queden pegadas al borde

                        }

                    });

        }

        }*/



    public static double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {

        final double R = 6371.0;
        double latRad1 = Math.toRadians(lat1);
        double latRad2 = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(latRad1) * Math.cos(latRad2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }



    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }


}