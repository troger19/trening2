package com.example.main.configuration;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.main.R;
import com.example.main.util.TinyDB;

import java.util.ArrayList;


public class TrainingConfigurationActivity extends AppCompatActivity {

    private LinearLayout mLayout, firstLayout;
    private Spinner spinner;
    private Button btnAdd,btnSave, btnCancel;
    private EditText editTraining, editPause,editSeries;
    private Resources res;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String trainingType, trainingTime, pauseTime, series;
    private TinyDB tinyDB;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_configuration);

        sharedPref = getSharedPreferences(getString(R.string.shared_pref_file), 0);
        tinyDB = new TinyDB(this);

        Intent intent = getIntent();
        trainingType = intent.getStringExtra(getString(R.string.training_type));

        mLayout = (LinearLayout) findViewById(R.id.linearLayoutMain);
//        firstLayout = (LinearLayout) findViewById(R.id.linearLayout1);
        firstLayout = (LinearLayout) findViewById(R.id.placeholder);
//        spinner = (Spinner) findViewById(R.id.spinner1);
        btnAdd = (Button) findViewById(R.id.button);
        btnAdd.setOnClickListener(onClick());

        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave.setOnClickListener(onSaveClick());
        editTraining= (EditText) findViewById(R.id.editTraining);
        editPause= (EditText) findViewById(R.id.editPause);
        editSeries= (EditText) findViewById(R.id.editSeries);

        res = getResources();
    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                firstLayout.addView(createNewLinearLayout());
            }
        };
    }


    private View.OnClickListener onSaveClick() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int dropDownsCount =  firstLayout.getChildCount();
                final ArrayList<String> exerciseList = new ArrayList<>();
                for (int i=0; i<dropDownsCount;i++) {
//                    editor.putString(trainingType + "_exercise" + i,  firstLayout.getChildAt(i).toString());
                    LinearLayout linearLayout = (LinearLayout) firstLayout.getChildAt(i);
                    Spinner tempSpinner = (Spinner) linearLayout.getChildAt(0);
                    exerciseList.add(tempSpinner.getSelectedItem().toString());
                }


                //check for not empty
                if(exerciseList.size()==0) {
                    btnAdd.setError("Add some exercise");
                    return;
                }
                if(TextUtils.isEmpty(editTraining.getText())) {
                    editTraining.setError("Put training time");
                    return;
                }
                if(editTraining.getText().toString().startsWith("0")){
                    editTraining.setError("Training time cannot be 0");
                    return;
                }

                if(TextUtils.isEmpty(editPause.getText())) {
                    editPause.setError("Put pause time");
                    return;
                }
                if(editPause.getText().toString().startsWith("0")){
                    editPause.setError("Pause time cannot be 0");
                    return;
                }
                if(TextUtils.isEmpty(editSeries.getText())) {
                    editSeries.setError("Put number of series");
                    return;
                }
                if(editSeries.getText().toString().startsWith("0")) {
                    editSeries.setError("Number of series cannot be 0");
                    return;
                }

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TrainingConfigurationActivity.this);
                alertDialogBuilder.setTitle("Save Changes?");
                alertDialogBuilder
                        .setMessage("Previous training settings would be overwritten")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, save the settings, finish activity and press Back button
                                tinyDB.putListString(trainingType, exerciseList);
                                tinyDB.putString(trainingType + getString(R.string.training_time), editTraining.getText().toString());
                                tinyDB.putString(trainingType + getString(R.string.pause_time), editPause.getText().toString());
                                tinyDB.putString(trainingType + getString(R.string.series), editSeries.getText().toString());

                                Toast.makeText(TrainingConfigurationActivity.this, "Training Saved", Toast.LENGTH_SHORT).show();
                                TrainingConfigurationActivity.this.finish();
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
        };
    }


    private LinearLayout createNewLinearLayout(){
        LinearLayout linearLayout = new LinearLayout(this);
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final Spinner spinner = new Spinner(this);
        String[] items = res.getStringArray(R.array.exercises_arrays);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        spinner.setAdapter(adapter);
        spinner.setLayoutParams(lparams);
        linearLayout.addView(spinner);

        ViewGroup layout = (ViewGroup) btnAdd.getParent();
        if(null!=layout) //for safety only  as you are doing onClick
            layout.removeView(btnAdd);
        linearLayout.addView(btnAdd);

        return linearLayout;

    }
}

