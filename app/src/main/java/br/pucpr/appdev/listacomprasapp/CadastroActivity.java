package br.pucpr.appdev.listacomprasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;

public class CadastroActivity extends AppCompatActivity {

    private EditText email, senha, confirmaSenha;
    private TextView erroEmail, erroSenha;
    private String mensagemErroValidacao;
    private String regexEmail = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        setTitle("Cadastro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        setUp();
    }

    private void setUp() {
        email = findViewById(R.id.cadastro_email);
        senha = findViewById(R.id.cadastro_senha);
        confirmaSenha = findViewById(R.id.cadastro_confirma_senha);

        erroEmail = findViewById(R.id.erro_email);
        erroSenha = findViewById(R.id.erro_senha);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }

        return true;
    }

    public boolean validateForm() {
        erroEmail.setVisibility(View.GONE);
        erroSenha.setVisibility(View.GONE);

        if (email.getText().length() == 0) {
            erroEmail.setVisibility(View.VISIBLE);
            erroEmail.setText("Preencha o campo Email");
            return false;
        }

        if (senha.getText().length() == 0) {
            erroSenha.setVisibility(View.VISIBLE);
            erroSenha.setText("Preencha o campo Senha");
            return false;
        }

        if (senha.getText().length() < 6) {
            erroSenha.setVisibility(View.VISIBLE);
            erroSenha.setText("Senha deve conter no mínimo 6 caractéres.");
            return false;
        }

        if (confirmaSenha.getText().length() == 0) {
            erroSenha.setVisibility(View.VISIBLE);
            erroSenha.setText("Preencha o campo Confirma Senha");
            return false;
        }

        if (!email.getText().toString().matches(regexEmail)) {
            erroEmail.setVisibility(View.VISIBLE);
            erroEmail.setText("Este e-mail não é valido.");
            return false;
        }

        if (!senha.getText().toString().equals(confirmaSenha.getText().toString())) {
            erroSenha.setVisibility(View.VISIBLE);
            erroSenha.setText("Senha e Confirmação de Senha não conferem.");
            return false;
        }

        return true;

    }

    public void cadastrarOnClick(View view) {
        if (validateForm()) {
            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), senha.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(CadastroActivity.this, "Você foi cadastrado com sucesso.", Toast.LENGTH_LONG).show();
                            CadastroActivity.this.finish();
                        }
                    });
        }
    }
}
