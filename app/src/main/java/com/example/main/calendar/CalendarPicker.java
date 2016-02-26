package com.example.main.calendar;

/**
 * Created by jan.babel on 18/08/2015.
 */

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.main.R;
import com.example.main.util.TinyDB;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarPicker extends Fragment {

    private CalendarPickerView calendar;
    private TinyDB tinyDB;

    public CalendarPicker() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_two, container, false);

    //  ------  define the calendar --------
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 2);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        String startDateDate = "01-07-2015"; // TODO posunut starting date
        Date startDate = null;
        try {
            startDate = sdf.parse(startDateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar = (CalendarPickerView) view.findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(startDate, endDate.getTime())
                .withSelectedDate(today)
                .inMode(CalendarPickerView.SelectionMode.RANGE);

        // TODO dorobit farbicky
        calendar.setCacheColorHint(Color.RED);
        calendar.setDrawingCacheBackgroundColor(Color.BLUE);
        calendar.setDrawingCacheEnabled(true);
        calendar.highlightDates(getTrainingDates());
 // ------------- end of calendar configuration -------------------------

        return view;
    }

    private ArrayList<Date> getTrainingDates() {
        tinyDB = new TinyDB(getActivity());
        ArrayList<String> trainingDaysList;
        trainingDaysList = (tinyDB.getListString(getString(R.string.training_days_list)) == null) ? new ArrayList<String>() : tinyDB.getListString(getString(R.string.training_days_list));

//        trainingDaysList.add("24-07-2015");
//        trainingDaysList.add("25-07-2015");
//        trainingDaysList.add("26-07-2015");
//        trainingDaysList.add("27-07-2015");
//        trainingDaysList.add("22-08-2015");
//        tinyDB.putListString(getString(R.string.training_days_list),trainingDaysList);


        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");

        ArrayList<Date> trainingDates = new ArrayList<>();
        for (String trainingDay : trainingDaysList) {
            Date date = null;
            try {
                date = sdf.parse(trainingDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            trainingDates.add(date);
        }
        return trainingDates;
    }

}


