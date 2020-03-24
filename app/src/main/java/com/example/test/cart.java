package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;

public class cart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Button btnproductlist = findViewById(R.id.product_list);
        btnproductlist.setPaintFlags(btnproductlist.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}
