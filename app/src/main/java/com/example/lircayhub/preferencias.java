package com.example.lircayhub;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class preferencias extends AppCompatActivity {

    private EditText etPresupuesto;
    private Spinner spnNumeroPagos;
    private Button btnGenerarPresupuesto;
    private PresupuestoDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        etPresupuesto = findViewById(R.id.etPresupuesto);
        spnNumeroPagos = findViewById(R.id.spnNumeroPagos);
        btnGenerarPresupuesto = findViewById(R.id.btnGenerarPresupuesto);

        // Configurar el Spinner con las opciones de número de pagos
        List<String> opcionesPagos = new ArrayList<>();
        opcionesPagos.add("5 pagos");
        opcionesPagos.add("10 pagos");
        opcionesPagos.add("15 pagos");
        opcionesPagos.add("20 pagos");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesPagos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNumeroPagos.setAdapter(adapter);

        // Crear una instancia de PresupuestoDBManager
        dbManager = new PresupuestoDBManager(this);

        btnGenerarPresupuesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generarPresupuesto();
            }
        });
    }

    private void generarPresupuesto() {
        String presupuesto = etPresupuesto.getText().toString();
        String numeroPagos = spnNumeroPagos.getSelectedItem().toString();

        // Verificar si ya existe un presupuesto en la base de datos
        boolean existePresupuesto = dbManager.existePresupuesto();

        // Actualizar o insertar el valor del presupuesto en la base de datos
        boolean operacionExitosa;
        if (existePresupuesto) {
            operacionExitosa = dbManager.actualizarPresupuesto(presupuesto);
        } else {
            operacionExitosa = dbManager.insertarPresupuesto(presupuesto, numeroPagos);
        }

        if (operacionExitosa) {
            Toast.makeText(this, "Presupuesto generado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

}
