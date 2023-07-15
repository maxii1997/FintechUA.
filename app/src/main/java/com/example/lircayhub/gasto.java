package com.example.lircayhub;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class gasto extends AppCompatActivity {

    private EditText presupuestoEditText;
    private EditText gastoEditText;
    private Spinner categoriaSpinner;
    private Button guardarButton;
    private GastoHelper gastoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        gastoHelper = new GastoHelper(this);
        gastoEditText = findViewById(R.id.gastoEditText);
        presupuestoEditText = findViewById(R.id.presupuestoEditText);
        categoriaSpinner = findViewById(R.id.categoriaSpinner);
        guardarButton = findViewById(R.id.guardarButton);

        // Configurar opciones de categoría en el Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categorias_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriaSpinner.setAdapter(adapter);

        // Agregar clic listener al botón de guardar
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarGasto();
            }
        });
    }

    private void guardarGasto() {
        String presupuestoText = presupuestoEditText.getText().toString().trim();
        String gastotext = gastoEditText.getText().toString().toString();
        String categoria = categoriaSpinner.getSelectedItem().toString();

        if (presupuestoText.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un presupuesto.", Toast.LENGTH_SHORT).show();
            return;
        }

        double presupuesto = Double.parseDouble(presupuestoText);

        // Obtener una instancia de la base de datos para escritura
        SQLiteDatabase db = gastoHelper.getWritableDatabase();

        // Crear un objeto ContentValues para almacenar los valores de los campos
        ContentValues values = new ContentValues();
        values.put(GastoHelper.COLUMN_PRESUPUESTO, presupuesto);
        values.put(GastoHelper.COLUMN_CATEGORIA, categoria);
        values.put(GastoHelper.COLUMN_GASTO, gastotext);

        // Verificar si tienes permiso para acceder a la ubicación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Obtener la ubicación actual
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Agregar los valores de latitud y longitud al objeto ContentValues
                    values.put("latitud", latitude);
                    values.put("longitud", longitude);
                }
            }
        }

        // Insertar el registro en la base de datos
        long newRowId = db.insert(GastoHelper.TABLE_GASTOS, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Gasto guardado con éxito.", Toast.LENGTH_SHORT).show();
        }

        // Limpiar los campos después de guardar
        presupuestoEditText.setText("");
        categoriaSpinner.setSelection(0);
    }
}
