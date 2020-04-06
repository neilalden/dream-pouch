package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class restockListView extends AppCompatActivity {

    FirebaseListAdapter adapter;
    ListView myListView;
    FirebaseDatabase database;
    StorageReference mStorageRef;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restock_list_view);

        myListView = findViewById(R.id.cartlistview);
        database = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("products");
        myRef = database.getReference("products");
        Query query = FirebaseDatabase.getInstance().getReference().child("products");
        final FirebaseListOptions<Product> options = new FirebaseListOptions.Builder<Product>()
                .setLayout(R.layout.product_info)
                .setQuery(query, Product.class)
                .build();
        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView productName = v.findViewById(R.id.productName);
                TextView productSpecs = v.findViewById(R.id.productSpecs);
                TextView productStock = v.findViewById(R.id.productStock);
                TextView id = v.findViewById(R.id.productId);
                ImageView img = v.findViewById(R.id.product_image);
                Product prd = (Product)model;
                productName.setText(prd.getName());
                productSpecs.setText(prd.getSpecs());
                id.setText(prd.getId());
                productStock.setText("Stocks remaining: "+prd.getStocks());
                if(prd.getImage() == null){
                    img.setImageDrawable(ContextCompat.getDrawable(restockListView.this, R.drawable.image));
                }
                else {
                    Glide.with(restockListView.this)
                            .load(prd.getImage())
                            .into(img);
                }


            }
        };
        myListView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
