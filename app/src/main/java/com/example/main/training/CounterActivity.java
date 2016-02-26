package com.example.main.training;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.main.R;
import com.example.main.util.TinyDB;
import com.example.main.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class CounterActivity extends Activity implements TextToSpeech.OnInitListener{

    private Button btnStart, btnStop;
    private TextView textViewTotalTime, textExercise;
    EditText editTextTrainingTime, editTextPauseTime, editTextSeries;
    private SoundPool sounds;
    private int sndtick;
    MediaPlayer mediaPlayer = null;
    String soundTick = "tick";
    CounterClass timer;
    int totalTrainingTime, seriesTime, pauseTime, series, roundCounter;
    TextToSpeech textToSpeech;
    boolean switcherTrainingPause = false;
    boolean playFiveSeconds = true;
    Vibrator vibrator;
    private static final String five="Five";
    private  static final String prepare="Prepare";
    private String stop= "Stop";
    private String done = "Done";
    private String start = "Start";
   private SharedPreferences sharedPref;
    private String trainingType;
    private TinyDB tinyDB;
    List<String> exercisesList = new ArrayList<>();
    private  int exerciseCounter = 0;
    private ImageView imageExercise;
    private String exerciseName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        textToSpeech = new TextToSpeech(this, this);
        sharedPref = getSharedPreferences(getString(R.string.shared_pref_file), 0);


        Intent intent = getIntent();
        trainingType = intent.getStringExtra(getString(R.string.training_type));

        tinyDB = new TinyDB(this);
        exercisesList = tinyDB.getListString(trainingType);


        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        textViewTotalTime = (TextView) findViewById(R.id.textViewTotalTime);
        textExercise = (TextView) findViewById(R.id.textExercise);
        sounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sndtick = sounds.load(this, R.raw.ticks10s, 1);
        editTextTrainingTime = (EditText) findViewById(R.id.editTextTrainingTime);
        editTextPauseTime = (EditText) findViewById(R.id.editTextPauseTime);
        editTextSeries = (EditText) findViewById(R.id.editTextSeries);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        imageExercise = (ImageView) findViewById(R.id.imageExercise);


        // load saved values into the boxes
        loadValues();

        btnStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //total training time = (Training + pause) * series
                totalTrainingTime = (Integer.parseInt(editTextTrainingTime.getText().toString()) + Integer.parseInt(editTextPauseTime.getText().toString()))
                        * Integer.parseInt(editTextSeries.getText().toString()) * exercisesList.size() ;
                seriesTime = Integer.parseInt(editTextTrainingTime.getText().toString());
                pauseTime = Integer.parseInt(editTextPauseTime.getText().toString());
                textViewTotalTime.setText(editTextTrainingTime.getText());
//                editTextPauseTime.setText(String.valueOf(seekBarPause.getProgress()));
                series = Integer.parseInt(editTextSeries.getText().toString());
                roundCounter = series * 2 * exercisesList.size();

                if (timer != null) {
                    timer.cancel();
                }

                exerciseName = Util.getNormalExerciseName(exercisesList.get(0));
                textExercise.setText(exerciseName);
                int imageResource = getResources().getIdentifier("drawable/" + exercisesList.get(0), null, getPackageName());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageResource);
                imageExercise.setImageBitmap(bitmap);

                // START
                textToSpeech.speak(start, TextToSpeech.QUEUE_FLUSH, null);
                while (textToSpeech.isSpeaking()){
                    System.out.println("Do something or nothing while speaking..");
                }
                // Say exercise name
                textToSpeech.speak(exerciseName, TextToSpeech.QUEUE_FLUSH, null);
                while (textToSpeech.isSpeaking()){
                    System.out.println("Do something or nothing while speaking..");
                }

                //start countdown
                timer = new CounterClass(seriesTime, 1);
                timer.start();
                managerOfSound(soundTick);
            }
        });

        btnStop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                }
                mediaPlayer.stop();
            }
        });


    }


    protected void managerOfSound(String theText) {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        if (theText == soundTick) {
            mediaPlayer = MediaPlayer.create(this, R.raw.ticks10s);
        } else {
            mediaPlayer = MediaPlayer.create(this, R.raw.onetwo);
        }
        mediaPlayer.start();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            } else {

            }
        } else {
            Log.e("TTS", "Initilization Failed");
        }
    }

    @Override
    public void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        if (timer != null) {
            timer.cancel();
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        super.onPause();
    }

    /**
     * load values from Shared preferences, set seekbar and TextViews
     */

    private void loadValues() {
        String seriesTime =tinyDB.getString(trainingType + getString(R.string.training_time));
        String pauseTime = tinyDB.getString(trainingType + getString(R.string.pause_time));
        editTextTrainingTime.setText(seriesTime);
        editTextPauseTime.setText(pauseTime);
        editTextSeries.setText(tinyDB.getString(trainingType + getString(R.string.series)));

    }



    // ---------------------------- HELPER TIMER CLASS -----------------  //

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    public class CounterClass extends CountDownTimer {


        public CounterClass(long secondsInFuture, long countDownInterval) {
            super(secondsInFuture * 1000, (countDownInterval * 1000)-100);
        }

        @Override
        public void onFinish() {
            Log.i("onFinish ","finish");
            Log.i("roundCounter ",Integer.toString(roundCounter));
            playFiveSeconds=true;


            if (roundCounter == 2) {
                // Training is completed
                textViewTotalTime.setText("Completed.");
                textToSpeech.speak(done, TextToSpeech.QUEUE_FLUSH, null);
                timer.cancel();
                displaySaveTheDataDialog();
                return;
            }

            if (roundCounter > 0) {
                if (switcherTrainingPause) {
                    timer = new CounterClass(seriesTime, 1);
                    textToSpeech.speak(start, TextToSpeech.QUEUE_FLUSH, null);
                    Log.i("seriesTime ", String.valueOf(seriesTime));
                    switcherTrainingPause = !switcherTrainingPause;
                    timer.start();
                } else {
                    timer = new CounterClass(pauseTime, 1);
                    textToSpeech.speak(stop, TextToSpeech.QUEUE_FLUSH, null);
                    Log.i("pauseTime ", String.valueOf(pauseTime));
                    switcherTrainingPause = !switcherTrainingPause;
                    int seriesLeft = Integer.parseInt(editTextSeries.getText().toString())-1;
                    editTextSeries.setText(String.valueOf(seriesLeft));
                    timer.start();
                }
                roundCounter--;
                if (roundCounter % (series*2) ==0){  // Change the Exercise
                    exerciseCounter =  (exerciseCounter < exercisesList.size()-1) ? exerciseCounter+1 : exerciseCounter; // not overflow the index
                    exerciseName = Util.getNormalExerciseName(exercisesList.get(exerciseCounter));
                    textExercise.setText(exerciseName);  // change text
                    editTextSeries.setText(String.valueOf(series)); // reset the series
                    int imageResource = getResources().getIdentifier("drawable/" + exercisesList.get(exerciseCounter), null, getPackageName());
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageResource);
                    imageExercise.setImageBitmap(bitmap);
                    textToSpeech.speak(exerciseName, TextToSpeech.QUEUE_FLUSH, null);
                    while (textToSpeech.isSpeaking()){
                        System.out.println("Do something or nothing while speaking..");
                    }
                }
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i("milistofinihed = ", String.valueOf(millisUntilFinished / 1000));
            long milis = millisUntilFinished;

            String hmsTotal = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(milis),
                    TimeUnit.MILLISECONDS.toMinutes(milis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milis)),
                    TimeUnit.MILLISECONDS.toSeconds(milis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milis)));
            Log.i("hmsTotal ", hmsTotal);
            textViewTotalTime.setText(hmsTotal);

            if (milis < 6000 && playFiveSeconds){
                playFiveSeconds = false;
                if (!switcherTrainingPause){
                    textToSpeech.speak(five, TextToSpeech.QUEUE_FLUSH, null);
                }else{
                    textToSpeech.speak(prepare, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        }
    }


    private void displaySaveTheDataDialog(){
        final ArrayList<String> trainingDaysList;
        trainingDaysList = (tinyDB.getListString(getString(R.string.training_days_list)) == null) ? new ArrayList<String>() : tinyDB.getListString(getString(R.string.training_days_list));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        Date today = new Date();
        String dateToSave = sdf.format(today);
        trainingDaysList.add(dateToSave);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CounterActivity.this);
        alertDialogBuilder.setTitle("Save the Date?");
        alertDialogBuilder
                .setMessage("Would you like to save the training to the calendar?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        tinyDB.putListString(getString(R.string.training_days_list), trainingDaysList);
                        Toast.makeText(CounterActivity.this, "Training Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
