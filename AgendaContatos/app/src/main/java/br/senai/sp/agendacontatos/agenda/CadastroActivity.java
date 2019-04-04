package br.senai.sp.agendacontatos.agenda;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import br.senai.sp.agendacontatos.BuildConfig;
import br.senai.sp.agendacontatos.R;
import br.senai.sp.agendacontatos.dao.ContatoDAO;
import br.senai.sp.agendacontatos.modelo.Contato;

public class CadastroActivity extends AppCompatActivity {

    public static final int GALERY_REQUEST = 155;
    public static final int CAMERA_REQUEST = 144;
    //Declaração de atributos
    CadastroContatoHelper helper;
    private Button btnGaleria;
    private Button btnCamera;
    private Button btnLigacao;
    private ImageView imageViewContato;
    private String caminhoFoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        helper = new CadastroContatoHelper(this);

        //Pega os dados serializados da MainActivity
        final Intent intent = getIntent();
        Contato contato = (Contato) intent.getSerializableExtra("contato");

        //Preenche o formulário se o contato não for null
        if (contato != null) {
            helper.preencherFormulario(contato);
        }

        //Conexão entre buttons do xml e atributo do CadastroActivity
        btnGaleria = findViewById(R.id.bt_galeria);
        btnCamera = findViewById(R.id.bt_camera);
        btnLigacao = findViewById(R.id.bt_chamada);
        imageViewContato = findViewById(R.id.image_view_contato);

        //Criando o ClickListeners dos botões
        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Criei uma Intent que pega conteúdo
                Intent intentGaleria = new Intent(Intent.ACTION_GET_CONTENT);
                //Setei o tipo de conteúdo que a Intent vai pegar (todos os tipos de imagens)
                intentGaleria.setType("image/*");
                //Iniciei a Activity da galeria buscando um arquivo de imagem como Result
                startActivityForResult(intentGaleria, GALERY_REQUEST);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intenção de abrir a camera
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String nomeArquivo = "/IMG_" + System.currentTimeMillis() + "/.jpg";
                //caminho foto recebe uma String
                caminhoFoto = getExternalFilesDir(null) + nomeArquivo;
                //arquivo_foto é um objeto file que tem o caminho de onde as imagens da camera serão salvas
                File arquivoFoto = new File(caminhoFoto);
                //Caminho foto
                Uri fotoUri = FileProvider.getUriForFile(
                        //contexto/activity
                        CadastroActivity.this,
                        //Id dá aplicação
                        BuildConfig.APPLICATION_ID + ".provider",
                        //arquivo
                        arquivoFoto
                );
                //Concatena a intenção de abrir a camera com um caminho de saída onde as imagem vai ser salva
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intentCamera, CAMERA_REQUEST);

            }
        });

        btnLigacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "Your Phone_number"));
                if (ActivityCompat.checkSelfPermission(CadastroActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);

            }
        });

    }

    //Criei um método que recebe o resultado de um StartActivityForResult
    //Ela Recebe como argumento o código da requisição, o código do Resultado, e o Resultado
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("RESULT_CODE", String.valueOf(resultCode));
        //Se o ResultCode tiver um resultado/ não foi cancelado
        if(resultCode == RESULT_OK){
            try {
                //Se o código de request for igual ao GALERY_REQUEST pega o Result da intentGaleria
               if(requestCode == GALERY_REQUEST){
                   //Esse objeto recebe os dados da galeria e o coloca em um "fluxo de entrada de dados"
                   //Porém é necessário fazer um casting para transformar os dados de Data para InputStream
                   InputStream inputStream = getContentResolver().openInputStream(data.getData());
                   //Cria um Bitmap e transforma o InputStream em bitmap
                   Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                   //Reduz o tamanho do Bitmap
                   Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap,300,300, true);
                   //Coloca no ImageViewContato a imagem da galeria
                   imageViewContato.setImageBitmap(bitmapReduzido);
               }if(requestCode == CAMERA_REQUEST){
                    Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
                    Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap,300,300, true);
                    imageViewContato.setImageBitmap(bitmapReduzido);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

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