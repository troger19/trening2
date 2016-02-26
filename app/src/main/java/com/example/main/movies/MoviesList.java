package com.example.main.movies;

/**
 * Created by jan.babel on 18/08/2015.
 */


import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.main.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MoviesList extends ListFragment {

    private Resources res;
    private String[] moviesList;
    private String[] moviesDescription;

    // Array of all images in res/drawable
    int[] flags = new int[]{
            R.drawable.abs_chest_mixed_workout,
            R.drawable.abs_workout1_level2,
            R.drawable.abs_workout1_level3,
            R.drawable.abs_workout2_hip_hop,
            R.drawable.abs_workout3_rock,
            R.drawable.abs_workout4_brazil,
            R.drawable.abs_workout5_insane_special_edition,
            R.drawable.abs_workout5_insane_workout_round2,
            R.drawable.abs_chest_mixed_workout,
            R.drawable.abs_chest_mixed_workout2,
            R.drawable.six_pack_abs_workout,
            R.drawable.women_4_minutes_abs_workout

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        res = getResources();
        moviesList = res.getStringArray(R.array.movies_array);
        moviesDescription = res.getStringArray(R.array.movies_description_array);
        // Each row in the list stores exercise name, description and image
        List<HashMap<String, String>> aList = new ArrayList<>();

        for (int i = 0; i < flags.length; i++) {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("movie", moviesList[i]);
            hm.put("desc", moviesDescription[i]);
            hm.put("image", Integer.toString(flags[i]));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"image", "movie", "desc"};

        // Ids of views in movies_list
        int[] to = {R.id.image, R.id.title, R.id.description};

        // Instantiating an adapter to store each items
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.movies_list, from, to);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), MoviesActivity.class);
        String movieName = moviesDescription[position];
        intent.putExtra(getString(R.string.selected_movie), movieName);
        startActivity(intent);
    }


}