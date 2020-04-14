package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class dashboard extends AppCompatActivity {
    Button btnsell, btnrestock, btnstats, btnlogs, btnregister, btnsignout, btndelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        btnregister = findViewById(R.id.btn_register);
        btnsell = findViewById(R.id.btn_sell);
        btnstats = findViewById(R.id.btn_stats);
        btndelete = findViewById(R.id.btn_delete);
        btndelete.setPaintFlags(btndelete.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnstats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this, statistics.class);
                startActivity(i);
            }
        });
        btnsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this,cart.class);
                startActivity(i);
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this,addproduct.class);
                startActivity(i);
            }
        });
        btnsignout = findViewById(R.id.btn_signout);
        btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this,MainActivity.class);
                startActivity(i);
            }
        });
        btnrestock = findViewById(R.id.btn_restock);
        btnrestock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this, restockListView.class);
                startActivity(i);
            }
        });
        btnlogs = findViewById(R.id.btn_logs);
        btnlogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this,logs.class);
                startActivity(i);
            }
        });
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
}
