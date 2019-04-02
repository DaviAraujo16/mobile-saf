package br.senai.sp.agendacontatos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.senai.sp.agendacontatos.R;
import br.senai.sp.agendacontatos.modelo.Contato;

public class ContatosAdapter extends BaseAdapter{

    private Context context;
    private List<Contato> contatos;

    public ContatosAdapter(Context context, List<Contato> contatos){
        this.context = context;
        this.contatos = contatos;
    }

    @Override
    public int getCount() {
        return contatos.size();
    }

    @Override
    public Object getItem(int position) {
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contatos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_listview_contatos, null);

        TextView textNomeContato = view.findViewById(R.id.text_nome_contato);
        textNomeContato.setText(contatos.get(position).getNome());

        TextView textTelefoneContato = view.findViewById(R.id.text_telefone);
        textTelefoneContato.setText(contatos.get(position).getTelefone());

        return view;
    }
}
