package com.example.lircayhub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class configuracion extends AppCompatActivity {
    private EditText editTextNombre;
    private EditText editTextApellido;
    private EditText editTextEmail;
    private EditText editTextTelefono;
    private EditText editTextFechaNacimiento;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextTelefono = findViewById(R.id.editTextTelefono);
        editTextFechaNacimiento = findViewById(R.id.editTextFechaNacimiento);

        dbHelper = new DBHelper(this);
    }

    public void showDatePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Actualizar el campo de fecha con el valor seleccionado
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editTextFechaNacimiento.setText(selectedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }


    public void guardarDatos(View view) {
        Log.d("GuardarDatos", "Método guardarDatos() llamado");

        String nombre = editTextNombre.getText().toString();
        String apellido = editTextApellido.getText().toString();
        String email = editTextEmail.getText().toString();
        String telefono = editTextTelefono.getText().toString();
        String fechaNacimiento = editTextFechaNacimiento.getText().toString();

        boolean resultado = dbHelper.insertarDatos(nombre, apellido, email, telefono, fechaNacimiento);

        if (resultado) {
            Toast.makeText(this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show();
            limpiarCampos();
            Intent exito = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(exito);
        } else {
            Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos() {
        editTextNombre.setText("");
        editTextApellido.setText("");
        editTextEmail.setText("");
        editTextTelefono.setText("");
        editTextFechaNacimiento.setText("");
    }
}
