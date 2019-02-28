package br.senai.sp.agendacontatos.agenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import br.senai.sp.agendacontatos.R;
import br.senai.sp.agendacontatos.dao.ContatoDAO;
import br.senai.sp.agendacontatos.modelo.Contato;

public class CadastroActivity extends AppCompatActivity {

    CadastroContatoHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        helper = new CadastroContatoHelper(this);

        Intent intent = getIntent();
        Contato contato = (Contato) intent.getSerializableExtra("contato");

        if(contato != null){
            helper.preencherFormulario(contato);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cadastro_contatos, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_salvar:
                helper = new CadastroContatoHelper(this);
                Contato contato = helper.getContato();
                ContatoDAO dao = new ContatoDAO(this);

                if(contato.getId() == 0){
                    dao.salvar(contato);
                    Toast.makeText(this,contato.getId(),Toast.LENGTH_LONG).show();
                }else{
                    dao.atualizar(contato);
                    Toast.makeText(this,"Contato atualizado com sucesso",Toast.LENGTH_LONG).show();
                }
                dao.close();
                finish();
                break;
            case R.id.menu_del:
                helper = new CadastroContatoHelper(this);
                dao = new ContatoDAO(this);
                contato = helper.getContato();

                Toast.makeText(this,"excluir",Toast.LENGTH_LONG).show();
                    dao.excluir(contato);
                    dao.close();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}