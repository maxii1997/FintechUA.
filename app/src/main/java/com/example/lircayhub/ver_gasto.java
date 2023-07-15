package com.example.lircayhub;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ver_gasto extends AppCompatActivity {

    private ListView registrosListView;
    private GastoHelper gastoHelper;
    private SearchView searchView;
    private SimpleCursorAdapter cursorAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_gasto);

        gastoHelper = new GastoHelper(this);
        registrosListView = findViewById(R.id.registrosListView);

        cursorAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                null,
                new String[] { GastoHelper.COLUMN_PRESUPUESTO, GastoHelper.COLUMN_CATEGORIA, GastoHelper.COLUMN_GASTO },
                new int[] { android.R.id.text1 },
                0
        );

        cursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == android.R.id.text1) {
                    TextView textView = (TextView) view;
                    double presupuesto = cursor.getDouble(cursor.getColumnIndexOrThrow(GastoHelper.COLUMN_PRESUPUESTO));
                    String categoria = cursor.getString(cursor.getColumnIndexOrThrow(GastoHelper.COLUMN_CATEGORIA));
                    String gasto = cursor.getString(cursor.getColumnIndexOrThrow(GastoHelper.COLUMN_GASTO));
                    String texto = getString(R.string.budget) + ": " + presupuesto + "\n" + getString(R.string.category) + ": " + categoria + "\n" +  getString(R.string.gasto) + ": " + gasto + "\n\n";
                    textView.setText(texto);
                    return true;
                }
                return false;
            }
        });

        registrosListView.setAdapter(cursorAdapter);

        mostrarRegistros();
        setupListViewClickListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ver_gasto, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarRegistros(newText);
                return true;
            }
        });

        MenuItem sortBudgetItem = menu.findItem(R.id.action_sort_budget);
        sortBudgetItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ordenarRegistrosPorPresupuesto();
                return true;
            }
        });

        MenuItem sortCategoryItem = menu.findItem(R.id.action_sort_category);
        sortCategoryItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ordenarRegistrosPorCategoria();
                return true;
            }
        });

        return true;
    }

    private void mostrarRegistros() {
        SQLiteDatabase db = gastoHelper.getReadableDatabase();

        String[] projection = {
                GastoHelper.COLUMN_ID,
                GastoHelper.COLUMN_PRESUPUESTO,
                GastoHelper.COLUMN_CATEGORIA,
                GastoHelper.COLUMN_GASTO
        };

        String sortOrder = GastoHelper.COLUMN_ID + " ASC";

        Cursor cursor = db.query(
                GastoHelper.TABLE_GASTOS,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        cursorAdapter.changeCursor(cursor);
    }

    private void buscarRegistros(String query) {
        SQLiteDatabase db = gastoHelper.getReadableDatabase();

        String[] projection = {
                GastoHelper.COLUMN_ID,
                GastoHelper.COLUMN_PRESUPUESTO,
                GastoHelper.COLUMN_CATEGORIA,
                GastoHelper.COLUMN_GASTO
        };

        String selection = GastoHelper.COLUMN_CATEGORIA + " LIKE ? OR " + GastoHelper.COLUMN_GASTO + " LIKE ?";
        String[] selectionArgs = { "%" + query + "%", "%" + query + "%" };

        try {
            double amount = Double.parseDouble(query);
            selection = GastoHelper.COLUMN_CATEGORIA + " LIKE ? OR " + GastoHelper.COLUMN_PRESUPUESTO + " = ? OR " + GastoHelper.COLUMN_GASTO + " = ?";
            selectionArgs = new String[] { "%" + query + "%", String.valueOf(amount), String.valueOf(amount) };
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String sortOrder = GastoHelper.COLUMN_ID + " ASC";

        Cursor cursor = db.query(
                GastoHelper.TABLE_GASTOS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        cursorAdapter.changeCursor(cursor);
    }

    private void ordenarRegistrosPorPresupuesto() {
        SQLiteDatabase db = gastoHelper.getReadableDatabase();

        String[] projection = {
                GastoHelper.COLUMN_ID,
                GastoHelper.COLUMN_PRESUPUESTO,
                GastoHelper.COLUMN_CATEGORIA,
                GastoHelper.COLUMN_GASTO
        };

        String sortOrder = GastoHelper.COLUMN_PRESUPUESTO + " ASC";

        Cursor cursor = db.query(
                GastoHelper.TABLE_GASTOS,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        cursorAdapter.changeCursor(cursor);
    }

    private void ordenarRegistrosPorCategoria() {
        SQLiteDatabase db = gastoHelper.getReadableDatabase();

        String[] projection = {
                GastoHelper.COLUMN_ID,
                GastoHelper.COLUMN_PRESUPUESTO,
                GastoHelper.COLUMN_CATEGORIA,
                GastoHelper.COLUMN_GASTO
        };

        String sortOrder = GastoHelper.COLUMN_CATEGORIA + " ASC";

        Cursor cursor = db.query(
                GastoHelper.TABLE_GASTOS,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        cursorAdapter.changeCursor(cursor);
    }

    private void editarItem(int itemId, double presupuesto, String categoria, String gasto) {
        Intent intent = new Intent(ver_gasto.this, Editargasto.class);
        intent.putExtra("itemId", itemId);
        intent.putExtra("presupuesto", presupuesto);
        intent.putExtra("categoria", categoria);
        intent.putExtra("gasto", gasto);
        startActivity(intent);
    }


    private void setupListViewClickListener() {
        registrosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = cursorAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    final int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(GastoHelper.COLUMN_ID));
                    double presupuesto = cursor.getDouble(cursor.getColumnIndexOrThrow(GastoHelper.COLUMN_PRESUPUESTO));
                    String categoria = cursor.getString(cursor.getColumnIndexOrThrow(GastoHelper.COLUMN_CATEGORIA));
                    String gasto = cursor.getString(cursor.getColumnIndexOrThrow(GastoHelper.COLUMN_GASTO));

                    String texto = getString(R.string.budget) + ": " + presupuesto + "\n" + getString(R.string.category) + ": " + categoria + "\n" + "Gasto: " + gasto + "\n\n";

                    AlertDialog.Builder builder = new AlertDialog.Builder(ver_gasto.this);
                    builder.setTitle(getString(R.string.options))
                            .setItems(new CharSequence[]{getString(R.string.edit), getString(R.string.delete)}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        // Opción Editar seleccionada
                                        editarItem(itemId, presupuesto, categoria, gasto);
                                    } else if (which == 1) {
                                        // Opción Eliminar seleccionada
                                        eliminarItem(itemId);
                                    }
                                }
                            })
                            .show();
                }
            }
        });
    }

    private void eliminarItem(int itemId) {
        SQLiteDatabase db = gastoHelper.getWritableDatabase();
        String selection = GastoHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(itemId)};
        int deletedRows = db.delete(GastoHelper.TABLE_GASTOS, selection, selectionArgs);
        if (deletedRows > 0) {
            Toast.makeText(ver_gasto.this, getString(R.string.item_deleted), Toast.LENGTH_SHORT).show();
            mostrarRegistros();
        } else {
            Toast.makeText(ver_gasto.this, getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
        }
    }

}
