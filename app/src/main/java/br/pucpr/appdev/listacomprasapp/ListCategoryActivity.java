package br.pucpr.appdev.listacomprasapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.pucpr.appdev.listacomprasapp.Model.APIResponse;
import br.pucpr.appdev.listacomprasapp.Model.Categoria;
import br.pucpr.appdev.listacomprasapp.webservices.ServiceBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCategoryActivity extends AppCompatActivity {

    List<Categoria> categoriaList = new ArrayList<>();
    RecyclerView mReclycleView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton mAddBtn;
    EditText edtNomeCateg;
    FirebaseFirestore db;
    FirebaseAuth auth;
    CustomAdapter adapter;
    ProgressDialog pd;
    SharedPreferences prefs;
    String token, uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);

        getAuth();

        db = FirebaseFirestore.getInstance();
        //auth = FirebaseAuth.getInstance();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                auth.signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showData() {
        pd.setTitle("Carregando Dados..");
        pd.show();

        ServiceBuilder.getCategoriaService(token).getAll(uuid).enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful()) {
                    categoriaList.clear();
                    List<Categoria> resultado = response.body();
                    for (Categoria c : resultado) {
                        if (uuid.equals(c.getIdUsuario()))
                            categoriaList.add(c);
                    }
                    adapter = new CustomAdapter(ListCategoryActivity.this, categoriaList);
                    mReclycleView.setAdapter(adapter);
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Toast.makeText(ListCategoryActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });

    }

    public void deleteData(int index) {
        pd.setTitle("Deletando o dado..");
        pd.show();

        Categoria cat = categoriaList.get(index);

        ServiceBuilder.getCategoriaService(token).delete(cat.getId(), cat.getIdUsuario()).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.code() == 200) {
                    Toast.makeText(ListCategoryActivity.this, "Deletado!", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    showData();
                }
                else{
                    Toast.makeText(ListCategoryActivity.this, "Não foi possível realizar esta operação!", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(ListCategoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
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

        ServiceBuilder.getCategoriaService(token).create(new Categoria(nome, id, uuid)).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                pd.dismiss();
                Toast.makeText(ListCategoryActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                showData();
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                t.printStackTrace();
                pd.dismiss();
                Toast.makeText(ListCategoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showSubLista(Categoria c) {
        Intent i = new Intent(this, SubCategoriaActivity.class);
        i.putExtra("categoria", c);
        startActivity(i);
    }

    private void getAuth() {
        prefs = getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = prefs.getString("token","");
        uuid = prefs.getString("uuid","");
    }
}
