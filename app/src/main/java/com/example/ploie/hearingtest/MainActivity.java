package com.example.ploie.hearingtest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    public static final Object monitor = new Object();
    public static boolean monitorState = false;

    static String CURRENT_USER = "bob";
    private User CurrentUser = null;
    private FirebaseUser User;
    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        instantiateUser();
        TextView text = findViewById(R.id.textView2);
        text.setMovementMethod(new ScrollingMovementMethod());
        text.setTypeface(null, Typeface.BOLD);
        text.setTextColor(Color.BLACK);
        text.setText(R.string.disclaimer);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                User = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }


    private void instantiateUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), RC_SIGN_IN);
    }


    /**
     * newTest is used to initiate a new test for the user when they click the "New Test" button.
     * @param view
     */
    public void newTest(View view) {
        if (User == null) {
            instantiateUser();
        }
        else {
            CurrentUser = new User(User.getDisplayName(), "No Name Provided");
            Intent intent = new Intent(this, PreTestActivity.class);
            intent.putExtra("user", CurrentUser);
            startActivity(intent);
        }
    }

    public void queryTest(View view) {
        if (User == null) {
            instantiateUser();
        }
        else {
            Intent intent = new Intent(this, QueryTest.class);
            CurrentUser = new User(User.getDisplayName(), "Name");
            intent.putExtra("user", CurrentUser);
            startActivity(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
    }

    public void onResume() {
        super.onResume();

    }

}
