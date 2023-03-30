package br.com.senaicimatec.agualife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase bancoDados;
    EditText edit_peso;
    EditText edit_idade;
    Button bt_calcular;
    Button bt_cadastrar;
    Button bt_historico;
    TextView text_resultado;
    TextView text_nome;
    ImageView redefinir_dados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_peso = (EditText) findViewById(R.id.edit_peso) ;
        edit_idade = (EditText) findViewById(R.id.edit_idade);
        bt_calcular = (Button) findViewById(R.id.bt_calcular);
        bt_cadastrar = (Button) findViewById(R.id.bt_cadastrar);
        bt_historico = (Button) findViewById(R.id.bt_historico);
        text_resultado = (TextView) findViewById(R.id.text_resultado);
        text_nome = (TextView) findViewById(R.id.text_nome);
        redefinir_dados = (ImageView) findViewById(R.id.redefinir_dados);

        bt_calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalculaConsumo();
            }
        });

        redefinir_dados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resetar();
            }
        });

        bt_historico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , HistoricoActivity.class));
            }
        });

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inserirDados();
            }
        });
    }
    public void CalculaConsumo(){
        //condição para testar se os valores estariam vazias
        if(TextUtils.isEmpty(text_nome.getText().toString()) || TextUtils.isEmpty(edit_peso.getText().toString()) || TextUtils.isEmpty(edit_idade.getText().toString())){
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
        } else{
            String nome = text_nome.getText().toString();
            double peso = Double.parseDouble(edit_peso.getText().toString());
            int idade =  Integer.parseInt(edit_idade.getText().toString());
            double resultado = peso + idade;
            if(idade <= 55){
                text_resultado.setText("Beba " + (peso * 40) + "ml de água.");
            }
            if (idade >= 56 && idade <= 65){
                text_resultado.setText("Beba " + (peso * 30) + "ml de água.");
            }
            if(idade >= 66){
                text_resultado.setText("Beba " + (peso * 25) + "ml de água.");
            }
        }
    }

    public void Resetar(){
        edit_idade.setText("");
        edit_peso.setText("");
        text_resultado.setText("");
        text_nome.setText("");
        Toast.makeText(this, "Preencha os campos.", Toast.LENGTH_SHORT).show();
    }

    public void inserirDados(){
        if(TextUtils.isEmpty(text_nome.getText().toString()) || TextUtils.isEmpty(edit_peso.getText().toString()) || TextUtils.isEmpty(edit_idade.getText().toString())){
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
        } else {
            try {
                bancoDados = openOrCreateDatabase("crud", MODE_PRIVATE, null);
                String sql = "INSERT INTO tb_atleta (nome, peso, idade) VALUES (?, ?, ?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);

                stmt.bindString(1, text_nome.getText().toString());
                stmt.bindString(2, edit_peso.getText().toString());
                stmt.bindString(3, edit_idade.getText().toString());
                stmt.executeInsert();
                bancoDados.close();
                Toast.makeText(this, "Cadastro realizado.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}