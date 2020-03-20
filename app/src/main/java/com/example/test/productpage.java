package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class productpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productpage);
        home home = new home();
        String samplestring = home.globalstring;
        TextView tv = (TextView)findViewById(R.id.tvsample);
        tv.setText(samplestring);
    }
}
