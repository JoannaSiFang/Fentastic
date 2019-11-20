package com.android.ethetiqs.fentastic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText usernameText;
    EditText passwordText;
    Button loginButton;
    Button registerButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // get UI components by their Id
        usernameText = (EditText) findViewById(R.id.input_text_username);
        passwordText = (EditText) findViewById(R.id.input_text_password);
        //loginButton = findViewById(R.id.button_login);

        mAuth = FirebaseAuth.getInstance();

        // set page title
        setTitle("Login");
        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.button_reg).setOnClickListener(this);

        /*There are two ways to define onClick method:
         1. create a onClickListener and assign the listener to the UI component in code.
         2. create a method in code and add the method name to component attribute: android:onClick= "method name"
         when both methods are used, first method has higher priority */

        // first method
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                userLogin();
//                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
//                loginIntent.putExtra("Message", "Hello World");
//                startActivity(loginIntent);
//            }
//        });
//
//        registerButton = findViewById(R.id.button_reg);
//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intentRegister);
//
//            }
//        });
    }

    protected void userLogin() {
        String email = usernameText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (email.isEmpty()) {
            usernameText.setError("Email is required");
            usernameText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameText.setError("Please enter a valid email");
            usernameText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordText.setError("Password is required");
            passwordText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordText.setError("Minimum length of a password should be 6");
            passwordText.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(loginIntent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // second method, only for example
    public void startMain(View view) {
        Toast.makeText(LoginActivity.this, "Login Button Clicked", Toast.LENGTH_SHORT).show();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                userLogin();
                break;

            case R.id.button_reg:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }
}
