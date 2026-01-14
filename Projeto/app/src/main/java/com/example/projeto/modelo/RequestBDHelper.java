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
    private static final String ID = "id", TITULO = "titulo", SERIE = "serie", AUTOR = "autor", ANO = "ano", CAPA = "capa";
    private static final int DB_VERSION = 1;
    private final SQLiteDatabase db;

    public RequestBDHelper(@Nullable Context context) {
        super(context, NOME_BD, null, DB_VERSION);
        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableRequest = "CREATE TABLE " + TABELA_NOME + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                TITULO + " TEXT NOT NULL, " +
                SERIE + " TEXT NOT NULL, " +
                AUTOR + " TEXT NOT NULL, " +
                ANO + " INTEGER NOT NULL, " +
                CAPA + " TEXT " +
                ");";

        sqLiteDatabase.execSQL(createTableRequest);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABELA_NOME);
        this.onCreate(sqLiteDatabase);
    }

    public Request adicionarRequestBD(Request l) {
        ContentValues values = new ContentValues();
        values.put(ID, l.getId());
        values.put(SERIE, l.getSerie());
        values.put(AUTOR, l.getAutor());
        values.put(ANO, l.getAno());
        values.put(CAPA, l.getCapa());
        long id = this.db.insert(TABELA_NOME, null, values);
        if (id > 1)
            return l;
        return null;
    }

    public boolean editarRequestBD(Request l) {
        ContentValues values = new ContentValues();
        values.put(TITULO, l.getTitulo());
        values.put(SERIE, l.getSerie());
        values.put(AUTOR, l.getAutor());
        values.put(ANO, l.getAno());
        values.put(CAPA, l.getCapa());
        int numLinhas = this.db.update(TABELA_NOME, values, ID + " = ?", new String[]{l.getId() + ""});
        return numLinhas>0;
}
    public boolean removerRequestBD(int id) {
        int numLinhas = this.db.delete(TABELA_NOME, ID + " = ?", new String[]{id + ""});
        return numLinhas>0;
    }

    public void removerAllRequestsBD() {
        this.db.delete(TABELA_NOME, null, null);
    }

    public ArrayList<Request> getAllRequestsBD() {
        ArrayList<Request> requests = new ArrayList<>();
        //int id, int capa, int ano, String titulo, String serie, String autor
        Cursor cursor = this.db.query(TABELA_NOME, new String[]{ID, CAPA, ANO, TITULO, SERIE, AUTOR}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Request auxRequest=new Request(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
                requests.add(auxRequest);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return requests;
        }
    }
