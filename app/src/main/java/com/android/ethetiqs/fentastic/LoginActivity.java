package com.android.ethetiqs.fentastic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText usernameText;
    EditText passwordText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // get UI components by their Id
        usernameText = (EditText) findViewById(R.id.input_text_username);
        passwordText = (EditText) findViewById(R.id.input_text_password);
        loginButton = findViewById(R.id.button_login);

        // set page title
        setTitle("Login");

        /*There are two ways to define onClick method:
         1. create a onClickListener and assign the listener to the UI component in code.
         2. create a method in code and add the method name to component attribute: android:onClick= "method name"
         when both methods are used, first method has higher priority */

        // first method
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                loginIntent.putExtra("Message", "Hello World");
                startActivity(loginIntent);
            }
        });
    }

    // second method, only for example
    public void startMain(View view) {
        Toast.makeText(LoginActivity.this, "Login Button Clicked", Toast.LENGTH_SHORT).show();
    }
}
