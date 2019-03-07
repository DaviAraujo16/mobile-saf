package br.senai.sp.agendacontatos.agenda;

        import android.content.DialogInterface;
        import android.content.Intent;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.widget.AdapterView;
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
        }else{
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
        final Contato contato = helper.getContato();
        final ContatoDAO dao = new ContatoDAO(this);

        switch (item.getItemId()) {
            case R.id.menu_salvar:
                if (helper.validarCampos(this) == true) {
                    if (contato.getId() == 0) {
                        dao.salvar(contato);
                        Toast.makeText(this, contato.getNome() + " salvo com sucesso!", Toast.LENGTH_LONG).show();
                    } else {
                        dao.atualizar(contato);
                        Toast.makeText(this, contato.getNome() + " atualizado com sucesso!", Toast.LENGTH_LONG).show();
                    }

                    dao.close();
                    finish();
                }
                break;
           case R.id.menu_del:
               AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
               new AlertDialog.Builder(this)
                       .setTitle("Excluindo Contato")
                       .setMessage("Tem certeza que deseja deletar " + contato.getNome() + "?")
                       .setPositiveButton("Sim",
                               new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       dao.excluir(contato);
                                       dao.close();
                                       finish();
                                   }
                               })
                       .setNegativeButton("Não", null)
                       .show();
               return super.onContextItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
}