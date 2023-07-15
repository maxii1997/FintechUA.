package com.example.lircayhub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class home extends AppCompatActivity {

    private TextView txtDollarValue;
    private TextView txtUFValue;
    private TextView txtFeriados;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtDollarValue = findViewById(R.id.txtDollarValue);
        txtUFValue = findViewById(R.id.txtUFValue);
        txtFeriados = findViewById(R.id.txtFeriados);
        Button resumen = findViewById(R.id.resumen);

        resumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resumen = new Intent(getApplicationContext(), resumen.class);
                startActivity(resumen);
            }
        });
        FetchDollarValueTask fetchDollarValueTask = new FetchDollarValueTask();
        fetchDollarValueTask.execute();

        FetchUFValueTask fetchUFValueTask = new FetchUFValueTask();
        fetchUFValueTask.execute();

        FetchFeriadosTask fetchFeriadosTask = new FetchFeriadosTask();
        fetchFeriadosTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        MenuItem itemResumen = menu.findItem(R.id.action_resumen);
        itemResumen.setTitle(getString(R.string.menu_presupuesto));

        MenuItem itemPresupuestos = menu.findItem(R.id.action_presupuestos);
        itemPresupuestos.setTitle(getString(R.string.menu_gastos));

        MenuItem itemGastos = menu.findItem(R.id.action_gastos);
        itemGastos.setTitle(getString(R.string.menu_ver_gastos));

        MenuItem itemIndicadores = menu.findItem(R.id.action_indicadores);
        itemIndicadores.setTitle(getString(R.string.menu_generar_presupuesto));

        MenuItem itemMapa = menu.findItem(R.id.action_mapa);
        itemMapa.setTitle(getString(R.string.menu_mapa));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_resumen:
                Intent pre = new Intent(getApplicationContext(), presupuesto.class);
                startActivity(pre);
                return true;

            case R.id.action_presupuestos:
                Intent gasto = new Intent(getApplicationContext(), gasto.class);
                startActivity(gasto);
                return true;

            case R.id.action_gastos:
                Intent ver = new Intent(getApplicationContext(), ver_gasto.class);
                startActivity(ver);
                return true;

            case R.id.action_indicadores:
                Intent intent = new Intent(getApplicationContext(), preferencias.class);
                startActivity(intent);
                return true;

            case R.id.action_mapa:
                Intent MAPA = new Intent(getApplicationContext(), mapa.class);
                startActivity(MAPA);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class FetchDollarValueTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String apiUrl = "https://mindicador.cl/api/dolar";
            return fetchData(apiUrl);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray seriesArray = jsonObject.getJSONArray("serie");
                JSONObject lastDataObject = seriesArray.getJSONObject(seriesArray.length() - 1);
                double valor = lastDataObject.getDouble("valor");

                txtDollarValue.setText(getString(R.string.valor_dolar) + valor);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class FetchUFValueTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String apiUrl = "https://mindicador.cl/api/uf";
            return fetchData(apiUrl);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray seriesArray = jsonObject.getJSONArray("serie");
                JSONObject lastDataObject = seriesArray.getJSONObject(seriesArray.length() - 1);
                double valor = lastDataObject.getDouble("valor");

                txtUFValue.setText(getString(R.string.valor_uf) + valor);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class FetchFeriadosTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String apiUrl = "https://apis.digital.gob.cl/fl/feriados/2023";
            return fetchData(apiUrl);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray feriadosArray = new JSONArray(result);

                // Obtener la fecha actual
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String fechaActual = dateFormat.format(calendar.getTime());

                String nombreFeriado = null;
                String fechaFeriado = null;

                // Buscar el feriado más cercano a la fecha actual
                for (int i = 0; i < feriadosArray.length(); i++) {
                    JSONObject feriadoObject = feriadosArray.getJSONObject(i);
                    String fecha = feriadoObject.getString("fecha");

                    if (fecha.equals(fechaActual)) {
                        nombreFeriado = feriadoObject.getString("nombre");
                        fechaFeriado = feriadoObject.getString("fecha");
                        break;
                    } else if (fecha.compareTo(fechaActual) > 0) {
                        nombreFeriado = feriadoObject.getString("nombre");
                        fechaFeriado = feriadoObject.getString("fecha");
                        break;
                    }
                }

                if (nombreFeriado != null && fechaFeriado != null) {
                    // Realizar acciones con el feriado obtenido
                    String feriadoText = nombreFeriado + " - " + fechaFeriado;
                    txtFeriados.setText(feriadoText);
                } else {
                    // No se encontró ningún feriado próximo
                    txtFeriados.setText("No hay feriados próximos");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private String fetchData(String apiUrl) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}
