package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class addproduct extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnsave,chooseimage;
    private TextView ETproducttype, ETproductname, ETspecs, ETprice;
    private ImageView mImageview;
    private ProgressBar pb;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mdatabase, sref;
    String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);
        chooseimage = findViewById(R.id.btn_choose_img);
        btnsave = findViewById(R.id.btn_enter);
        pb = findViewById(R.id.progressBar);
        mImageview = findViewById(R.id.product_image);
        mStorageRef = FirebaseStorage.getInstance().getReference("products");
        mdatabase = FirebaseDatabase.getInstance().getReference("products");
        Button btnback = findViewById(R.id.btn_back);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });

        TextView date = findViewById(R.id.date);
        String sdate = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(new Date());
        date.setText(sdate);
        chooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openfile();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    private void openfile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            mImageUri = data.getData();
            Picasso.with(addproduct.this).load(mImageUri).into(mImageview);

    }


    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void save(){
        if(mImageUri != null){
            final StorageReference fileref = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
            fileref.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pb.setProgress(0);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pb.setProgress(0);
                        }
                    }, 500);

                    ETproducttype = findViewById(R.id.product_type);
                    ETproductname = findViewById(R.id.product_name);
                    ETspecs = findViewById(R.id.specification);
                    ETprice = findViewById(R.id.price);
                    final String type =  ETproducttype.getText().toString();
                    final String name =  ETproductname.getText().toString();
                    String specs =  ETspecs.getText().toString();
                    String priceContainer =  ETprice.getText().toString();
                    final String id;
                    float price =0;
                    final int stocks = 0;
                    if(type.isEmpty()){
                        ETproducttype.setError("product type is required");
                        ETproducttype.requestFocus();
                        return;
                    }
                    if(name.isEmpty()){
                        ETproductname.setError("product name is required");
                        ETproductname.requestFocus();
                        return;
                    }
                    if(specs.isEmpty()){
                        specs = "";
                    }
                    if(priceContainer.isEmpty()){
                        ETprice.setError("product price is required");
                        ETprice.requestFocus();
                        return;
                    }else{
                        try{
                            price = Float.valueOf(ETprice.getText().toString());
                        }catch (Exception e){
                            Toast.makeText(addproduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    id = mdatabase.push().getKey();
                    final String finalSpecs = specs;
                    final float finalPrice = price;
                    fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            downloadUrl = uri.toString();
                            Product upload = new Product(id,type,name, finalSpecs, finalPrice,stocks, downloadUrl);
                            sref = FirebaseDatabase.getInstance().getReference("sales");
                            Sales sales = new Sales(type+id,finalSpecs,0);
                            sref.child(type+id).setValue(sales);
                            mdatabase.child(id).setValue(upload);
                        }
                    });
                    Toast.makeText(addproduct.this, "saved!", Toast.LENGTH_SHORT).show();
                    ETproducttype.setText("");
                    ETproductname.setText("");
                    ETspecs.setText("");
                    ETprice.setText("");
                    mImageview.setImageDrawable(ContextCompat.getDrawable(addproduct.this, R.drawable.image));
                    pb.setVisibility(View.INVISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(addproduct.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
        else{
            Toast.makeText(this,"No image selected", Toast.LENGTH_SHORT).show();
        }
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
        Intent i = new Intent(addproduct.this,dashboard.class);
        startActivity(i);

    }
}
