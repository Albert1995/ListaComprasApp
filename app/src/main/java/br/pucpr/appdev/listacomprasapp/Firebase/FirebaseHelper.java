package br.pucpr.appdev.listacomprasapp.Firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import br.pucpr.appdev.listacomprasapp.Model.Categoria;
/*
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
*/
public class FirebaseHelper {

    FirebaseFirestore db;
    Boolean saved;
    ArrayList<Categoria> categorias = new ArrayList<>();


 //PASS DATABASE REFRENCE

    public FirebaseHelper(FirebaseFirestore db) {
        this.db = db;
        retrieve();
    }

    public ArrayList<Categoria> retrieve()
    {
        db.collection("Categorias")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc: task.getResult()){
                            Categoria categ = new Categoria();
                            categ.setNome(doc.getString("nome"));
                            categorias.add(categ);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return categorias;
    }
    /*
    //WRITE IF NOT NULL
    public Boolean save(Categoria categoria)
    {
        if(categoria == null)
        {
            saved = false;
        }else
        {
            try
            {
                db.child("Categoria").push().setValue(categoria);
                saved=true;

            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }

        return saved;
    }

    //IMPLEMENT FETCH DATA AND FILL ARRAYLIST
    private void fetchData(DataSnapshot dataSnapshot)
    {
        categorias.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Categoria categoria = ds.getValue(Categoria.class);
            categorias.add(categoria);
        }
    }

    //RETRIEVE
    public ArrayList<Categoria> retrieve()
    {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return categorias;
    }
    */
}
