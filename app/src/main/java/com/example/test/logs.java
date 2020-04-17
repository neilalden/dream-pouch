package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class logs extends AppCompatActivity {

    ListView myListView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        TextView date = findViewById(R.id.date);
        String sdate = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(new Date());
        date.setText(sdate);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(logs.this, dashboard.class);
                startActivity(i);
            }
        });

        myListView = findViewById(R.id.logslistview);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("logs");
        Query query = FirebaseDatabase.getInstance().getReference().child("logs");
        final FirebaseListOptions<Log> options = new FirebaseListOptions.Builder<Log>()
                .setLayout(R.layout.logs_list)
                .setQuery(query, Log.class)
                .build();
        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView loguser = v.findViewById(R.id.TVusername);
                TextView logact = v.findViewById(R.id.TVaction);
                TextView logdate = v.findViewById(R.id.TVdate);
                //TextView id = v.findViewById(R.id.logid);
                //id.setText(prd.getId());

                Log log = (Log)model;
                String slogact = log.getLogAction();
                StringBuffer finalString = new StringBuffer();
                String[] stringArray = slogact.split("\\s+");
                String tmpString = "";
                for (String singleWord : stringArray) {
                    if ((tmpString + singleWord + " ").length() > 35) {
                        finalString.append(tmpString + "\n");
                        tmpString = singleWord + " ";
                    } else {
                        tmpString = tmpString + singleWord + " ";
                    }
                }

                if (tmpString.length() > 0) {
                    finalString.append(tmpString);
                }
                logact.setText(finalString);
                loguser.setText(log.getLogUsername());
                logdate.setText(log.getLogDate());


            }
        };
        myListView.setAdapter(adapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              // nothing yet
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
        Intent i = new Intent(logs.this,dashboard.class);
        startActivity(i);

    }
}
