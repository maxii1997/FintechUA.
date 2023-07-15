package com.example.lircayhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class activity_splash extends AppCompatActivity {
    // Duración del splash screen en milisegundos
    private static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Ocultar la barra de acción (action bar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Temporizador para pasar a la siguiente actividad después de 3 segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Iniciar la siguiente actividad
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}
