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
    private static final int DB_VERSION = 4; // Incremented to update schema

    // Common Columns
    private static final String ID = "id";
    private static final String CREATED_AT = "created_at";
    private static final String UPDATED_AT = "updated_at";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    // Request Table
    private static final String TABELA_REQUEST = "requests";
    private static final String CUSTOMER_ID = "customer_id";
    private static final String PRIORITY = "priority";
    private static final String STATUS = "status";
    private static final String TECHNICIAN_ID = "technician_id";
    private static final String CANCELED_AT = "canceled_at";
    private static final String CANCELED_BY = "canceled_by";

    // Profile Table
    private static final String TABELA_PROFILE = "profiles";
    private static final String USER_ID = "user_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHONE = "phone";

    // Rating Table
    private static final String TABELA_RATING = "ratings";
    private static final String REQUEST_ID = "request_id";
    private static final String SCORE = "score";

    public RequestBDHelper(@Nullable Context context) {
        super(context, NOME_BD, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableRequest = "CREATE TABLE " + TABELA_REQUEST + " (" +
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

        String createTableProfile = "CREATE TABLE " + TABELA_PROFILE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_ID + " INTEGER, " +
                FIRST_NAME + " TEXT, " +
                LAST_NAME + " TEXT, " +
                PHONE + " TEXT, " +
                CREATED_AT + " TEXT, " +
                UPDATED_AT + " TEXT" +
                ");";

        String createTableRating = "CREATE TABLE " + TABELA_RATING + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                REQUEST_ID + " INTEGER, " +
                TITLE + " TEXT NOT NULL, " +
                DESCRIPTION + " TEXT, " +
                SCORE + " INTEGER, " +
                CREATED_AT + " TEXT, " +
                UPDATED_AT + " TEXT, " +
                "FOREIGN KEY(" + REQUEST_ID + ") REFERENCES " + TABELA_REQUEST + "(" + ID + ")" +
                ");";

        sqLiteDatabase.execSQL(createTableRequest);
        sqLiteDatabase.execSQL(createTableProfile);
        sqLiteDatabase.execSQL(createTableRating);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABELA_REQUEST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABELA_PROFILE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABELA_RATING);
        onCreate(sqLiteDatabase);
    }

    //region REQUEST DB
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
        db.insert(TABELA_REQUEST, null, values);
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
        int numLinhas = db.update(TABELA_REQUEST, values, ID + " = ?", new String[]{String.valueOf(r.getId())});
        return numLinhas > 0;
    }

    public boolean removerRequestBD(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int numLinhas = db.delete(TABELA_REQUEST, ID + " = ?", new String[]{String.valueOf(id)});
        return numLinhas > 0;
    }

    public void removerAllRequestsBD() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABELA_REQUEST, null, null);
    }

    public ArrayList<Request> getAllRequestsBD() {
        ArrayList<Request> requests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA_REQUEST, null, null, null, null, null, null);
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
    //endregion

    //region RATING DB
    public void adicionarRatingBD(Rating r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REQUEST_ID, r.getRequestId());
        values.put(TITLE, r.getTitle());
        values.put(DESCRIPTION, r.getDescription());
        values.put(SCORE, r.getScore());
        values.put(CREATED_AT, r.getCreatedAt());
        values.put(UPDATED_AT, r.getUpdatedAt());
        db.insert(TABELA_RATING, null, values);
    }

    public boolean editarRatingBD(Rating r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REQUEST_ID, r.getRequestId());
        values.put(TITLE, r.getTitle());
        values.put(DESCRIPTION, r.getDescription());
        values.put(SCORE, r.getScore());
        values.put(CREATED_AT, r.getCreatedAt());
        values.put(UPDATED_AT, r.getUpdatedAt());
        int numLinhas = db.update(TABELA_RATING, values, ID + " = ?", new String[]{String.valueOf(r.getId())});
        return numLinhas > 0;
    }

    public boolean removerRatingBD(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int numLinhas = db.delete(TABELA_RATING, ID + " = ?", new String[]{String.valueOf(id)});
        return numLinhas > 0;
    }

    public void removerAllRatingsBD() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABELA_RATING, null, null);
    }

    public ArrayList<Rating> getAllRatingsBD() {
        ArrayList<Rating> ratings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_RATING, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Rating auxRating = new Rating(
                        cursor.getInt(0),   // id
                        cursor.getInt(1),   // request_id
                        cursor.getString(2), // title
                        cursor.getString(3), // description
                        cursor.getInt(4),   // score
                        cursor.getString(5), // created_at
                        cursor.getString(6)  // updated_at
                );
                ratings.add(auxRating);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ratings;
    }
    //endregion
}
