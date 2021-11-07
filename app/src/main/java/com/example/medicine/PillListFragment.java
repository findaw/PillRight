package com.example.medicine;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class PillListFragment extends Fragment {
    private LinearLayout pillListFragment;
    private MyDBHelper myHelper;

    ImageView btn_plus_fill;

    private RecyclerView view_pillList;
    private ListItemRecyclerViewAdapter listItemRecyclerViewAdapter;
    private ArrayList<ListItem> pillList = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();

        myHelper = new MyDBHelper(getContext());

        pillList.clear();
        pillList.addAll(myHelper.allListItems());
        view_pillList = (RecyclerView) pillListFragment.findViewById(R.id.pill_list);
        listItemRecyclerViewAdapter = new ListItemRecyclerViewAdapter(pillList, getContext());
        view_pillList.invalidate();
        listItemRecyclerViewAdapter.notifyItemRangeChanged(0, pillList.size());

        view_pillList.setAdapter(listItemRecyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view_pillList.setLayoutManager(layoutManager);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        pillListFragment = (LinearLayout) inflater.inflate(R.layout.fragment_pill_list, container, false);

        btn_plus_fill = (ImageView) pillListFragment.findViewById(R.id.btn_plus_fill);

        btn_plus_fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        return pillListFragment;
    }
}