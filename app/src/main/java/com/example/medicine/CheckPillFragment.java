package com.example.medicine;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CheckPillFragment extends Fragment {
    private LinearLayout checkPillFragment;

    private SQLiteDatabase sqlDB;
    private MyDBHelper myHelper;

    private RecyclerView view_pillChekList;
    private PillListRecyclerViewAdapter PillListRecyclerViewAdapter;
    private ArrayList<PillList> pillCheckList = new ArrayList<>();


    @Override
    public void onResume() {
        super.onResume();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        myHelper = new MyDBHelper(getContext());
        sqlDB = myHelper.getReadableDatabase();

        pillCheckList.clear();
        pillCheckList.addAll(myHelper.allPListItems());

        checkPillFragment = (LinearLayout) inflater.inflate(R.layout.fragment_check_pill, container, false);

        view_pillChekList = (RecyclerView) checkPillFragment.findViewById(R.id.pill_checklist);
        PillListRecyclerViewAdapter = new PillListRecyclerViewAdapter(pillCheckList, getContext());
        PillListRecyclerViewAdapter.notifyItemRangeChanged(0, pillCheckList.size());
        view_pillChekList.setAdapter(PillListRecyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view_pillChekList.setLayoutManager(layoutManager);



        return checkPillFragment;
    }

}
