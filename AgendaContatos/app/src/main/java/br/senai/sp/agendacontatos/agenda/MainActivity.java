package br.senai.sp.agendacontatos.agenda;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import br.senai.sp.agendacontatos.R;
import br.senai.sp.agendacontatos.adapter.ContatosAdapter;
import br.senai.sp.agendacontatos.dao.ContatoDAO;
import br.senai.sp.agendacontatos.modelo.Contato;

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
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        //relaciona o menu de contexto com a listaContatos
        registerForContextMenu(listaContatos);

        //Quando um contato pe selecionado ele redireciona para a outra activity e envia os dados daquele contato
        listaContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato = (Contato) listaContatos.getItemAtPosition(position);
                Intent cadastro = new Intent(MainActivity.this, CadastroActivity.class);
                cadastro.putExtra("contato", contato);
                startActivity(cadastro);

            }
        });

    }

    //Esse método dispara uma função quando o menu de contexto é criado
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_context_lista_contatos, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final Contato contato = (Contato) listaContatos.getItemAtPosition(info.position);

        new AlertDialog.Builder(this)
                .setTitle("Excluindo Contato")
                .setMessage("Tem certeza que deseja deletar o contato " + contato.getNome() + "?")

                .setPositiveButton("Sim",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ContatoDAO dao = new ContatoDAO(MainActivity.this);
                                dao.excluir(contato);
                                dao.close();
                                carregarLista();
                            }
                        })
                .setNegativeButton("Não", null)
                .show();

        return super.onContextItemSelected(item);
    }

    //Esse método carrega lista de contatos salvos
    private void carregarLista(){
        ContatoDAO dao = new ContatoDAO(this);
        List<Contato> contatos = dao.getContatos();
        dao.close();

        ContatosAdapter listaContatoAdapter = new ContatosAdapter(this, contatos);
        listaContatos.setAdapter(listaContatoAdapter);
    }

    @Override
    protected void onResume() {
        carregarLista();
        super.onResume();
    }
}
