package com.example.lircayhub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GastoHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gastos.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_GASTOS = "gastos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRESUPUESTO = "presupuesto";
    public static final String COLUMN_CATEGORIA = "categoria";
    public static final String COLUMN_GASTO = "gasto"; // Nueva columna para el gasto

    private static final String CREATE_TABLE_GASTOS = "CREATE TABLE " + TABLE_GASTOS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PRESUPUESTO + " REAL, "
            + COLUMN_GASTO + " TEXT, " // Agregar la nueva columna al crear la tabla
            + COLUMN_CATEGORIA + " TEXT, "
            + "latitud REAL, "
            + "longitud REAL)";

    public GastoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GASTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si hay cambios en la estructura de la tabla, puedes implementar aquí la lógica de actualización
        // Por ejemplo, eliminar la tabla existente y crear una nueva
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GASTOS);
        onCreate(db);
    }

    @SuppressLint("Range")
    public List<String> obtenerCategoria() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> categorias = new ArrayList<>();

        String[] columns = {COLUMN_CATEGORIA};
        Cursor cursor = db.query(true, TABLE_GASTOS, columns, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String categoria = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORIA));
                categorias.add(categoria);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categorias;
    }

    @SuppressLint("Range")
    public List<String> obtenerPresupuestos() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> presupuestos = new ArrayList<>();

        String[] columns = {COLUMN_PRESUPUESTO};
        Cursor cursor = db.query(true, TABLE_GASTOS, columns, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                double presupuesto = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRESUPUESTO));
                presupuestos.add(String.valueOf(presupuesto));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return presupuestos;
    }
}