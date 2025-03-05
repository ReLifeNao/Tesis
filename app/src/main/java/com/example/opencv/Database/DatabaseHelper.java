package com.example.opencv.Database;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SignalGo.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String DB_PATH = "/data/data/com.example.opencv/databases/";
    private static final String DB_FULL_PATH = DB_PATH + DATABASE_NAME;
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // No es necesario crear la tabla aquí porque estamos usando una base de datos prellenada
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No es necesario actualizar la base de datos aquí
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            Log.i(TAG, "Database does not exist. Copying database from assets.");

            // Asegúrate de que el directorio exista
            File dbDir = new File(DB_PATH);
            if (!dbDir.exists()) {
                dbDir.mkdirs();
                Log.i(TAG, "Database directory created: " + DB_PATH);
            }

            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database", e);
            }
        } else {
            Log.i(TAG, "Database already exists.");
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null, SQLiteDatabase.OPEN_READONLY);
            Log.i(TAG, "Database found at path: " + DB_FULL_PATH);
        } catch (SQLiteException e) {
            Log.i(TAG, "Database does not exist yet.");
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    public void printAllTables() {
        Cursor cursor = null;
        try {
            // Consulta para obtener todos los nombres de las tablas
            cursor = myDataBase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

            if (cursor.moveToFirst()) {
                do {
                    // Itera sobre todas las tablas e imprime sus nombres
                    String tableName = cursor.getString(0);
                    Log.i(TAG, "Table name: " + tableName);
                } while (cursor.moveToNext());
            } else {
                Log.i(TAG, "No tables found in the database.");
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error querying database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void copyDataBase() throws IOException {
        try {
            InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
            String outFileName = DB_FULL_PATH;
            OutputStream myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();

            Log.i(TAG, "Database copied successfully to: " + outFileName);
        } catch (IOException e) {
            Log.e(TAG, "Error copying database", e);
            throw new IOException("Error copying database", e);
        }
    }

    public void openDataBase() throws SQLiteException {
        String myPath = DB_FULL_PATH;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Log.i(TAG, "Database opened successfully at: " + myPath);
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null) {
            myDataBase.close();
            Log.i(TAG, "Database closed.");
        }
        super.close();
    }
}