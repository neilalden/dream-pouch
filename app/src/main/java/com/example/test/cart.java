package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class cart extends AppCompatActivity {
String date;
DatabaseReference mref;
public static String ultimateID,customerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        final EditText cn = findViewById(R.id.ETcustomername);
        Button btnproductlist = findViewById(R.id.product_list);
        mref = FirebaseDatabase.getInstance().getReference();
        date = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(new Date());
        TextView d = findViewById(R.id.date);
        d.setText(date);
        btnproductlist.setPaintFlags(btnproductlist.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            btnproductlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customerName = cn.getText().toString();
                        if (customerName.isEmpty()){
                            cn.setError("customer name is required");
                            cn.requestFocus();
                            return;
                        }
                        else{
                            String mrefID = mref.push().getKey();
                            String nospacename = customerName.replaceAll("\\s","_");
                            String nospacedate = date.replaceAll("\\s","_");
                            ultimateID = nospacename+"_"+nospacedate+mrefID;
                            Intent i = new Intent(cart.this, sellListView.class);
                            startActivity(i);
                        }
                    }
            });

    }
}
