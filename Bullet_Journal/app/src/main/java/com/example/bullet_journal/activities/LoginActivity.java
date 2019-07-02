package com.example.bullet_journal.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bullet_journal.MainActivity;
import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.recivers.NetworkBroadcastReciver;
import com.example.bullet_journal.synchronization.PullFromFirestoreAsyncTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends RootActivity {

    private final Context context = this;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private NetworkBroadcastReciver networkBroadcastReciver;

    EditText _email, _password;
    Button _loginButton;
    TextView _signUpLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(firebaseAuth.getCurrentUser() != null){
            Toast.makeText(getBaseContext(), R.string.logged_in_alredy, Toast.LENGTH_LONG).show();
            finish();
        }

        networkBroadcastReciver = new NetworkBroadcastReciver();

        _email = (EditText) findViewById(R.id.login_email);
        _password = (EditText) findViewById(R.id.login_password);

        _loginButton = (Button) findViewById(R.id.login_button);
        TextView _signUpLink = (TextView) findViewById(R.id.link_signup);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 login();
            }
        });

        _signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkBroadcastReciver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkBroadcastReciver);
    }

    public void login() {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_AppBarOverlay);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.login_progress_dialog));

        String email = _email.getText().toString();
        String password = _password.getText().toString();

        if (TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)) {
            Toast.makeText(getBaseContext(), R.string.fields_empty, Toast.LENGTH_LONG).show();
            return;
        }

        if(networkBroadcastReciver.isDataOn() || networkBroadcastReciver.isWifiOn()){
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(getBaseContext(), R.string.basic_error, Toast.LENGTH_LONG).show();
                        return;
                    }

                    AsyncTask<Void, Void, Boolean> pullFromFirestoreAsyncTask = new PullFromFirestoreAsyncTask(getApplicationContext(), new AsyncResponse<Boolean>(){
                        @Override
                        public void taskFinished(Boolean retVal) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            if(retVal){
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getBaseContext(), R.string.basic_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).execute();
                }
            });
        }else{
            Toast.makeText(getBaseContext(), R.string.network_required, Toast.LENGTH_SHORT).show();
        }
    }

}
