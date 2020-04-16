package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class sellProductView extends AppCompatActivity {
    String strRef, strCustomerName, strName, strSpecs, strimage, dt, type, productID;
    String customerID;
    int btnamnt, intsales, intstocks;
    TextView tvname, tvspecs, tvstock, tvdate;
    EditText amount;
    Button back,cart;
    ImageView image;
    Product prd;
    DatabaseReference mref, CSref,updateref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product_view);

        cart crt = new cart();
        sellListView slv = new sellListView();
        strRef = slv.globalstring;
        strCustomerName = crt.customerName;
        customerID = crt.ultimateID;
        tvdate =findViewById(R.id.date);
        tvname = findViewById(R.id.product_name);
        tvspecs = findViewById(R.id.product_specs);
        tvstock = findViewById(R.id.product_stocks);
        image = findViewById(R.id.imageView);
        back = findViewById(R.id.btn_back);
        cart = findViewById(R.id.btn_to_cart);
        amount = findViewById(R.id.amount);
        dt = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(new Date());
        tvdate.setText(dt);

        mref = FirebaseDatabase.getInstance().getReference("products/"+strRef);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    prd = dataSnapshot.getValue(Product.class);
                    type = prd.getType();
                    productID = prd.getId();
                    tvname.setText(prd.getName());
                    strName = prd.getName();
                    tvspecs.setText(prd.getSpecs());
                    strSpecs = prd.getSpecs();
                    intstocks = prd.getStocks();
                    tvstock.setText("Stocks left: "+intstocks);
                    if(prd.getImage() == null){
                        image.setImageDrawable(ContextCompat.getDrawable(sellProductView.this, R.drawable.image));
                    }
                    else {
                        Glide.with(sellProductView.this)
                                .load(prd.getImage())
                                .into(image);
                        strimage = prd.getImage();
                    }
                    updateref = FirebaseDatabase.getInstance().getReference("sales").child(type+productID);
                    updateref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Sales sales = dataSnapshot.getValue(Sales.class);
                            intsales = sales.getSold();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }catch (Exception e){
                    // Toast.makeText(sellProductView.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(sellProductView.this, sellListView.class);
                startActivity(i);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    btnamnt = Integer.parseInt(String.valueOf(amount.getText()));
                    if(btnamnt==0){
                        Toast.makeText(sellProductView.this, "no amount entered",Toast.LENGTH_SHORT).show();
                    }
                    else if(btnamnt > intstocks){
                        Toast.makeText(sellProductView.this, "not enough stocks for those",Toast.LENGTH_LONG).show();
                    }
                    else{
                        amount.setText("");
                        addtocart();
                    }
                }catch (Exception e){
                    Toast.makeText(sellProductView.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void addtocart(){
        try {
            CSref = FirebaseDatabase.getInstance().getReference("customerSales").child(customerID);
            String id = CSref.push().getKey();
            CustomerSale cs = new CustomerSale(id,strCustomerName,customerID,strName, strSpecs, strimage,btnamnt,dt);
            CSref.child(id).setValue(cs);

            DatabaseReference stockupdate = FirebaseDatabase.getInstance().getReference("products/"+productID);
            stockupdate.child("stocks").setValue(intstocks-btnamnt);

            update(type+productID,strSpecs,intsales+btnamnt);
        }
        catch (Exception e){
            Toast.makeText(sellProductView.this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void update(String updateID, String updateName, int updateSold){
        Sales sls = new Sales(updateID, updateName, updateSold);
        updateref.setValue(sls);
        Toast.makeText(sellProductView.this,"added to cart!", Toast.LENGTH_SHORT).show();

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
        Intent i = new Intent(sellProductView.this,sellListView.class);
        startActivity(i);

    }
}
