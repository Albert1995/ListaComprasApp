package br.pucpr.appdev.listacomprasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.pucpr.appdev.listacomprasapp.Adapter.CategoryItemAdapter;
import br.pucpr.appdev.listacomprasapp.Model.Categoria;
import br.pucpr.appdev.listacomprasapp.Model.SubCategoria;

public class SubCategoriaActivity extends AppCompatActivity {

    Categoria categoria;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    List<SubCategoria> lista = new ArrayList<>();
    CategoryItemAdapter adapter;
    FloatingActionButton addBtn;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_itens);

        // Firebase
        db = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(this);

        categoria = (Categoria) getIntent().getSerializableExtra("categoria");

        addBtn = findViewById(R.id.addBtnSub);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = new Dialog(SubCategoriaActivity.this);
                d.setTitle("Save To Firebase");
                d.setContentView(R.layout.input_dialog);

                TextView titulo = d.findViewById(R.id.txtTitulo);
                titulo.setText("Nova SubCategoria");

                TextView label = d.findViewById(R.id.txtNomeCategoria);
                label.setText("Nome da SubCategoria");

                final EditText edtNomeCateg = (EditText) d.findViewById(R.id.edtNomeCateg);
                Button saveBtn = (Button) d.findViewById(R.id.saveBtn);

                //SAVE
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //GET DATA
                        String nome = edtNomeCateg.getText().toString();

                        //SET DATA
                        Categoria s = new Categoria();
                        s.setNome(nome);

                        //SIMPLE VALIDATION
                        if (nome != null && nome.length() > 0) {
                            //THEN SAVE
                            save(nome);
                            d.dismiss();

                        } else {
                            Toast.makeText(SubCategoriaActivity.this, "Por favor, preencha o nome da categoria", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                d.show();
            }
        });

        recyclerView = findViewById(R.id.rv_category_itens);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getListaFromDB();
    }

    private void getListaFromDB() {
        pd.setTitle("Carregando Dados..");
        pd.show();
        db.collection("SubCategorias")
                .whereEqualTo("categoria", categoria.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            lista.add(new SubCategoria(ds.getId(), ds.getString("descricao")));
                        }

                        adapter = new CategoryItemAdapter(lista);
                        recyclerView.setAdapter(adapter);
                        pd.dismiss();
                    }
                });
    }

    private void save(final String descricao) {
        pd.setTitle("Salvando o dado..");
        pd.show();
        final String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("descricao", descricao);
        doc.put("categoria", categoria.getId());

        db.collection("SubCategorias").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(SubCategoriaActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                        lista.add(new SubCategoria(id, descricao));
                        adapter.notifyDataSetChanged();
                        pd.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(SubCategoriaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
