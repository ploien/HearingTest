package com.example.ploie.hearingtest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class QueryTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_test);
    }

    public void onQueryClick(View view) {
        Bundle data = getIntent().getExtras();
        User CurrentUser = data.getParcelable("user");
        Intent intent = new Intent(this, TestActivity.class);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("server/saving-data/fireblog/Tests/");
        Query query = userRef.orderByKey().
                      startAt("Fake Tester (Jul 6, 2018 12:42:54 PM)").
                      endAt("Fake Tester (Jul 6, 2018 12:42:54 PM)");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
