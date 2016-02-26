package com.example.main.configuration;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.main.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kaktus on 31.1.2016.
 */
public class TrainingConfigurationActivityNew extends AppCompatActivity {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_configuration_new);

        final Intent intent = new Intent(getBaseContext(), AllExerciseList.class);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        List<String> list = new LinkedList<String>();
        for (int i = 1; i < 30; i++) {
            list.add(String.valueOf(i));
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, android.R.id.text1, list));

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TrainingConfigurationActivityNew.this, "nazdoooo", Toast.LENGTH_SHORT).show();
                System.out.println("kruuvava");
                startActivity(intent);
//                fab.hide();
//                FragmentManager frgManager = getFragmentManager();
////                frgManager.beginTransaction().replace(R.id.main_content, new ExerciseList()).commit();
//                frgManager.beginTransaction().replace(R.id.listView, new ExerciseList())
//                        .commit();


            }
        });

    }
}
