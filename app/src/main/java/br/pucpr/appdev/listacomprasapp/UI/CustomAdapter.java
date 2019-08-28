package br.pucpr.appdev.listacomprasapp.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.pucpr.appdev.listacomprasapp.Model.Categoria;
import br.pucpr.appdev.listacomprasapp.R;

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<Categoria> categorias;

    public CustomAdapter(Context context, ArrayList<Categoria> categorias) {
        this.context = context;
        this.categorias = categorias;
    }

    @Override
    public int getCount() {
        return categorias.size();
    }

    @Override
    public Object getItem(int position) {
        return categorias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.lista_categorias,parent,false);
        }

        TextView txtNome= (TextView) convertView.findViewById(R.id.txtNome);

        final Categoria cate = (Categoria) this.getItem(position);

        txtNome.setText(cate.getNome());

        //ONITECLICK
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, cate.getNome(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}