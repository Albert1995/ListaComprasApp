package br.pucpr.appdev.listacomprasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import br.pucpr.appdev.listacomprasapp.Model.APIResponse;
import br.pucpr.appdev.listacomprasapp.Model.Categoria;
import br.pucpr.appdev.listacomprasapp.Model.SubCategoria;
import br.pucpr.appdev.listacomprasapp.webservices.ServiceBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoriaActivity extends AppCompatActivity {

    Categoria categoria;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    List<SubCategoria> lista = new ArrayList<>();
    CategoryItemAdapter adapter;
    FloatingActionButton addBtn;
    ProgressDialog pd;
    SharedPreferences prefs;
    String token, uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_itens);
        getAuth();

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
        showDataSubCategoria();
    }

    private void showDataSubCategoria() {
        pd.setTitle("Carregando Dados..");
        pd.show();

        ServiceBuilder.getSubCategoriaService(token).getAll(categoria.getId()).enqueue(new Callback<List<SubCategoria>>() {
            @Override
            public void onResponse(Call<List<SubCategoria>> call, Response<List<SubCategoria>> response) {
                if (response.isSuccessful()) {
                    lista.clear();
                    List<SubCategoria> resultado = response.body();
                    for (SubCategoria c : resultado) {
                        lista.add(c);
                    }
                    adapter = new CategoryItemAdapter(SubCategoriaActivity.this, lista);
                    recyclerView.setAdapter(adapter);
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<List<SubCategoria>> call, Throwable t) {
                Toast.makeText(SubCategoriaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });

    }

    private void save(final String descricao) {
        pd.setTitle("Salvando o dado..");
        pd.show();
        final String id = UUID.randomUUID().toString();

        ServiceBuilder.getSubCategoriaService(token).create(new SubCategoria(id, descricao, categoria.getId())).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                pd.dismiss();
                Toast.makeText(SubCategoriaActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                showDataSubCategoria();
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                t.printStackTrace();
                pd.dismiss();
                Toast.makeText(SubCategoriaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteData(int index) {
        pd.setTitle("Deletando o dado..");
        pd.show();

        SubCategoria subCat = lista.get(index);

        ServiceBuilder.getSubCategoriaService(token).delete(subCat.getId()).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.code() == 200) {
                    Toast.makeText(SubCategoriaActivity.this, "Deletado!", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    showDataSubCategoria();
                }
                else{
                    Toast.makeText(SubCategoriaActivity.this, "Não foi possível realizar esta operação!", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(SubCategoriaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    private void getAuth() {
        prefs = getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = prefs.getString("token","");
        uuid = prefs.getString("uuid","");
    }
}
