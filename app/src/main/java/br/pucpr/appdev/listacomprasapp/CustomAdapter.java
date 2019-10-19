package br.pucpr.appdev.listacomprasapp;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.pucpr.appdev.listacomprasapp.Model.Categoria;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {

    ListCategoryActivity listActivity;
    List<Categoria> categoriaList;

    public CustomAdapter(ListCategoryActivity listActivity, List<Categoria> categoriaList) {
        this.listActivity = listActivity;
        this.categoriaList = categoriaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_layout, viewGroup, false);

        ViewHolder viewHolder  = new ViewHolder(itemView);

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String nome = categoriaList.get(position).getNome();
                Toast.makeText(listActivity, nome, Toast.LENGTH_SHORT).show();
                listActivity.showSubLista(categoriaList.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                listActivity.deleteData(position);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mNome.setText(categoriaList.get(position).getNome());
    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }
}
