package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class statistics extends AppCompatActivity {
    DatabaseReference salesRef;
    TextView text;
    GraphView graphs;
    LineGraphSeries bars;
    int index, size;
    List<String> name = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        text = findViewById(R.id.sometext);
        salesRef = FirebaseDatabase.getInstance().getReference("sales");
        graphs = findViewById(R.id.graphview);
        bars = new LineGraphSeries<>();
        bars.setDrawDataPoints(true);
        graphs.addSeries(bars);
        graphs.getViewport().setMinY(1.0);
        graphs.getViewport().setMinX(1.0);

    }

    @Override
    protected void onStart() {
        super.onStart();
        salesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                index = 0;
                for(DataSnapshot mysp : dataSnapshot.getChildren()){
                    final Sales sales = mysp.getValue(Sales.class);
                    size = (int) dataSnapshot.getChildrenCount();

                    dp[index] = new DataPoint(index, sales.getSold());
                    name.add(sales.getName());


                    index++;
                }
                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphs);
                String[] stringArray = name.toArray(new String[0]);
                staticLabelsFormatter.setHorizontalLabels(stringArray);
                graphs.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

                bars.resetData(dp);
                ProgressBar pb = findViewById(R.id.progress_bar);
                pb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


