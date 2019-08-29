package br.pucpr.appdev.listacomprasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.pucpr.appdev.listacomprasapp.Firebase.FirebaseHelper;
import br.pucpr.appdev.listacomprasapp.Model.Categoria;
import br.pucpr.appdev.listacomprasapp.UI.CustomAdapter;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseHelper helper;
    CustomAdapter adapter;
    ListView lv;
    EditText edtNomeCateg;
    ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Attaching the layout to the toolbar object
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Setting toolbar as the ActionBar with setSupportActionBar() call
        //setSupportActionBar(toolbar);

        pg = new ProgressDialog(this);

        lv = (ListView) findViewById(R.id.listView);

        //INITIALIZE FIREBASE DB
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        //helper = new FirebaseHelper(db);

        //ADAPTER
        //adapter = new CustomAdapter(this, helper.retrieve());
        //lv.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            displayInputDialog();
            //Toast.makeText(this, "Deu boa!!!!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //DISPLAY INPUT DIALOG
    private void displayInputDialog() {
        Dialog d = new Dialog(this);
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
                    /*if (helper.save(s)) {
                        //IF SAVED CLEAR EDITXT
                        edtNomeCateg.setText("");

                        adapter = new CustomAdapter(MainActivity.this, helper.retrieve());
                        lv.setAdapter(adapter);
                    }*/
                } else {
                    Toast.makeText(MainActivity.this, "Por favor, preencha o nome da categoria", Toast.LENGTH_SHORT).show();
                }

            }
        });

        d.show();
    }

    private void saveItem(String nome){
        pg.setTitle("Salvando dados...");

        pg.show();

        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("nome", nome);

        db.collection("Categorias")
                .add(doc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        pg.dismiss();
                        Toast.makeText(MainActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pg.dismiss();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        /*db.collection("Categorias").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pg.dismiss();
                Toast.makeText(MainActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pg.dismiss();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
