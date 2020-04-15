package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class restockProductView extends AppCompatActivity {

    String strRef, strCustomerName, strName, strSpecs, strimage, dt, type, productID;
    int stck, getstock, intsales;
    TextView tvname, tvspecs, tvstock, tvdate;
    EditText amount;
    Button back,save;
    ImageView image;
    Product prd;
    DatabaseReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restock_product_view);

        restockListView rlv = new restockListView();
        strRef = rlv.Rglobalstring;

        tvname = findViewById(R.id.product_name);
        tvspecs = findViewById(R.id.product_specs);
        tvstock = findViewById(R.id.product_stocks);
        image = findViewById(R.id.imageView);
        back = findViewById(R.id.btn_back);
        save = findViewById(R.id.btn_to_cart);
        amount = findViewById(R.id.amount);
        tvdate =findViewById(R.id.date);
        dt = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(new Date());
        tvdate.setText(dt);

        Button btnback = findViewById(R.id.btn_back);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });



        mref = FirebaseDatabase.getInstance().getReference("products/"+strRef);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    prd = dataSnapshot.getValue(Product.class);
                    type = prd.getType();
                    productID = prd.getId();
                    tvname.setText(prd.getName());
                    strName = prd.getName();
                    tvspecs.setText(prd.getSpecs());
                    strSpecs = prd.getSpecs();
                    getstock = prd.getStocks();
                    tvstock.setText("Stocks left: "+getstock);
                    stck = Integer.parseInt(String.valueOf(prd.getStocks()));
                    if(prd.getImage() == null){
                        image.setImageDrawable(ContextCompat.getDrawable(restockProductView.this, R.drawable.image));
                    }
                    else {
                        Glide.with(restockProductView.this)
                                .load(prd.getImage())
                                .into(image);
                        strimage = prd.getImage();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
                amount.setText("");
            }
        });
    }
    public void update(){
        int iamount = Integer.parseInt(amount.getText().toString());
        DatabaseReference uref = FirebaseDatabase.getInstance().getReference("products/"+strRef);
        uref.child("stocks").setValue(iamount+getstock);
        Toast.makeText(restockProductView.this, String.format("%d restocked", iamount),Toast.LENGTH_SHORT).show();


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
            exit();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
    public void exit(){
        Intent i = new Intent(restockProductView.this,restockListView.class);
        startActivity(i);

    }
}
