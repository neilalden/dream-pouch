package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class cart extends AppCompatActivity {
    String date;
    Button back,btnproductlist;
    EditText cn;
    FirebaseListAdapter adapter;
    ListView myListView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorageRef;
public static String ultimateID,customerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        back = findViewById(R.id.btn_back);
        cn = findViewById(R.id.ETcustomername);
        btnproductlist = findViewById(R.id.product_list);
        myRef = FirebaseDatabase.getInstance().getReference();
        date = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(new Date());
        TextView d = findViewById(R.id.date);
        d.setText(date);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
        btnproductlist.setPaintFlags(btnproductlist.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        sellListView slv = new sellListView();
        String nem = slv.crtName;
        if(nem != null){
            cn.setText(nem);
        }
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
                            String mrefID = myRef.push().getKey();
                            String nospacename = customerName.replaceAll("\\s","-");
                            String nospacedate = date.replaceAll("\\s","-");
                            ultimateID = nospacename+""+nospacedate+mrefID;
                            Intent i = new Intent(cart.this, sellListView.class);
                            startActivity(i);
                        }
                    }
            });

                myListView = findViewById(R.id.cartlistview);
                database = FirebaseDatabase.getInstance();
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
                        ImageView img = v.findViewById(R.id.productImage);
                        ElegantNumberButton num = v.findViewById(R.id.amount);
                        CustomerSale cs = (CustomerSale) model;
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


                    }
                };
                myListView.setAdapter(adapter);

                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // do nothing... yet
                    }
                });

                findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(cart.this, dashboard.class);
                        i.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        myRef.child("customerSales/"+ultimateID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){

                }
                else{
                    adapter.startListening();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
