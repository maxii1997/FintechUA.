package com.example.lircayhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class mapa extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private GastoHelper gastoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        gastoHelper = new GastoHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Verificar y solicitar permisos de ubicación si no se han otorgado
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            obtenerUbicacionActual();
        }
    }

    private void obtenerUbicacionActual() {
        // Obtener la última ubicación conocida
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Obtener la latitud y longitud de la ubicación actual
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            // Crear objeto LatLng para la ubicación actual
                            LatLng currentLocation = new LatLng(latitude, longitude);

                            // Mover la cámara a la ubicación actual con un zoom
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));

                            // Agregar marcador en la ubicación actual
                            mMap.addMarker(new MarkerOptions()
                                    .position(currentLocation)
                                    .title("Mi ubicación"));

                            agregarMarcadoresGastos();
                        } else {
                            Toast.makeText(mapa.this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void agregarMarcadoresGastos() {
        List<String> categorias = gastoHelper.obtenerCategoria();
        List<String> presupuestos = gastoHelper.obtenerPresupuestos();

        // Verificar si las listas tienen la misma cantidad de elementos
        if (categorias.size() == presupuestos.size()) {
            for (int i = 0; i < categorias.size(); i++) {
                String categoria = categorias.get(i);
                String presupuesto = presupuestos.get(i);

                // Obtener la ubicación del gasto (puedes reemplazar estos valores con los datos reales de la ubicación del gasto)
                double latitude = 0.0;
                double longitude = 0.0;

                // Crear objeto LatLng para la ubicación del gasto
                LatLng gastoLocation = new LatLng(latitude, longitude);

                // Agregar marcador del gasto en el mapa con el nombre del gasto como título
                mMap.addMarker(new MarkerOptions().position(gastoLocation).title(categoria));

                // Puedes usar el valor del presupuesto para realizar otras acciones, como cambiar el ícono del marcador o agregar información adicional en el título o snippet del marcador
            }
        } else {
            // Si las listas no tienen la misma cantidad de elementos, muestra un mensaje de error o realiza la lógica adecuada para manejar esta situación
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionActual();
            } else {
                Toast.makeText(this, "Se requieren permisos de ubicación para mostrar la ubicación actual", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
