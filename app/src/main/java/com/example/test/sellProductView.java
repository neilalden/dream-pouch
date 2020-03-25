package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class sellProductView extends AppCompatActivity {
    String strRef;
    TextView tvname, tvspecs, tvstock;
    ImageView image;
    Product prd;
    DatabaseReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product_view);

        sellListView slv = new sellListView();
        strRef = slv.globalstring;
        tvname = findViewById(R.id.product_name);
        tvspecs = findViewById(R.id.product_specs);
        tvstock = findViewById(R.id.product_stocks);
        image = findViewById(R.id.imageView);
        mref = FirebaseDatabase.getInstance().getReference("products/"+strRef);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    prd = dataSnapshot.getValue(Product.class);
                    tvname.setText(prd.getName());
                    tvspecs.setText(prd.getSpecs());
                    tvstock.setText("Stocks left: "+prd.getStocks());
                    if(prd.getImage() == null){
                        image.setImageDrawable(ContextCompat.getDrawable(sellProductView.this, R.drawable.image));
                    }
                    else {
                        Glide.with(sellProductView.this)
                                .load(prd.getImage())
                                .into(image);
                    }
                }catch (Exception e){
                    Toast.makeText(sellProductView.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
