package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class restockListView extends AppCompatActivity {

    FirebaseListAdapter adapter;
    ListView myListView;
    FirebaseDatabase database;
    StorageReference mStorageRef;
    DatabaseReference myRef;
    public static String Rglobalstring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restock_list_view);
        TextView date = findViewById(R.id.date);
        String sdate = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(new Date());
        date.setText(sdate);
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

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Rglobalstring = ((TextView)view.findViewById(R.id.productId)).getText().toString();
                Intent intent = new Intent(restockListView.this, restockProductView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(restockListView.this, dashboard.class);
                startActivity(i);
            }
        });
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
        Intent i = new Intent(restockListView.this,dashboard.class);
        startActivity(i);

    }
}
