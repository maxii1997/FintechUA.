package com.example.lircayhub;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PresupuestoDBManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "presupuestos.db";
    private static final int DATABASE_VERSION = 1;

    public PresupuestoDBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla de presupuestos
        String createTableQuery = "CREATE TABLE presupuestos (id INTEGER PRIMARY KEY AUTOINCREMENT, presupuesto TEXT, numero_pagos TEXT)";
        db.execSQL(createTableQuery);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla existente si existe
        db.execSQL("DROP TABLE IF EXISTS presupuestos");

        // Crear la tabla nuevamente
        onCreate(db);
    }

    public boolean insertarPresupuesto(String presupuesto, String numeroPagos) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("presupuesto", presupuesto);
        values.put("numero_pagos", numeroPagos);
        long result = db.insert("presupuestos", null, values);
        db.close();
        return result != -1; // Retorna true si el resultado es distinto de -1 (inserción exitosa)
    }

    @SuppressLint("Range")
    public String obtenerPresupuesto() {
        SQLiteDatabase db = this.getReadableDatabase();
        String presupuesto = "0";

        String[] columns = { "presupuesto" };
        Cursor cursor = db.query("presupuestos", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            presupuesto = cursor.getString(cursor.getColumnIndex("presupuesto"));
        }

        cursor.close();
        db.close();

        return presupuesto;
    }

    @SuppressLint("Range")
    public String obtenerNumeroPagos() {
        SQLiteDatabase db = this.getReadableDatabase();
        String numeroPagos = "0";

        String[] columns = { "numero_pagos" };
        Cursor cursor = db.query("presupuestos", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            numeroPagos = cursor.getString(cursor.getColumnIndex("numero_pagos"));
        }

        cursor.close();
        db.close();

        return numeroPagos;
    }

    public boolean actualizarPresupuesto(String presupuesto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("presupuesto", presupuesto);
        int rowsAffected = db.update("presupuestos", values, null, null);
        db.close();
        return rowsAffected > 0;
    }

        // ...

        public boolean existePresupuesto() {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT COUNT(*) FROM presupuestos";
            Cursor cursor = db.rawQuery(query, null);
            boolean existe = false;

            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                existe = count > 0;
            }

            cursor.close();
            db.close();
            return existe;
        }


}
