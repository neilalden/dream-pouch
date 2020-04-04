package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
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
    int stck, btnamnt, intsales;
    TextView tvname, tvspecs, tvstock, tvdate;
    ElegantNumberButton amount;
    Button back,cart;
    ImageView image;
    Product prd;
    DatabaseReference mref, CSref, Sref, Getref;
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
                    tvstock.setText("Stocks left: "+prd.getStocks());
                    stck = Integer.parseInt(String.valueOf(prd.getStocks()));
                    if(prd.getImage() == null){
                        image.setImageDrawable(ContextCompat.getDrawable(sellProductView.this, R.drawable.image));
                    }
                    else {
                        Glide.with(sellProductView.this)
                                .load(prd.getImage())
                                .into(image);
                        strimage = prd.getImage();
                    }
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
                btnamnt = Integer.parseInt( amount.getNumber());
                try{
                    if(btnamnt==0){
                        Toast.makeText(sellProductView.this, "no amount entered",Toast.LENGTH_SHORT).show();
                    }
                    else{
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
            update(type+productID,strName+" "+strSpecs,btnamnt);
            Toast.makeText(sellProductView.this,"added to cart!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(sellProductView.this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void update(String updateID, String updateName, int updateSold){
        DatabaseReference updateref = FirebaseDatabase.getInstance().getReference("sales").child(updateID);
        Sales sls = new Sales(updateID, updateName, updateSold);
        updateref.setValue(sls);

    }
}
