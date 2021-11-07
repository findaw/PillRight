package com.example.medicine;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment {
    private Calendar cal = Calendar.getInstance();
    private CalendarView calV;
    private long cur;

    private int year;
    private int month;
    private int dayOfMonth;
    private Date day;

    private LinearLayout calendarFragment;

    private SQLiteDatabase sqlDB;
    private MyDBHelper myHelper;

    private RecyclerView view_pillChekList;
    private CalendarPillListRecyclerViewAdapter listAdapter;
    private ArrayList<PillList> pillCheckList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        calendarFragment = (LinearLayout) inflater.inflate(R.layout.fragment_calendar, container, false);
        defaultDate();
        return calendarFragment;
    }


    @Override
    public void onResume() {
        super.onResume();

        calV = (CalendarView) calendarFragment.findViewById(R.id.calV);
        cur = calV.getDate();

        calV.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year2, int month2, int dayOfMonth2) {
                year = year2;
                month = month2 + 1;
                dayOfMonth = dayOfMonth2;
                display();
            }
        });

        display();
    }

    private void defaultDate() {
        int curYear = cal.get(Calendar.YEAR);
        int curMonth = cal.get(Calendar.MONTH) + 1;
        int curDayOfMonth = cal.get(Calendar.DATE);

        year = curYear;
        month = curMonth;
        dayOfMonth = curDayOfMonth;
    }

    private void display() {
        MyDBHelper myDBHelper = new MyDBHelper(getContext());
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String dbDate = "";
        int m = month - 1;
        try {
            day = dt.parse("" + year + "-" + month + "- " + dayOfMonth);
            dbDate = dt.format(day);
            setDateDayOption(day, dbDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDateDayOption(Date d, String dbDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayNum) {
            case 1:
                setListView("일", dbDate);
                break;
            case 2:
                setListView("월", dbDate);
                break;
            case 3:
                setListView("화", dbDate);
                break;
            case 4:
                setListView("수", dbDate);
                break;
            case 5:
                setListView("목", dbDate);
                break;
            case 6:
                setListView("금", dbDate);
                break;
            case 7:
                setListView("토", dbDate);
                break;
        }
    }

    private void setListView(String targetWeek, String targetDate){
        myHelper = new MyDBHelper(getContext());
        sqlDB = myHelper.getReadableDatabase();

        pillCheckList.clear();
        pillCheckList.addAll(myHelper.allPListItems(targetWeek, targetDate));

        view_pillChekList = calendarFragment.findViewById(R.id.list_take_pill);
        listAdapter = new CalendarPillListRecyclerViewAdapter(pillCheckList, getContext(), day);
        listAdapter.notifyItemRangeChanged(0, pillCheckList.size());
        view_pillChekList.setAdapter(listAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view_pillChekList.setLayoutManager(layoutManager);

    }

}
