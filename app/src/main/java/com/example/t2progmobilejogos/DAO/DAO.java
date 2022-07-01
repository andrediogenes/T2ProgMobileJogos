package com.example.t2progmobilejogos.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class DAO extends SQLiteOpenHelper {
    public DAO(Context context) {
        super(context, "USUARIO", null, 1);
    }

    //Criacao das tabelas no banco
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_jogador = "CREATE TABLE JOGADOR (JOGADOR_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "JOGADOR_IDTIME INTEGER," +
                "JOGADOR_NOME TEXT," +
                "JOGADOR_CPF TEXT," +
                "JOGADOR_NASC INT);";
        String sql_time = "CREATE TABLE TIME (TIME_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "TIME_DESCRICAO INTEGER);";

        db.execSQL(sql_jogador);
        db.execSQL(sql_time);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int versaoNova) {
        String sql_jogador = "DROP TABLE IF EXISTS JOGADOR;";
        String sql_time = "DROP TABLE IF EXISTS TIME;";

        db.execSQL(sql_jogador);
        db.execSQL(sql_time);

        onCreate(db);
    }
}