package com.example.lircayhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Editargasto extends AppCompatActivity {

    private EditText presupuestoEditText;
    private EditText gastoEditText;
    private Button guardarButton;

    private GastoHelper gastoHelper;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editargasto);

        presupuestoEditText = findViewById(R.id.editTextPresupuesto);
        Spinner spinnerCategoria = findViewById(R.id.spinnerCategoria);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categorias_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
        gastoEditText = findViewById(R.id.editTextGasto);
        guardarButton = findViewById(R.id.guardarButton);

        gastoHelper = new GastoHelper(this);

        // Obtener los valores pasados desde la actividad anterior
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemId = extras.getInt("itemId");
            double presupuesto = extras.getDouble("presupuesto");
            String categoria = extras.getString("categoria");
            String gasto = extras.getString("gasto");

            // Mostrar los valores en los EditText
            presupuestoEditText.setText(String.valueOf(presupuesto));
            spinnerCategoria.setSelection(adapter.getPosition(categoria));
            gastoEditText.setText(gasto);
        }

        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los nuevos valores ingresados en los EditText
                double nuevoPresupuesto = Double.parseDouble(presupuestoEditText.getText().toString());
                String nuevaCategoria = spinnerCategoria.getSelectedItem().toString();
                String nuevoGasto = gastoEditText.getText().toString();

                // Actualizar los valores en la base de datos
                actualizarItem(itemId, nuevoPresupuesto, nuevaCategoria, nuevoGasto);
            }
        });
    }

    private void actualizarItem(int itemId, double nuevoPresupuesto, String nuevaCategoria, String nuevoGasto) {
        SQLiteDatabase db = gastoHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GastoHelper.COLUMN_PRESUPUESTO, nuevoPresupuesto);
        values.put(GastoHelper.COLUMN_CATEGORIA, nuevaCategoria);
        values.put(GastoHelper.COLUMN_GASTO, nuevoGasto);

        String selection = GastoHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(itemId) };

        int rowsAffected = db.update(GastoHelper.TABLE_GASTOS, values, selection, selectionArgs);
        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(this, getString(R.string.expenses_updated), Toast.LENGTH_SHORT).show();
            Intent item = new Intent(getApplicationContext(), home.class);
            startActivity(item);
        } else {
            Toast.makeText(this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
        }
    }
}