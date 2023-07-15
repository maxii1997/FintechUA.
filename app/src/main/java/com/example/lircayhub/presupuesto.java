package com.example.lircayhub;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lircayhub.PresupuestoDBManager;

public class presupuesto extends AppCompatActivity {

    private TextView tvPresupuesto;
    private PresupuestoDBManager dbManager;
    private EditText etArriendo, etAlimentacion, etTransporte, etServiciosBasicos, etEducacion, etDeudas, etAhorros;
    private Button btnCalcular;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto);

        tvPresupuesto = findViewById(R.id.tvPresupuesto);
        etArriendo = findViewById(R.id.etArriendo);
        etAlimentacion = findViewById(R.id.etAlimentacion);
        etTransporte = findViewById(R.id.etTransporte);
        etServiciosBasicos = findViewById(R.id.etServiciosBasicos);
        etEducacion = findViewById(R.id.etEducacion);
        etDeudas = findViewById(R.id.etDeudas);
        etAhorros = findViewById(R.id.etAhorros);
        btnCalcular = findViewById(R.id.btnCalcular);

        dbManager = new PresupuestoDBManager(this);



        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validarCamposObligatorios()) {
                    return;
                }
                // Obtener los valores ingresados en los EditText
                double arriendo = Double.parseDouble(etArriendo.getText().toString());
                double alimentacion = Double.parseDouble(etAlimentacion.getText().toString());
                double transporte = Double.parseDouble(etTransporte.getText().toString());
                double serviciosBasicos = Double.parseDouble(etServiciosBasicos.getText().toString());
                double educacion = Double.parseDouble(etEducacion.getText().toString());
                double deudas = Double.parseDouble(etDeudas.getText().toString());
                double ahorros = Double.parseDouble(etAhorros.getText().toString());


                // Obtener el valor del presupuesto desde la base de datos
                double presupuestoTotal = Double.parseDouble(dbManager.obtenerPresupuesto());

                // Calcular los porcentajes
                double porcentajeArriendo = (arriendo / 100) * presupuestoTotal;
                double porcentajeAlimentacion = (alimentacion / 100) * presupuestoTotal;
                double porcentajeTransporte = (transporte / 100) * presupuestoTotal;
                double porcentajeServiciosBasicos = (serviciosBasicos / 100) * presupuestoTotal;
                double porcentajeEducacion = (educacion / 100) * presupuestoTotal;
                double porcentajeDeudas = (deudas / 100) * presupuestoTotal;
                double porcentajeAhorros = (ahorros / 100) * presupuestoTotal;

                // Actualizar los TextViews con los valores correspondientes
                TextView tvPorcentajeArriendo = findViewById(R.id.primero);
                TextView tvPorcentajeAlimentacion = findViewById(R.id.alimentacion);
                TextView tvPorcentajeTransporte = findViewById(R.id.transporte);
                TextView tvPorcentajeServiciosBasicos = findViewById(R.id.basicos);
                TextView tvPorcentajeEducacion = findViewById(R.id.educacion);
                TextView tvPorcentajeDeudas = findViewById(R.id.deudas);
                TextView tvPorcentajeAhorros = findViewById(R.id.ahorros);


                tvPorcentajeArriendo.setText("Arriendo/Hipoteca: " + porcentajeArriendo);
                tvPorcentajeAlimentacion.setText("Alimentación: " + porcentajeAlimentacion);
                tvPorcentajeTransporte.setText("Transporte: " + porcentajeTransporte);
                tvPorcentajeServiciosBasicos.setText("Servicios Básicos: " + porcentajeServiciosBasicos);
                tvPorcentajeEducacion.setText("Educación: " + porcentajeEducacion);
                tvPorcentajeDeudas.setText("Deudas: " + porcentajeDeudas);
                tvPorcentajeAhorros.setText("Ahorros e Inversiones: " + porcentajeAhorros);
            }
        });



        // Obtener el valor del presupuesto desde la base de datos
        String presupuesto = dbManager.obtenerPresupuesto();

        // Establecer el valor del presupuesto en el TextView
        String textoPresupuesto = "Presupuesto total: " + presupuesto;
        tvPresupuesto.setText(textoPresupuesto);
    }

    private boolean validarCamposObligatorios() {
        String arriendoStr = etArriendo.getText().toString();
        String alimentacionStr = etAlimentacion.getText().toString();
        String transporteStr = etTransporte.getText().toString();
        String serviciosBasicosStr = etServiciosBasicos.getText().toString();
        String educacionStr = etEducacion.getText().toString();
        String deudasStr = etDeudas.getText().toString();
        String ahorrosStr = etAhorros.getText().toString();

        if (arriendoStr.isEmpty() || alimentacionStr.isEmpty() || transporteStr.isEmpty() ||
                serviciosBasicosStr.isEmpty() || educacionStr.isEmpty() || deudasStr.isEmpty() ||
                ahorrosStr.isEmpty()) {
            Toast.makeText(presupuesto.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
