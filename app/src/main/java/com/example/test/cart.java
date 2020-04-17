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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class cart extends AppCompatActivity {
    public static String ultimateID,customerName;
    String date, mrefID, currentBuyer, slvID, prodid, prodtype;
    int prodamnt, currentamount, intsales;
    Button btnback,btnproductlist, btncheckout;
    EditText editTextCustomerName;
    TextView textViewDate;
    FirebaseListAdapter adapter;
    ListView myListView;
    HashMap<String, Integer> Hlogs = new HashMap<String, Integer>();

    FirebaseUser fbuser;
    DatabaseReference myRef;
    StorageReference mStorageRef;
    sellListView slv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        slv = new sellListView();
        myRef = FirebaseDatabase.getInstance().getReference();
        myListView = findViewById(R.id.cartlistview);
        date = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(new Date());

        btncheckout = findViewById(R.id.btn_checkout);
        btnback = findViewById(R.id.btn_back);
        editTextCustomerName = findViewById(R.id.ETcustomername);
        btnproductlist = findViewById(R.id.product_list);
        textViewDate = findViewById(R.id.date);

        editTextCustomerName.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        btnproductlist.setPaintFlags(btnproductlist.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewDate.setText(date);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
        // current buyer is for checking if someone already entered SLV
        //and if there is, then there's already a firebase id, ill call it slvID
        // always consider customerName
        currentBuyer = slv.crtName;
        slvID = slv.slvUltimateID;
        if(currentBuyer != null){
            editTextCustomerName.setText(currentBuyer);
            ultimateID = slv.slvUltimateID;
            customerName = currentBuyer;
        }
        btnproductlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    customerName = editTextCustomerName.getText().toString();
                    if (customerName.isEmpty()){
                        editTextCustomerName.setError("name is required");
                        editTextCustomerName.requestFocus();
                        return;
                    }
                    else if(!customerName.isEmpty() && adapter.getCount() != 0 && !ultimateID.isEmpty()){
                        Intent i = new Intent(cart.this, sellListView.class);
                        startActivity(i);
                    }
                    else if(!customerName.isEmpty() && ultimateID != ""){
                        mrefID = myRef.push().getKey();
                        String nospacename = customerName.replaceAll("\\s","-");
                        String nospacedate = date.replaceAll("\\s","-");
                        ultimateID = nospacename+"-"+nospacedate+mrefID;
                        Intent i = new Intent(cart.this, sellListView.class);
                        startActivity(i);
                    }
                }catch (Exception e){
                    Toast.makeText(cart.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("customerSales/"+ultimateID);
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
                TextView num = v.findViewById(R.id.amount);
                final CustomerSale cs = (CustomerSale) model;
                prodamnt = cs.getAmount();
                prodtype = cs.getProductname();
                prodid =cs.getProductid();
                Hlogs.put(cs.getProductspecs(),prodamnt);
                productName.setText(prodtype);
                productSpecs.setText(cs.getProductspecs());
                num.setText(String.valueOf(prodamnt));
                if(cs.getImage() == null){
                    img.setImageDrawable(ContextCompat.getDrawable(cart.this, R.drawable.image));
                }
                else {
                    Glide.with(cart.this)
                            .load(cs.getImage())
                            .into(img);
                }
                DatabaseReference prodref = FirebaseDatabase.getInstance().getReference("products").child(prodid);
                prodref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Product product = dataSnapshot.getValue(Product.class);
                        currentamount = product.getStocks();
                        DatabaseReference updateref = FirebaseDatabase.getInstance().getReference("sales").child(prodtype+prodid);
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Button del = v.findViewById(R.id.button2);
                del.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        myRef.child(cs.getId()).removeValue();
                        DatabaseReference sref = FirebaseDatabase.getInstance().getReference("sales").child(prodtype+prodid);
                        DatabaseReference pref = FirebaseDatabase.getInstance().getReference("products").child(prodid);
                        pref.child("stocks").setValue(prodamnt+currentamount);
                        sref.child("sold").setValue(intsales-prodamnt);
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

                    DatabaseReference logref = FirebaseDatabase.getInstance().getReference("logs");
                    String user = fbuser.getEmail();
                    String x = "";

                    for (String i : Hlogs.keySet()) {
                        x += i+" " + Hlogs.get(i)+" ";
                    }
                    Log lg = new Log(ultimateID,user,date,x+ "sold to "+customerName);
                    logref.child(ultimateID).setValue(lg);
                    Toast.makeText(cart.this, "check out successful!", Toast.LENGTH_SHORT).show();
                    editTextCustomerName.setText("");
                    ultimateID = "";
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
        ultimateID ="";
        editTextCustomerName.setText("");
    }
}
