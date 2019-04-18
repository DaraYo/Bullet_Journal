package com.example.bullet_journal.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;

public class SignUpActivity extends RootActivity {

    EditText _firstName, _lastName, _email, _password, _confirmPassword;
    Button _signUpButton;
    TextView _registration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _firstName = findViewById(R.id.input_first_name);
        _lastName = findViewById(R.id.input_last_name);
        _email = findViewById(R.id.input_email);
        _password = findViewById(R.id.input_password);
        _confirmPassword = findViewById(R.id.input_confirm_password);

        _signUpButton = (Button) findViewById(R.id.btn_signup);
        TextView _loginLink = (TextView) findViewById(R.id.link_login);

        _signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_PopupOverlay);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String firstName = _firstName.getText().toString();
        String lastName = _lastName.getText().toString();
        String email = _email.getText().toString();
        String password = _password.getText().toString();
        String confirmPassword = _confirmPassword.getText().toString();
    }


    public void onSignupSuccess() {
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_LONG).show();
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