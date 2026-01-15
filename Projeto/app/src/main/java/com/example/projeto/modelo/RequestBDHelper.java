package com.example.projeto.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RequestBDHelper extends SQLiteOpenHelper {

    private static final String NOME_BD = "dbRequests3";
    private static final String TABELA_NOME = "requests";
    
    // Database Columns
    private static final String ID = "id";
    private static final String CUSTOMER_ID = "customer_id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String PRIORITY = "priority";
    private static final String STATUS = "status";
    private static final String TECHNICIAN_ID = "technician_id";
    private static final String CANCELED_AT = "canceled_at";
    private static final String CANCELED_BY = "canceled_by";
    private static final String CREATED_AT = "created_at";
    private static final String UPDATED_AT = "updated_at";

    private static final int DB_VERSION = 3; // Incremented to update schema

    public RequestBDHelper(@Nullable Context context) {
        super(context, NOME_BD, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableRequest = "CREATE TABLE " + TABELA_NOME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CUSTOMER_ID + " INTEGER, " +
                TITLE + " TEXT NOT NULL, " +
                DESCRIPTION + " TEXT NOT NULL, " +
                PRIORITY + " TEXT, " +
                STATUS + " TEXT, " +
                TECHNICIAN_ID + " INTEGER, " +
                CANCELED_AT + " TEXT, " +
                CANCELED_BY + " INTEGER, " +
                CREATED_AT + " TEXT, " +
                UPDATED_AT + " TEXT" +
                ");";

        sqLiteDatabase.execSQL(createTableRequest);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABELA_NOME);
        onCreate(sqLiteDatabase);
    }

    public void adicionarRequestBD(Request r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_ID, r.getCustomerId());
        values.put(TITLE, r.getTitle());
        values.put(DESCRIPTION, r.getDescription());
        values.put(PRIORITY, r.getPriority().name());
        values.put(STATUS, r.getStatus().name());
        values.put(TECHNICIAN_ID, r.getCurrentTechnicianId());
        values.put(CANCELED_AT, r.getCanceledAt());
        values.put(CANCELED_BY, r.getCanceledBy());
        values.put(CREATED_AT, r.getCreatedAt());
        values.put(UPDATED_AT, r.getUpdatedAt());
        db.insert(TABELA_NOME, null, values);
    }

    public boolean editarRequestBD(Request r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_ID, r.getCustomerId());
        values.put(TITLE, r.getTitle());
        values.put(DESCRIPTION, r.getDescription());
        values.put(PRIORITY, r.getPriority().name());
        values.put(STATUS, r.getStatus().name());
        values.put(TECHNICIAN_ID, r.getCurrentTechnicianId());
        values.put(CANCELED_AT, r.getCanceledAt());
        values.put(CANCELED_BY, r.getCanceledBy());
        values.put(CREATED_AT, r.getCreatedAt());
        values.put(UPDATED_AT, r.getUpdatedAt());
        int numLinhas = db.update(TABELA_NOME, values, ID + " = ?", new String[]{String.valueOf(r.getId())});
        return numLinhas > 0;
    }

    public boolean removerRequestBD(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int numLinhas = db.delete(TABELA_NOME, ID + " = ?", new String[]{String.valueOf(id)});
        return numLinhas > 0;
    }

    public void removerAllRequestsBD() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABELA_NOME, null, null);
    }

    public ArrayList<Request> getAllRequestsBD() {
        ArrayList<Request> requests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA_NOME, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Request auxRequest = new Request(
                        cursor.getInt(0), // id
                        cursor.getInt(1), // customer_id
                        cursor.getString(2), // title
                        cursor.getString(3), // description
                        Priority.valueOf(cursor.getString(4)), // priority
                        Status.valueOf(cursor.getString(5)), // status
                        cursor.getInt(6), // current_technician_id
                        cursor.getString(7), // canceled_at
                        cursor.getInt(8), // canceled_by
                        cursor.getString(9), // created_at
                        cursor.getString(10) // updated_at
                );
                requests.add(auxRequest);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return requests;
    }
}
