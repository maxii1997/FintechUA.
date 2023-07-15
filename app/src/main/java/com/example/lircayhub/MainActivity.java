package com.example.lircayhub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verificar si existen datos guardados al iniciar la actividad
        if (existenDatosGuardados()) {
            // Redirigir a la actividad de datos guardados
            Intent intent = new Intent(MainActivity.this, home.class);
            startActivity(intent);
            finish(); // Finalizar la actividad actual para que no se pueda volver atrás
        } else {
            showWelcomeDialog();
        }
    }

    private void showWelcomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.welcome_title))
                .setMessage(getString(R.string.welcome_message))
                .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), configuracion.class);
                        startActivity(intent);
                    }
                })
                .setCancelable(false)
                .show();

        // Personalizar el color de texto del botón del cuadro de diálogo
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.black));
            }
        });
    }

    private boolean existenDatosGuardados() {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME, null);
        boolean existenDatos = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return existenDatos;
    }
}
