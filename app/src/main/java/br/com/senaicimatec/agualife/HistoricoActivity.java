package br.com.senaicimatec.agualife;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoricoActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    public ListView list_dados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        list_dados = (ListView) findViewById(R.id.list_dados);

        criarBanco();
        listarDados();
    }

    public void criarBanco(){
        try {
            bancoDados = openOrCreateDatabase("crud", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS tb_atleta(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , nome VARCHAR " +
                    " , peso REAL " +
                    " , idade INTEGER)");
            bancoDados.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void listarDados(){
        try {
            // ArrayIds = new ArrayList<>();
            bancoDados = openOrCreateDatabase("crud", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id, nome, peso, idade from tb_atleta", null);
            ArrayList<String> atleta = new ArrayList<String>();
            ArrayAdapter adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    atleta
            );
            list_dados.setAdapter(adapter);
            cursor.moveToFirst();
            while (cursor != null){
                String atletas = cursor.getString(0) + " : " +
                        cursor.getString(1) + " - " +
                        cursor.getString(2) + " Kg e " +
                        cursor.getString(3) + " anos.";
                atleta.add(atletas);
                // ArrayIds.add(cursor.getInt(0));
                cursor.moveToNext();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}