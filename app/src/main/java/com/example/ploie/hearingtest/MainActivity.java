package com.example.ploie.hearingtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static String CURRENT_USER = "bob";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        CURRENT_USER = sharedPref.getString(getString(R.string.current_user), CURRENT_USER);
        Toast.makeText(this, CURRENT_USER,
                Toast.LENGTH_LONG).show();
    }

    public void newTest(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String newUser = "Jimmy Bob Joe";
        editor.putString(getString(R.string.current_user), newUser);
        editor.commit();

    }

    public void onResume() {
        super.onResume();


    }

}
