package br.pucpr.appdev.listacomprasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private EditText email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Entrar");
        firebaseAuth = FirebaseAuth.getInstance();
        setUp();
    }

    private void setUp() {
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
    }

    public void novoUsuarioOnClick(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void entrarOnClick(View view) {
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), senha.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult.getUser() != null) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    }
                });
    }
}
