package com.example.ploie.hearingtest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    public static final Object monitor = new Object();
    public static boolean monitorState = false;

    static String CURRENT_USER = "bob";
    private User CurrentUser;
    private FirebaseAuth mAuth;
    private FirebaseUser User;
    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        CURRENT_USER = sharedPref.getString(getString(R.string.current_user), CURRENT_USER);
        Toast.makeText(this, CURRENT_USER,
                Toast.LENGTH_LONG).show();

        instantiateUser();

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
        mAuth = FirebaseAuth.getInstance();
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), RC_SIGN_IN);
    }


    /**
     * newTest is used to initiate a new test for the user when they click the "New Test" button.
     * @param view
     */
    public void newTest(View view) {

//        final Thread thread = new Thread(new Runnable() {
//            public Handler mHandler;
//
//            public void run() {
//                Looper.prepare();
//                setTesterName();;
//            }
//        });
//        thread.start();
//        waitForThread();
        CurrentUser = new User(User.getDisplayName(), "No Name Provided");

        Intent intent = new Intent(this, PreTestActivity.class);
        intent.putExtra("user", CurrentUser);
        startActivity(intent);
    }

    public void queryTest(View view) {
        Intent intent = new Intent(this, QueryTest.class);
        startActivity(intent);
    }

    public void showGraph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
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
