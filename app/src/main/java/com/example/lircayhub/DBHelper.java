package com.example.lircayhub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public  static final String DATABASE_NAME = "lircayhub.db";
    public  static final String TABLE_NAME = "usuarios";
    public  static final String COL_ID = "id";
    public  static final String COL_NOMBRE = "nombre";
    public  static final String COL_APELLIDO = "apellido";
    public  static final String COL_EMAIL = "email";
    public  static final String COL_TELEFONO = "telefono";
    public  static final String COL_FECHA_NACIMIENTO = "fecha_nacimiento";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOMBRE + " TEXT, " +
                COL_APELLIDO + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_TELEFONO + " TEXT, " +
                COL_FECHA_NACIMIENTO + " TEXT)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertarDatos(String nombre, String apellido, String email, String telefono, String fechaNacimiento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NOMBRE, nombre);
        contentValues.put(COL_APELLIDO, apellido);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_TELEFONO, telefono);
        contentValues.put(COL_FECHA_NACIMIENTO, fechaNacimiento);

        long resultado = db.insert(TABLE_NAME, null, contentValues);
        return resultado != -1;
    }

    public Cursor obtenerTodosLosDatos() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
