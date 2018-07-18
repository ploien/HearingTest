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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryTest extends AppCompatActivity {
    private DatabaseReference database;
    private String testLocation;
    private String deleteLocation = null;
    private ListView mUserList;
    private ListView mKeyList;
    private ArrayList<String> mResults = new ArrayList<>();
    private ArrayList<String> kResults = new ArrayList<>();
    private boolean canQuery = false;
    final TestResults testResults = new TestResults();


    // The onCreate method queries the user's latest testing information from the database.
    // this is done using the User's username.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_test);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Create a database reference in order to find the keys that match the username
        DatabaseReference keysRef = FirebaseDatabase.getInstance().getReference("/server/saving-data/fireblog/");

        // Get the user's information, and set the testLocation to be wherever the user's tests are stored.
        Bundle data = getIntent().getExtras();
        final User CurrentUser = data.getParcelable("user");
        testLocation = CurrentUser.getUsername();


        // This list is used to track the user's test results, and an arrayAdapter is used to put that information
        // into the text view.
        mUserList = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mResults);
        mUserList.setAdapter(arrayAdapter);


        // This list is used to see the key for all test results stored. The user will be able to
        // select which test he wishes to view, and the mUserList ListView will show the results.
        mKeyList = (ListView) findViewById(R.id.keysView);
        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, kResults);
        mKeyList.setAdapter(arrayAdapter2);


        // This onClickListener queries the database for the test at the location selected from the listView
        // by the user. This information is sent to the mUserList listView to show what test is currently
        // selected.
        mKeyList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        testLocation = String.valueOf(parent.getItemAtPosition(position));
                        database = FirebaseDatabase.getInstance().getReference("/server/saving-data/fireblog/Tests/" + testLocation + "/");
                        deleteLocation = "/server/saving-data/fireblog/Tests/" + testLocation + "/";

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
                                if (canQuery == false) {canQuery = true;}
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                mResults.clear();
                                arrayAdapter.notifyDataSetChanged();
                                canQuery = false;
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

        // This updates the list with all of the user's tests.
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
                kResults.clear();
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
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // This method allows the user to take the information from the query and view the results in a graph.
    public void onQueryClick(View view) {
        if (canQuery) {
            Intent intent = new Intent(this, GraphActivity.class);
            intent.putExtra("testFrequencies", testResults.getFrequencies());
            intent.putExtra("testDecibels", testResults.getDecibels());
            startActivity(intent);
        }
    }

    // Deletes the currently selected test.
    public void onDeleteClick(View view) {
        if (deleteLocation != null) {
            Bundle data = getIntent().getExtras();
            final User CurrentUser = data.getParcelable("user");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference(deleteLocation);

            Map<String, TestResults> results = new HashMap();
            results.put("Test", null);
            reference.setValue(results);
            deleteLocation = null;
            canQuery = false;
        }
    }
}
