package br.com.senaicimatec.agualife;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HistoricoActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    public ListView list_dados;
    public ArrayList<Integer> arrayIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        list_dados = (ListView) findViewById(R.id.list_dados);
        list_dados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DeletarItem(i);
                return true;
            }
        });
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
            arrayIds = new ArrayList<>();
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
                String atletas = cursor.getString(1) + " - " +
                        cursor.getString(2) + " Kg e " +
                        cursor.getString(3) + " anos.";
                atleta.add(atletas);
                arrayIds.add(cursor.getInt(0));
                cursor.moveToNext();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void DeletarItem(Integer i){
        try{
            bancoDados = openOrCreateDatabase("crud", MODE_PRIVATE, null);
            String sql = "DELETE FROM tb_atleta WHERE id=?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, arrayIds.get(i));
            stmt.executeUpdateDelete();
            listarDados();
            bancoDados.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(this, "Atleta Deletado", Toast.LENGTH_SHORT).show();
    }
}