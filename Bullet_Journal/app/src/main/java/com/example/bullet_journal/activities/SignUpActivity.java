package com.example.bullet_journal.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.helpClasses.FirebaseUserDTO;
import com.example.bullet_journal.recivers.NetworkBroadcastReciver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends RootActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private NetworkBroadcastReciver networkBroadcastReciver;

    EditText _firstName, _lastName, _email, _password, _confirmPassword;
    Button _signUpButton;
    TextView _registration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(firebaseAuth.getCurrentUser() != null){
            Toast.makeText(getBaseContext(), "You are Logged In", Toast.LENGTH_LONG).show();
            finish();
        }

        _firstName = findViewById(R.id.input_first_name);
        _lastName = findViewById(R.id.input_last_name);
        _email = findViewById(R.id.input_email);
        _password = findViewById(R.id.input_password);
        _confirmPassword = findViewById(R.id.input_confirm_password);

        _signUpButton = (Button) findViewById(R.id.btn_signup);
        TextView _loginLink = (TextView) findViewById(R.id.link_login);

        networkBroadcastReciver = new NetworkBroadcastReciver();

        _signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void signup() {
        if (!validate()) {
            Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_PopupOverlay);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");

        final String firstName = _firstName.getText().toString();
        final String lastName = _lastName.getText().toString();
        final String email = _email.getText().toString();
        final String password = _password.getText().toString();
        String confirmPassword = _confirmPassword.getText().toString();

        if(networkBroadcastReciver.isWifiOn() || networkBroadcastReciver.isDataOn()){
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUserDTO newUser = new FirebaseUserDTO(firebaseAuth.getUid(), firstName, lastName, email, password);
                        final CollectionReference colRef = FirebaseFirestore.getInstance().collection("Users");

                        colRef.document(firebaseAuth.getCurrentUser().getUid()).set(newUser).addOnSuccessListener(
                                new OnSuccessListener< Void >() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                        ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getBaseContext(), R.string.basic_error, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else{
                        Toast.makeText(getBaseContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            Toast.makeText(getBaseContext(), "Turn Wifi or Data to proceed", Toast.LENGTH_LONG).show();
        }
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

    public boolean validate() {
        boolean valid = true;

        String firstName = _firstName.getText().toString();
        String lastName = _lastName.getText().toString();
        String email = _email.getText().toString();
        String password = _password.getText().toString();
        String confirmPassword = _confirmPassword.getText().toString();

        if (firstName.isEmpty()) {
            _firstName.setError("Please enter your first name.");
            valid = false;
        } else {
            _firstName.setError(null);
        }

        if (lastName.isEmpty()) {
            _lastName.setError("Please enter your last name.");
            valid = false;
        } else {
            _lastName.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _email.setError("Enter a valid email address");
            valid = false;
        } else {
            _email.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 50) {
            _password.setError("Password must be between 6 and 50 characters");
            valid = false;
        } else {
            _password.setError(null);
        }

        if (!password.equals(confirmPassword)) {
            _confirmPassword.setError("Password and confirmation password don't match");
            valid = false;
        } else {
            _confirmPassword.setError(null);
        }

        return valid;
    }
}