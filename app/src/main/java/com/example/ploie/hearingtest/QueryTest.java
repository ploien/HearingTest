package com.example.ploie.hearingtest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryTest extends AppCompatActivity {

    private ListView mUserList;
    private ArrayList<String> mResults = new ArrayList<>();
    final TestResults testResults = new TestResults();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_test);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("/server/saving-data/fireblog/Tests/Drew Lundgren (Jul 6, 2018 11:24:26 PM)/");
        mUserList = (ListView) findViewById(R.id.listView);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mResults);
        mUserList.setAdapter(arrayAdapter);


        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                TestResults value = dataSnapshot.getValue(TestResults.class);
                mResults.add(value.getParticipant_name());
                mResults.add(value.getDecibels().toString());
                mResults.add(value.getFrequencies().toString());
                testResults.setParticpant_name(value.getParticipant_name());
                testResults.setDecibels(value.getDecibels());
                testResults.setFrequencies(value.getFrequencies());

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onQueryClick(View view) {

        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra("testFrequencies", testResults.getFrequencies());
        intent.putExtra("testDecibels", testResults.getDecibels());
        startActivity(intent);
    }
}
