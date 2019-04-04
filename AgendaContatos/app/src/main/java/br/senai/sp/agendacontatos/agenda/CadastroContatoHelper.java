
package br.senai.sp.agendacontatos.agenda;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import br.senai.sp.agendacontatos.R;
import br.senai.sp.agendacontatos.conversores.Imagem;
import br.senai.sp.agendacontatos.modelo.Contato;

public class CadastroContatoHelper {

    private TextInputLayout layoutTxtNome;
    private TextInputLayout layoutTxtEndereco;
    private TextInputLayout layoutTxtTelefone;
    private TextInputLayout layoutTxtEmail;
    private TextInputLayout layoutTxtLinkedin;
    private EditText txtNome;
    private EditText txtEndereco;
    private EditText txtTelefone;
    private EditText txtEmail;
    private EditText txtLinkedin;
    private ImageView imageViewContato;
    private Contato contato;


    public CadastroContatoHelper(CadastroActivity activity){
        txtNome = activity.findViewById(R.id.txt_nome);
        txtEndereco = activity.findViewById(R.id.txt_endereco);
        txtTelefone = activity.findViewById(R.id.txt_telefone);
        txtEmail = activity.findViewById(R.id.txt_email);
        txtLinkedin = activity.findViewById(R.id.txt_linkedin);
        imageViewContato = activity.findViewById(R.id.image_view_contato);
        contato = new Contato();
    }

    //Método que pega os dados da caixa de texto e associa a um objeto
    public Contato getContato(){
        contato.setNome(txtNome.getText().toString());
        contato.setEndereco(txtEndereco.getText().toString());
        contato.setTelefone(txtTelefone.getText().toString());
        contato.setEmail(txtEmail.getText().toString());
        contato.setLinkedin(txtLinkedin.getText().toString());

        //Foi criado um objeto bitmapDrawble que pega o img_foto com um drawble e o transforma em um bitmapDrawable
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageViewContato.getDrawable();
        //Cria um bitmap que extrai a imagem do bitmapDrawable
        Bitmap bm = bitmapDrawable.getBitmap();

        //Foi criado um ByteArrayOutPutStream(Fluxo de Saída de Array de byte)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bm,300,300,true);
        //A imagem é comprimida para ser enviada para o bd
        bitmapReduzido.compress(Bitmap.CompressFormat.PNG,0, byteArrayOutputStream);
        //Pegou o Fluxo de Saída de Array de byte e trasforma em um Array de Byte
        byte[] fotoArray = byteArrayOutputStream.toByteArray();
        //Setou a imagem
        contato.setFoto(fotoArray);

        return contato;
    }

    public void preencherFormulario(Contato contato){
        txtNome.setText(contato.getNome());
        txtEmail.setText(contato.getEmail());
        txtEndereco.setText(contato.getEndereco());
        txtTelefone.setText(contato.getTelefone());
        txtLinkedin.setText(contato.getLinkedin());

        //Se a foto não for igual a null faça a conversão de array de byte para bitmap
        if(contato.getFoto() != null){
            imageViewContato.setImageBitmap(Imagem.arrayToBitmap(contato.getFoto()));
        }
        this.contato = contato;
    }

    public boolean validarCampos(CadastroActivity activity){

        boolean validado = true;

        layoutTxtNome = activity.findViewById(R.id.layout_txt_nome);
        layoutTxtEmail = activity.findViewById(R.id.layout_txt_email);
        layoutTxtEndereco = activity.findViewById(R.id.layout_txt_endereco);
        layoutTxtTelefone = activity.findViewById(R.id.layout_txt_telefone);
        layoutTxtLinkedin = activity.findViewById(R.id.layout_txt_linkedin);

        if(txtNome.getText().toString().isEmpty()){
            layoutTxtNome.setError("Campo vazio!");
            validado = false;
        }
        if(txtTelefone.getText().toString().isEmpty()){
            layoutTxtTelefone.setError("Campo vazio!");
            validado = false;
        }
        if(txtLinkedin.getText().toString().isEmpty()){
            layoutTxtLinkedin.setError("Campo vazio!");
            validado = false;
        }
        if(txtEndereco.getText().toString().isEmpty()){
            layoutTxtEndereco.setError("Campo vazio!");
            validado = false;
        }
        if(txtEmail.getText().toString().isEmpty()) {
            layoutTxtEmail.setError("Campo vazio!");
            validado = false;
        }

        return validado;

    }

}