package br.pucpr.appdev.listacomprasapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;

import br.pucpr.appdev.listacomprasapp.Model.APIResponse;
import br.pucpr.appdev.listacomprasapp.Model.Auth;
import br.pucpr.appdev.listacomprasapp.Model.Usuario;
import br.pucpr.appdev.listacomprasapp.webservices.ServiceBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private int GOOGLE_SIGN_IN = 1000;
    ProgressDialog pd;

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
        pd = new ProgressDialog(this);

        pd.setTitle("Validando..");
        pd.show();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, ListCategoryActivity.class));
        }
        pd.dismiss();
    }

    public void novoUsuarioOnClick(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void entrarOnClick(View view) {
        pd.setTitle("Entrando..");
        pd.show();

        ServiceBuilder.getUsuarioService().login(new Usuario(email.getText().toString(), senha.getText().toString())).enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {

                Auth resultado = response.body();
                SharedPreferences prefs;
                SharedPreferences.Editor edit;
                prefs = getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                edit = prefs.edit();
                String saveToken = resultado.getToken();
                String saveUUID = resultado.getUuid();
                edit.putString("token",saveToken);
                edit.putString("uuid",saveUUID);
                edit.commit();

                startActivity(new Intent(LoginActivity.this, ListCategoryActivity.class));
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                t.printStackTrace();
                pd.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void entrarGoogleOnClick(View view) {
        pd.setTitle("Entrando..");
        pd.show();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GOOGLE_SIGN_IN) {
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LoginActivity.this, ListCategoryActivity.class));
                                    pd.dismiss();
                                } else {
                                    task.getException().printStackTrace();
                                }
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
