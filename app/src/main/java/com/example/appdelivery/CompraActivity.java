package com.example.appdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;


public class CompraActivity extends AppCompatActivity {

    private CheckBox cbCarne;
    private CheckBox cbCamarones;
    private CheckBox cbSalmon;
    private CheckBox cbQueso;
    private CheckBox cbVino;
    private CheckBox cbJamonSerrano;
    private CheckBox cbJamon;
    private CheckBox cbPollo;
    private CheckBox cbMaricosSurtidos;
    private CheckBox cbPato;
    private CheckBox cbOstras;
    private CheckBox cbLangosta;
    private CheckBox cbTrufa;
    private CheckBox cbCordero;
    Button btnCalcular;
    Button btnVolver;
    private TextView tvTotal;
    private NumberFormat formato;
    double total = 0;
    double totalCompra = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_compra);


       // capturando los checkbox
        cbCarne = findViewById(R.id.cbCarne);
        cbCamarones = findViewById(R.id.cbCamarones);
        cbSalmon = findViewById(R.id.cbSalmon);
        cbQueso = findViewById(R.id.cbQueso);
        cbVino = findViewById(R.id.cbVino);
        cbJamonSerrano = findViewById(R.id.cbJamonSerrano);
        cbJamon = findViewById(R.id.cbJamon);
        cbPollo = findViewById(R.id.cbPollo);
        cbMaricosSurtidos = findViewById(R.id.cbMariscosSurtidos);
        cbPato = findViewById(R.id.cbPato);
        cbOstras = findViewById(R.id.cbOstras);
        cbLangosta = findViewById(R.id.cbLangosta);
        cbTrufa = findViewById(R.id.cbTrufa);
        cbCordero = findViewById(R.id.cbCordero);

        // capturando botones y textview
        btnCalcular = findViewById(R.id.btnCalcular);
        tvTotal = findViewById(R.id.tvTotal);
        btnVolver = findViewById(R.id.btnVolver);





        btnCalcular.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //para cambiar el (,) por el (.)en los miles
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setGroupingSeparator('.'); // se define el símbolo para separar miles

                // se le agrega symbols al formato
                DecimalFormat formato = new DecimalFormat("#,###", symbols);

                totalCompra = calcularTotal(total);

                tvTotal.setText("Total de la compra: " +"\n$" + formato.format(totalCompra));




            }

        });

        btnVolver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Intent intentMenu = new Intent(CompraActivity.this, MenuActivity.class);

                intentMenu.putExtra("totalCompra", totalCompra );

                startActivity(intentMenu);



            }


        });







        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



    // metodo para calcular el total de la compra
    public double calcularTotal(double total){

        boolean seleccionado = false;

        if (cbCarne.isChecked()) {
            total += 17990;
            seleccionado = true;
        }

        if (cbCamarones.isChecked()) {
            total += 13990;
            seleccionado = true;
        }

        if (cbSalmon.isChecked()) {
            total += 15990;
            seleccionado = true;
        }

        if(cbQueso.isChecked()) {
            total += 13990;
            seleccionado = true;
        }

        if (cbVino.isChecked()) {
            total += 19990;
            seleccionado = true;
        }

        if (cbJamonSerrano.isChecked()) {
            total += 12490;
            seleccionado = true;
        }

        if (cbJamon.isChecked()) {
            total += 35990;
            seleccionado = true;
        }

        if (cbPollo.isChecked()) {
            total += 7990;
            seleccionado = true;
        }

        if (cbMaricosSurtidos.isChecked()) {
            total += 14990;
            seleccionado = true;
        }

        if (cbPato.isChecked()) {
            total += 18990;
            seleccionado = true;
        }

        if (cbOstras.isChecked()) {
            total += 17990;
            seleccionado = true;
        }

        if (cbLangosta.isChecked()) {
            total += 42990;
            seleccionado = true;
        }

        if (cbTrufa.isChecked()) {
            total += 39990;
            seleccionado = true;
        }


        if (cbCordero.isChecked()) {
            total += 21990;
            seleccionado = true;
        }

        if (!seleccionado) {
            Toast.makeText(this, "Total: $0 no has seleccionado ningún producto aún.", Toast.LENGTH_SHORT).show();
            return 0;
        }

        return total;
    }
}