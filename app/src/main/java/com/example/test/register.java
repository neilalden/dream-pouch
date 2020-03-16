package com.example.test;

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
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class register extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    EditText ETemail, ETpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        ETemail = findViewById(R.id.email);
        ETpassword = findViewById(R.id.password);
        findViewById(R.id.register).setOnClickListener(this);
    }
    public void registerUser(){

        String email = ETemail.getText().toString().trim();
        String password = ETpassword.getText().toString().trim();
        if(email.isEmpty()){
            ETemail.setError("email is required");
            ETemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ETemail.setError("please enter a valid email");
            ETemail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            ETpassword.setError("password is required");
            ETpassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            ETpassword.setError("password must be 6 characters long");
            ETpassword.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(register.this, "User registered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                registerUser();
                break;
        }
    }
}
