package com.android.ethetiqs.fentastic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    Button submitButton;
    EditText editTextEmail, editTextPassword, editTextWeeks;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail = (EditText) findViewById(R.id.email_text_input_edittext);
        editTextPassword = (EditText) findViewById(R.id.password_edit_text);
        editTextWeeks = (EditText) findViewById(R.id.pregnancy_edit_text);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_sub).setOnClickListener(this);
        findViewById(R.id.button_login_fromreg).setOnClickListener(this);

//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                registerUser();
//                Intent intentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(intentLogin);
//
//            }
//        });
    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String week = editTextWeeks.getText().toString();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (week.isEmpty()) {
            editTextWeeks.setError("Weeks into pregnancy is required");
            editTextWeeks.requestFocus();
            return;
        }



        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of a password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String user_id = mAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_bd = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                    Map newPost = new HashMap();
                    newPost.put("email", email);
                    newPost.put("weeks", week);
                    current_user_bd.setValue(newPost);

                    Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sub:
                registerUser();
                break;
            case R.id.button_login_fromreg:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }


}
