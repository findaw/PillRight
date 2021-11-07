package com.example.medicine;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1000;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1001;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 1002;

    protected BackPressCloseHandler backPressCloseHandler;

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private CheckPillFragment checkPillFragment;
    private PillListFragment pillListFragment;
    private CalendarFragment calendarFragment;
    private SearchFragment searchFragment;
    private FindFragment findFragment;

    String[] PERMISSIONS ={
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);

//
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                setFragment(menuItem.getItemId());
                return true;
            }
        });

        checkPillFragment = new CheckPillFragment();
        calendarFragment = new CalendarFragment();
        searchFragment = new SearchFragment();
        pillListFragment = new PillListFragment();
        findFragment = new FindFragment();

        setFragment(R.id.page_check_pill);
        bottomNavigationView.setSelectedItemId(R.id.page_check_pill);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(!hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this, this.PERMISSIONS, this.PERMISSION_REQUEST_CODE);
            }
        }

    }
    public boolean hasPermissions(Context context, String[] permissions){
        if(context != null && permissions != null){
            for(String permission : permissions){
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }

            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {

                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            continue;
                        } else {
                            Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                }
                break;


        }
    }

    private void setFragment(int pageId){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch(pageId){
            case R.id.page_check_pill:
                Log.d("setFragment()", "page_check_pill");
                ft.replace(R.id.main_frame, checkPillFragment);
                ft.commit();
                break;
            case R.id.page_pill_list:
                Log.d("setFragment()", "page_pill_list");
                ft.replace(R.id.main_frame, pillListFragment);
                ft.commit();
                break;
            case R.id.page_search:
                Log.d("setFragment()", "page_search");
                ft.replace(R.id.main_frame, searchFragment);
                ft.commit();
                break;
            case R.id.page_find:
                Log.d("setFragment()", "page_find");
                ft.replace(R.id.main_frame, findFragment);
                ft.commit();
                break;
            case R.id.page_calendar:
                Log.d("setFragment()", "page_calendar");
                ft.replace(R.id.main_frame, calendarFragment);
                ft.commit();
                break;

        }

    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
