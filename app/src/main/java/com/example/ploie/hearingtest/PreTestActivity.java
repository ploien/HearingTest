package com.example.ploie.hearingtest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class PreTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pre_test);
    }

    public void onBeginClick(View view) {
        Bundle data = getIntent().getExtras();
        User CurrentUser = data.getParcelable("user");
        Intent intent = new Intent(this, TestActivity.class);

        EditText box = (EditText) findViewById(R.id.editText);
        String name = box.getText().toString();

        Log.d("PreTestActivity", "Setting Screener's Name");
        CurrentUser.setname(name);
        intent.putExtra("user", CurrentUser);
        startActivity(intent);
    }


}
