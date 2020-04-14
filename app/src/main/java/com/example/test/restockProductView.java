package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
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

        tvdate =findViewById(R.id.date);
        tvname = findViewById(R.id.product_name);
        tvspecs = findViewById(R.id.product_specs);
        tvstock = findViewById(R.id.product_stocks);
        image = findViewById(R.id.imageView);
        back = findViewById(R.id.btn_back);
        save = findViewById(R.id.btn_to_cart);
        amount = findViewById(R.id.amount);
        dt = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(new Date());
        tvdate.setText(dt);



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
                    tvstock.setText("Stocks left: "+prd.getStocks());
                    getstock = prd.getStocks();
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
            }
        });
    }
    public void update(){
        int iamount = Integer.parseInt(amount.getText().toString());
        DatabaseReference uref = FirebaseDatabase.getInstance().getReference("products/"+strRef);
        uref.child("stocks").setValue(iamount);
        Toast.makeText(restockProductView.this, String.format("%d restocked", iamount),Toast.LENGTH_SHORT).show();
        amount.setText("");
        

    }
}
