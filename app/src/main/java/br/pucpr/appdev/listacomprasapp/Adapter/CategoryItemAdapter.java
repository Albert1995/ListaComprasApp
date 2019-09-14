package br.pucpr.appdev.listacomprasapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import br.pucpr.appdev.listacomprasapp.Model.SubCategoria;
import br.pucpr.appdev.listacomprasapp.R;
import br.pucpr.appdev.listacomprasapp.ViewHolder;

public class CategoryItemAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<SubCategoria> lista;
    private FirebaseFirestore db;

    public CategoryItemAdapter(List<SubCategoria> lista) {
        this.lista = lista;
        db = FirebaseFirestore.getInstance();
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

            }

            @Override
            public void onItemLongClick(View view, int position) {
                db.collection("SubCategorias").document(lista.get(position).getId()).delete();
                lista.remove(position);
                notifyDataSetChanged();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getmNome().setText(lista.get(position).getDescricao());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
