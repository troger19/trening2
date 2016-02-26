package com.example.main.configuration;

/**
 * Created by jan.babel on 18/08/2015.
 */


import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.main.R;
import com.example.main.util.TinyDB;

import java.util.ArrayList;
import java.util.List;


public class TrainingConfigurationNew extends ListFragment {

    private Resources res;
    private TinyDB tinyDB;
    private List<String> exercisesList = new ArrayList<>();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        res = getResources();
        tinyDB = new TinyDB(getActivity());
        final String[] trainings = res.getStringArray(R.array.trainings_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, trainings) {
            // highlight the saved trainings
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                exercisesList = tinyDB.getListString(text.getText().toString());
                if (exercisesList.size() != 0) {
                    text.setTextColor(Color.GREEN);
                }
                return view;
            }
        };
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        final Intent intent = new Intent(getActivity(), TrainingConfigurationActivity.class);
        final Intent intent = new Intent(getActivity(), TrainingConfigurationActivityNew.class);
        final String trainingType = getListAdapter().getItem(position).toString();
        intent.putExtra(getString(R.string.training_type), trainingType);
        tinyDB = new TinyDB(getActivity());
        exercisesList = tinyDB.getListString(trainingType);
        // if there is a training already saved, display dialog, otherwise allow editing wihtout warning
        if (exercisesList.size() > 0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle("Start editing?");
            alertDialogBuilder
                    .setMessage("Previous exercises for this training will be overwritten: " + exercisesList.toString())
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(intent);
                        }
                    })
                    .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //delete all the trainign related variables from DB
                            tinyDB.remove(trainingType);
                            tinyDB.remove(trainingType + getString(R.string.training_time));
                            tinyDB.remove(trainingType + getString(R.string.pause_time));
                            tinyDB.remove(trainingType + getString(R.string.series));
                            Toast.makeText(getActivity(),"Training was deleted",Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            startActivity(intent);
        }
    }
}


