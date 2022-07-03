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

import com.example.t2progmobilejogos.OBJETOS.Jogador;
import com.example.t2progmobilejogos.OBJETOS.Time;

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
                "JOGADOR_CPF TEXT UNIQUE," +
                "JOGADOR_NASC INT);";
        String sql_time = "CREATE TABLE TIME (TIME_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "TIME_DESCRICAO TEXT UNIQUE);";

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

    //Passado um jogador e um time, insere ele no banco vinculado ao time pertencente
    public String insereJogador(Jogador jogador, String descricaoTime){
        SQLiteDatabase db = getWritableDatabase();

        Integer idTime = retornaIDTime(descricaoTime);

        //Dados a serem gravados no banco
        try {
            ContentValues dadosJogador = new ContentValues();
            dadosJogador.put("JOGADOR_IDTIME", idTime);
            dadosJogador.put("JOGADOR_NOME", jogador.getJogador_nome());
            dadosJogador.put("JOGADOR_CPF", jogador.getJogador_CPF());
            dadosJogador.put("JOGADOR_NASC", jogador.getJogador_nasc());

            db.insertOrThrow("JOGADOR", null,dadosJogador);
            db.close();
        } catch (SQLiteConstraintException erro) {
            return "Jogador já cadastrado com esse CPF";
        }
        return "Sucesso ao cadastrar jogador";
    }

    //Deleta um jogador pelo CPF
    public String deletaJogador(String CPF){
        SQLiteDatabase db = getWritableDatabase();
        try {
            String sqli_deleta_jogador = "DELETE FROM JOGADOR WHERE JOGADOR_CPF = " +
                    "'" +
                    CPF +
                    "'";

            db.execSQL(sqli_deleta_jogador);
            db.close();
        } catch (SQLiteConstraintException erro){
            return "Erro de delete";
        }
        return "Jogador deletado com sucesso";
    }

    //Dado um time, Insere ele no banco
    public String insereTime(Time time){
        SQLiteDatabase db = getWritableDatabase();

        //Dados a serem gravados no banco
        try {
            ContentValues dados_time = new ContentValues();
            dados_time.put("TIME_DESCRICAO", time.getTime_descricao());

            db.insertOrThrow("TIME", null,dados_time);
            db.close();
        } catch (SQLiteConstraintException erro) {
            return "Erro de insercao, TIME já cadastrado";
        }
        return "Time cadastrado com sucesso";
    }

    //Dado uma descricao de time, realiza o delete
    public String deletaTime(String descricao){
        SQLiteDatabase db = getWritableDatabase();
        try {
            String sqli_deleta_time = "DELETE FROM TIME WHERE TIME_DESCRICAO = " +
                    "'" +
                    descricao +
                    "'";

            db.execSQL(sqli_deleta_time);
            db.close();
        } catch (SQLiteConstraintException erro){
            return "Erro de delete";
        }
        return "Time deletado com sucesso";
    }

    //Pesquisa um TIME pela descricao e o retorna pelo ID
    public Integer retornaIDTime(String descricao){
        SQLiteDatabase db = getWritableDatabase();
        String sqli_busca_time = "SELECT * FROM TIME WHERE TIME_DESCRICAO = " +
                "'" +
                descricao +
                "'";
        Cursor c = db.rawQuery(sqli_busca_time, null);
        Integer id = c.getColumnIndex("TIME_ID");
        db.close();
        c.close();
        return id;
    }

    //Funcao que retorna um Arraylist de Jogadores
    public ArrayList<Jogador> listarJogadores() {
        ArrayList<Jogador> linhas = new ArrayList<>();
        try {
            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM JOGADOR", null);

            while(c.moveToNext()){
                Jogador jogador = new Jogador();
                jogador.setJogador_nome(c.getString(c.getColumnIndexOrThrow("JOGADOR_NOME")));
                jogador.setJogador_CPF(c.getString(c.getColumnIndexOrThrow("JOGADOR_CPF")));
                jogador.setJogador_nasc(c.getInt(c.getColumnIndexOrThrow("JOGADOR_NASC")));
                linhas.add(jogador);
            }
            db.close();
            c.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return linhas;
    }
    //Funcao que retorna um Arraylist de TIMES
    public ArrayList<Time> listarTimes() {
        ArrayList<Time> linhas = new ArrayList<>();
        try {
            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM TIME", null);

            while(c.moveToNext()){
                Time time = new Time();
                time.setTime_descricao(c.getString(c.getColumnIndexOrThrow("TIME_DESCRICAO")));
                linhas.add(time);
            }
            db.close();
            c.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return linhas;
    }

    //Funcao que atualiza Jogador
    public long atualizarJogador(Jogador jogador){
        long retornoBD;
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("JOGADOR_NOME",jogador.getJogador_nome());
        values.put("JOGADOR_CPF",jogador.getJogador_CPF());
        values.put("JOGADOR_NASC", jogador.getJogador_nasc());
        String[] args = {String.valueOf(jogador.getJogador_CPF())};
        retornoBD=db.update("USUARIO",values,"JOGADOR_CPF=?",args);
        db.close();
        return retornoBD;
    }

    //Funcao que atualiza Time, dado uma nova descricao e o ID do time que se deseja alterar
    public long atualizarTime(String descricao, Integer id_time){
        long retornoBD;
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("TIME_DESCRICAO",descricao);
        String[] args = {String.valueOf(id_time)};
        retornoBD=db.update("USUARIO",values,"TIME_ID=?",args);
        db.close();
        return retornoBD;
    }
}