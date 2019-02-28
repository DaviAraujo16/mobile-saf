package br.senai.sp.agendacontatos.agenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import br.senai.sp.agendacontatos.R;

public class MainActivity extends AppCompatActivity {

    private ListView listaContatos;
    private Button btNovoContato;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaContatos = findViewById(R.id.list_contato);
        btNovoContato = findViewById(R.id.bt_novo_contato);

        btNovoContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abrirActivityCadastro = new Intent(MainActivity.this, CadastroActivity.class);
                startActivity(abrirActivityCadastro);
            }
        });

        String[] contatos = {"David","Kelvin","Marlon","Celso","Alisson"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, contatos);
        listaContatos.setAdapter(arrayAdapter);
    }

}
