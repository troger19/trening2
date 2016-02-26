package com.example.main.training;

/**
 * Created by jan.babel on 18/08/2015.
 */


import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.main.R;
import com.example.main.util.TinyDB;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;


public class TrainingList extends ListFragment {

    private Resources res;
    private TinyDB tinyDB;
    private List<String> exercisesList = new ArrayList<>();
    private List<Integer> activeTraining = new ArrayList<>();

    @Override
        public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        res = getResources();
        tinyDB = new TinyDB(getActivity());

        String[] trainings = res.getStringArray(R.array.trainings_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, trainings) {
            // colorize the saved training's positions
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                exercisesList = tinyDB.getListString(text.getText().toString());
                if (exercisesList.size() != 0) {
                    text.setTextColor(Color.GREEN);
                    activeTraining.add(position);
                }
                return view;
            }
        };
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (activeTraining.contains(position)){
            Intent intent = new Intent(getActivity(), CounterActivity.class);
            intent.putExtra(getString(R.string.training_type),getListAdapter().getItem(position).toString());
            startActivity(intent);
        }else{
            makeText(getActivity(), "No exercises to load", LENGTH_LONG).show();
        }
    }
}