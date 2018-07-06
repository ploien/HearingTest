package com.example.ploie.hearingtest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.*;

/**
 * TestActivity starts a hearing test and
 * and contains all the methods controlling
 * the flow of the test.
 */
public class TestActivity extends AppCompatActivity {

    /**
     * Used to lock yes and no buttons until a
     * given frequency has finished playing.
     */
    public static final Object monitor = new Object();

    /**
     * Used for the monitor object
     */
    public static boolean monitorState = false;

    private boolean testComplete = false;
    private TestResults finalResults = null;
    private boolean yesClicked = false;
    private boolean noClicked = false;
    final static String TEST_ACTIVITY = "TestActivity";

    //Button yesButton = (Button) findViewById(R.id.yesButton);
    //Button noButton = (Button) findViewById(R.id.noButton);
    //Button startButton = (Button) findViewById(R.id.button3);

    @Override
    /**
     * Sets view for Test activity
     * @return void
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();

    }

    private static void unlockWaiter() {
        synchronized (monitor) {
            monitorState = false;
            monitor.notifyAll();
        }
    }


    /**
     * Sets yesClick to true when user clicks yes
     * @param view
     */
    public void onYesClick(View view) {

        yesClicked = true;
        unlockWaiter();
    }

    /**
     * Sets noClick to true when user clicks no
     * @param view
     */
    public void onNoClick(View view) {

        noClicked = true;
        unlockWaiter();
    }

    private static void waitForThread() {

        monitorState = true;
        while (monitorState) {
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (Exception ignored) {}
            }
        }

    }


    /**
     * Runs the Hearing test when startClick is clicked
     * (runs on background thread)
     * @return void
     * @param view
     */
    public void onStartClick(View view) {

        //startButton.setEnabled(false);
        final Thread thread = new Thread(new Runnable() {
            public Handler mHandler;

            public void run() {
                Looper.prepare();
                test();
            }
        });
        thread.start();
    }


    public void saveResults(View view){
        Bundle data = getIntent().getExtras();
        User CurrentUser = data.getParcelable("user");
        if (CurrentUser.getUsername() == "Drew Lundgren"
                || CurrentUser.getUsername() == "Peter Oien")
        {
            testComplete = true;
        }

        if (testComplete) {
            if (finalResults == null) {
                finalResults = new TestResults();
                List<String> dummy = new ArrayList<>();
                dummy.add("1");
                dummy.add("2");
                dummy.add("3");
                dummy.add("4");
                dummy.add("5");
                finalResults.setFrequencies(dummy);
                finalResults.setDecibels(dummy);
                finalResults.setParticpant_name("Demo Screening");
            }


            finalResults.setParticpant_name(CurrentUser.getname());

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("server/saving-data/fireblog/");
            DatabaseReference usersRef = reference.child("Users" + "/" + CurrentUser.getUsername());

            Map<String, User> users = new HashMap();
            users.put(CurrentUser.getUsername(), CurrentUser);

            usersRef.setValue(users);

            DatabaseReference resultsRef = reference.child("Tests" + "/" + CurrentUser.getUsername()
                    + " (" + DateFormat.getDateTimeInstance().format(new Date()) + ")");
            // Since we are storing test data this way, we will have to query it with a command kind of like this:
            // yourRef.orderByKey().startAt("abc").endAt("abc\uf8ff") in order to do a partial key query.
            Map<String, TestResults> results = new HashMap();
            results.put("Test", finalResults);
            resultsRef.setValue(results);
        }
    }

    private void displayResults(String results) {

    }


