package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
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

public class cart extends AppCompatActivity {
    public static String ultimateID,customerName;
    String date, mrefID, currentBuyer;
    Button btnback,btnproductlist, btncheckout;
    EditText editTextCustomerName;
    TextView textViewDate;
    FirebaseListAdapter adapter;
    ListView myListView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        sellListView slv = new sellListView();
        myRef = FirebaseDatabase.getInstance().getReference();
        myListView = findViewById(R.id.cartlistview);
        database = FirebaseDatabase.getInstance();
        date = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(new Date());

        btncheckout = findViewById(R.id.btn_checkout);
        btnback = findViewById(R.id.btn_back);
        editTextCustomerName = findViewById(R.id.ETcustomername);
        btnproductlist = findViewById(R.id.product_list);
        textViewDate = findViewById(R.id.date);

        btnproductlist.setPaintFlags(btnproductlist.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        editTextCustomerName.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        textViewDate.setText(date);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
        currentBuyer = slv.crtName;
        if(currentBuyer != null){
            editTextCustomerName.setText(currentBuyer);
        }
        btnproductlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerName = editTextCustomerName.getText().toString();
                if (customerName.isEmpty()){
                    editTextCustomerName.setError("name is required");
                    editTextCustomerName.requestFocus();
                    return;
                }
                else if(!customerName.isEmpty() && adapter.getCount() != 0){
                    Intent i = new Intent(cart.this, sellListView.class);
                    startActivity(i);
                }
                if(!customerName.isEmpty() && ultimateID != ""){
                    mrefID = myRef.push().getKey();
                    String nospacename = customerName.replaceAll("\\s","-");
                    String nospacedate = date.replaceAll("\\s","-");
                    ultimateID = nospacename+""+nospacedate+mrefID;
                    Intent i = new Intent(cart.this, sellListView.class);
                    startActivity(i);
                }
            }
        });

        myRef = database.getReference("customerSales/"+ultimateID);
        mStorageRef = FirebaseStorage.getInstance().getReference("products");
        Query query = FirebaseDatabase.getInstance().getReference().child("customerSales/"+ultimateID);
        FirebaseListOptions<CustomerSale> options = new FirebaseListOptions.Builder<CustomerSale>()
                .setLayout(R.layout.cart_list)
                .setQuery(query, CustomerSale.class)
                .build();
        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView productName = v.findViewById(R.id.productName);
                TextView productSpecs = v.findViewById(R.id.productSpecs);
                ImageView img = v.findViewById(R.id.product_image);
                ElegantNumberButton num = v.findViewById(R.id.amount);
                final CustomerSale cs = (CustomerSale) model;
                productName.setText(cs.getProductname());
                productSpecs.setText(cs.getProductspecs());
                num.setNumber(String.valueOf(cs.getAmount()));
                if(cs.getImage() == null){
                    img.setImageDrawable(ContextCompat.getDrawable(cart.this, R.drawable.image));
                }
                else {
                    Glide.with(cart.this)
                            .load(cs.getImage())
                            .into(img);
                }
                Button del = v.findViewById(R.id.button2);
                del.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        myRef.child(cs.getId()).removeValue();
                    }
                });

            }
        };
        myListView.setAdapter(adapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // do nothing... yet
            }
        });
        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getCount() == 0){
                    Toast.makeText(cart.this, "cart is empty.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(cart.this, "check out successful!", Toast.LENGTH_SHORT).show();
                    editTextCustomerName.setText("");
                    myListView.setAdapter(null);
                }
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
        Intent i = new Intent(cart.this,dashboard.class);
        startActivity(i);

    }
}
