package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    EditText ETemail, ETpassword;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        ETemail = findViewById(R.id.email);
        ETpassword = findViewById(R.id.password);
        pb = findViewById(R.id.progressBar);
        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        Button btn = findViewById(R.id.register);
        btn.setPaintFlags(btn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            event.startTracking();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
                && !event.isCanceled()) {
            System.exit(0);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
    public void loginUser() {

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
        pb.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(MainActivity.this, dashboard.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
                pb.setVisibility(View.INVISIBLE);
            }
        });
    }

        @Override
    public void onClick(View view) {
        if(view.getId() == R.id.register){
            startActivity(new Intent(this, register.class));
        }
        else if(view.getId() == R.id.login){
            loginUser();
        }
    }
}
