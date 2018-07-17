package com.example.ploie.hearingtest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
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
    private DatabaseReference database;
    private String testLocation;
    private ListView mUserList;
    private ListView mKeyList;
    private ArrayList<String> mResults = new ArrayList<>();
    final TestResults testResults = new TestResults();
    private ArrayList<String> kResults = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_test);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle data = getIntent().getExtras();
        final User CurrentUser = data.getParcelable("user");
        testLocation = CurrentUser.getUsername();

        String user = CurrentUser.getUsername();
        Intent intent = new Intent(this, TestActivity.class);

        mUserList = (ListView) findViewById(R.id.listView);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mResults);
        mUserList.setAdapter(arrayAdapter);

        DatabaseReference keysRef = FirebaseDatabase.getInstance().getReference("/server/saving-data/fireblog/");
        mKeyList = (ListView) findViewById(R.id.keysView);
        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, kResults);
        mKeyList.setAdapter(arrayAdapter2);

        mKeyList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        testLocation = String.valueOf(parent.getItemAtPosition(position));
                        database = FirebaseDatabase.getInstance().getReference("/server/saving-data/fireblog/Tests/" + testLocation + "/");
                        database.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                mResults.clear();
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
                }
        );

        keysRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DataSnapshot usersSnapshot = dataSnapshot.child("Tests");
                Iterable<DataSnapshot> keyChildren = dataSnapshot.getChildren();

                for (DataSnapshot key : keyChildren) {
                    if(key.getKey().contains(CurrentUser.getUsername() + " (")){
                        kResults.add(key.getKey());
                    }
                }

                arrayAdapter2.notifyDataSetChanged();
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