//     Below is an alpha example of how the test should run. Here are the steps for the code:
//
//     1. A boolean value is created to act as a gate, preventing testing on a specific frequency
//        from ending before testing conditions are met. A mock boolean conditionsMet currently
//        allows the while loop to always complete.
//
//     2. A for loop, simulating 16 tone tests is created.
//
//     3. The boolean, testingFrequency, is flagged as true in the beginning of each iteration of
//        the for loop. This ensures that each tone is tested.
//
//     4. A sleep command is used to simulate the playing of the tone.
//
//     5. The booleans 'waiting' and 'buttonClicked' are set in order to allow the user's input
//        to the yes / no buttons. The selection of either button allows the test to continue.
//        the test currently uses an infinite while loop to simulate waiting for user input.
//
//     6. If the user clicked yes or no, reactions in the test would take place. For now, it simply
//        changes the booleans declared at the beginning of TestActivity.
//
//     7. Since the conditions are always "Met", the testingFrequency boolean is switched to false,
//        indicating the completion of the current frequency.


    private void test() {

        final TextView frequencyView = findViewById(R.id.frequencyView);
        final TextView decibelView = findViewById(R.id.decibelView);
        final ProgressBar bar = findViewById(R.id.progressBar);



        boolean conditionsMet = false; // placeholder for conditions for frequency met (3/5 tones heard at appropriate level)

        PlaySound play;
        //yesButton.setEnabled(false);
        //noButton.setEnabled(false);

        double frequencies[] = {1000, 2000, 4000, 8000, 1000, 500, 250, 125};
        TestResults results = new TestResults();
        List<String> decibels = new ArrayList<>();
        List<String> testedFrequencies = new ArrayList<>();

        for (final double frequency : frequencies) {

            Log.d(TEST_ACTIVITY, "New Frequency: " + Double.toString(frequency));

            int lowestYesVolume = 0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    {
                        frequencyView.setText(Double.toString(frequency ) + " Hz");
                    }
                }
            });



            play = new PlaySound();
            play.setFrequency(frequency);


            boolean firstNo = false;
            boolean yesAfterNo = false;
            conditionsMet = false;

            int yesCount = 0;
            int noCount = 0;


            while (!conditionsMet) {

                final String currentDecibel = Integer.toString(play.getDecibel());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        {
                            decibelView.setText(currentDecibel + " dB");
                        }
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        {

                            ValueAnimator animator = ValueAnimator.ofInt(0, bar.getMax());
                            animator.setDuration(2250);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    bar.setProgress((Integer) animation.getAnimatedValue());
                                }
                            });
                            animator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    // start your activity here
                                }
                            });
                            animator.start();

                        }
                    }
                });



                // Play the sound here, simulated by Thread sleeping
                play.genTone();
                play.playSound();


                //yesButton.setEnabled(true);
                //noButton.setEnabled(true);
                waitForThread(); // Current placeholder for test is waiting


                if (yesClicked) {
                    // do yes conditions (lower volume or tally result)
                    yesClicked = false;

                    //Conditions after we hear no the first time
                    if (firstNo && (!yesAfterNo)) {
                        yesAfterNo = true;
                        lowestYesVolume = play.getDecibel();
                        ++yesCount;
                    //After getting our first yes when increasing in volume
                    } else if (firstNo) {
                        ++yesCount;
                        if (lowestYesVolume > play.getDecibel()) {
                            lowestYesVolume = play.getDecibel();
                            yesCount = 1;
                            noCount = 0;
                        }
                    }
                    play.decreaseVolume();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else if (noClicked) {
                    // increase volume, reset tally results
                    noClicked = false;

                    //Conditions after we hear no the first time
                    if (!firstNo) {
                        firstNo = true;
                    //start going back up after hearing no for the first time
                    } else if (lowestYesVolume == play.getDecibel()) {
                        ++noCount;
                    }
                    play.increaseVolume();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //Once we've found the lowest audible volume
                //evaluate whether they've heard it 3/5 times
                if ((yesCount > 2)) {
                    conditionsMet = true;
                } else if (noCount > 2) {
                    yesCount = 0;
                    noCount = 0;
                    yesAfterNo = false;
                    play.increaseVolume();
                }

            }
            play.increaseVolume();
            play.increaseVolume();
            Log.d(TEST_ACTIVITY, "Saved decibel: " + Double.toString(play.getDecibel()));
            decibels.add(Integer.toString(play.getDecibel()));
            testedFrequencies.add(Double.toString(frequency));
            //send info to json string
        }

        results.setDecibels(decibels);
        results.setFrequencies(testedFrequencies);
        finalResults = results;
        testComplete = true;
    }


}
