package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class cart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Button btnproductlist = findViewById(R.id.product_list);
        btnproductlist.setPaintFlags(btnproductlist.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnproductlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cart.this, products_sell_list_view.class);
                startActivity(intent);
            }
        });
    }
}
