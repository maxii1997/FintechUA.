package com.example.lircayhub;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import java.util.List;

public class resumen extends AppCompatActivity {

    private PresupuestoDBManager presupuestoDBManager;
    private GastoHelper gastoHelper;

    private TextView txtPresupuesto;
    private TextView txtNumeroPagos;
    private TextView txtCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        presupuestoDBManager = new PresupuestoDBManager(this);
        gastoHelper = new GastoHelper(this);

        txtPresupuesto = findViewById(R.id.txtPresupuesto);
        txtNumeroPagos = findViewById(R.id.txtNumeroPagos);
        txtCategoria = findViewById(R.id.txtCategoria);

        // Mostrar los valores en los TextView
        String presupuesto = presupuestoDBManager.obtenerPresupuesto();
        String numeroPagos = presupuestoDBManager.obtenerNumeroPagos();
        List<String> categoriasList = gastoHelper.obtenerCategoria();
        List<String> presupuestosList = gastoHelper.obtenerPresupuestos();

        StringBuilder sb = new StringBuilder();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        for (int i = 0; i < categoriasList.size(); i++) {
            String categoria = categoriasList.get(i);
            String presupuestoCategoria = presupuestosList.get(i);
            double porcentaje = (Double.parseDouble(presupuestoCategoria) / Double.parseDouble(presupuesto)) * 100;
            String porcentajeFormatted = String.format("%.2f", porcentaje); // Formatear el porcentaje con 2 decimales

            String textoPorcentaje = porcentajeFormatted + "%";
            SpannableStringBuilder spannablePorcentaje = new SpannableStringBuilder(textoPorcentaje);
            ForegroundColorSpan colorSpan;
            if (porcentaje <= 100) {
                colorSpan = new ForegroundColorSpan(Color.GREEN);
            } else {
                colorSpan = new ForegroundColorSpan(Color.RED);
            }
            spannablePorcentaje.setSpan(colorSpan, 0, textoPorcentaje.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            sb.append(categoria).append(" ").append(presupuestoCategoria).append(" (").append(spannablePorcentaje).append(")");
            if (i < categoriasList.size() - 1) {
                sb.append("\n");
            }

            // Agregar el texto al SpannableStringBuilder
            spannableStringBuilder.append(sb.toString());
            // Establecer color del porcentaje según el valor
            if (porcentaje <= 100) {
                spannableStringBuilder.setSpan(colorSpan, sb.length() - textoPorcentaje.length(), sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                spannableStringBuilder.setSpan(colorSpan, sb.length() - textoPorcentaje.length(), sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            sb.setLength(0); // Reiniciar el StringBuilder para la siguiente iteración
        }

        String categoriaPresupuesto = spannableStringBuilder.toString();

        txtPresupuesto.setText(presupuesto);
        txtNumeroPagos.setText(numeroPagos);
        txtCategoria.setText(categoriaPresupuesto);
    }
}
