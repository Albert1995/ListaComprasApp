package br.pucpr.appdev.listacomprasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import br.pucpr.appdev.listacomprasapp.Model.Categoria;

public class ListCategoryActivity extends AppCompatActivity {

    List<Categoria> categoriaList = new ArrayList<>();
    RecyclerView mReclycleView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton mAddBtn;
    EditText edtNomeCateg;

    FirebaseFirestore db;

    CustomAdapter adapter;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);

        db = FirebaseFirestore.getInstance();

        mReclycleView = findViewById(R.id.recycler_view);
        mAddBtn = findViewById(R.id.addBtn);

        mReclycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mReclycleView.setLayoutManager(layoutManager);

        pd = new ProgressDialog(this);

        showData();

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInputDialog();
            }
        });
    }

    private void showData() {
        pd.setTitle("Carregando Dados..");
        pd.show();

        db.collection("Categorias")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        categoriaList.clear();
                        pd.dismiss();

                        for (DocumentSnapshot doc : task.getResult()) {
                            Categoria categoria = new Categoria(doc.getString("nome"), doc.getString("id"));
                            categoriaList.add(categoria);
                        }

                        adapter = new CustomAdapter(ListCategoryActivity.this, categoriaList);

                        mReclycleView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();

                        Toast.makeText(ListCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteData(int index) {
        pd.setTitle("Deletando o dado..");
        pd.show();

        db.collection("Categorias").document(categoriaList.get(index).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ListCategoryActivity.this, "Deletado!", Toast.LENGTH_SHORT).show();
                        showData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ListCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //DISPLAY INPUT DIALOG
    private void displayInputDialog() {
        final Dialog d = new Dialog(this);
        d.setTitle("Save To Firebase");
        d.setContentView(R.layout.input_dialog);

        edtNomeCateg = (EditText) d.findViewById(R.id.edtNomeCateg);
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
                    saveItem(nome);
                    showData();
                    d.dismiss();

                } else {
                    Toast.makeText(ListCategoryActivity.this, "Por favor, preencha o nome da categoria", Toast.LENGTH_SHORT).show();
                }

            }
        });

        d.show();
    }


    private void saveItem(String nome){
        pd.setTitle("Salvando dados...");

        pd.show();

        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("nome", nome);
        doc.put("id", id);

        db.collection("Categorias").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(ListCategoryActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(ListCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
