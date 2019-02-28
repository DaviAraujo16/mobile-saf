
package br.senai.sp.agendacontatos.agenda;

import android.widget.EditText;

import br.senai.sp.agendacontatos.R;
import br.senai.sp.agendacontatos.modelo.Contato;

public class CadastroContatoHelper {

    private EditText txtNome;
    private EditText txtEndereco;
    private EditText txtTelefone;
    private EditText txtEmail;
    private EditText txtLinkedin;
    private Contato contato;

    public CadastroContatoHelper(CadastroActivity activity){
        txtNome = activity.findViewById(R.id.txt_nome);
        txtEndereco = activity.findViewById(R.id.txt_endereco);
        txtTelefone = activity.findViewById(R.id.txt_telefone);
        txtEmail = activity.findViewById(R.id.txt_email);
        txtLinkedin = activity.findViewById(R.id.txt_linkedin);
        contato = new Contato();
    }

    //Método que pega os dados da caixa de texto e associa a um objeto
    public Contato getContato(){
        contato.setNome(txtNome.getText().toString());
        contato.setEndereco(txtEndereco.getText().toString());
        contato.setTelefone(txtTelefone.getText().toString());
        contato.setEmail(txtEmail.getText().toString());
        contato.setLinkedin(txtLinkedin.getText().toString());

        return contato;
    }

    public void preencherFormulario(Contato contato){
        txtNome.setText(contato.getNome());
        txtEmail.setText(contato.getEmail());
        txtEndereco.setText(contato.getEndereco());
        txtTelefone.setText(contato.getTelefone());
        txtLinkedin.setText(contato.getLinkedin());

        this.contato = contato;
    }


}