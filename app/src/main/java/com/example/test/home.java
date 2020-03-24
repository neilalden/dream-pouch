package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import java.net.InetAddress;
import java.net.URI;

public class home extends AppCompatActivity {
    FirebaseListAdapter adapter;
    ListView myListView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Product product;
    StorageReference mStorageRef;
    public static String globalstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        isInternetAvailable();
        product = new Product();
        myListView = findViewById(R.id.listview);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("products");
        mStorageRef = FirebaseStorage.getInstance().getReference("products");
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
                ImageView img = v.findViewById(R.id.product_image);
                Product prd = (Product)model;
                productName.setText(prd.getName());
                productSpecs.setText(prd.getSpecs());
                productStock.setText("Stocks remaining: "+prd.getStocks());
                if(prd.getImage() == null){
                    img.setImageDrawable(ContextCompat.getDrawable(home.this, R.drawable.image));
                }
                else {
                    Glide.with(home.this)
                            .load(prd.getImage())
                            .into(img);
                }
            }
        };
        myListView.setAdapter(adapter);



        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(home.this, sellProductView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                globalstring = ((TextView)view.findViewById(R.id.productSpecs)).getText().toString();
            }
        });
        Button login = findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(home.this,addproduct.class);
                startActivity(i);
            }
        });
    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
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
