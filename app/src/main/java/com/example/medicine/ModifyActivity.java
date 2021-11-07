package com.example.medicine;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ModifyActivity extends AppCompatActivity {
    ImageView btn_back;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button btn_modify, start, end, btn_inPhoto;
    EditText number, name;
    LinearLayout timelayout1, timelayout2, timelayout3, timelayout4, timelayout5;
    SQLiteDatabase sqlDB;
    MyDBHelper myHelper;
    Integer timesPerDay;
    Integer[] day_array;
    String[] time_array;
    String startday, endday;
    TextView[] tv = new TextView[5];
    Integer Y, M, D;
    final int DAY_TRUE = 1, DAY_FALSE = 0;
    final int WEEK_MON = 0, WEEK_TUE = 1, WEEK_WED = 2, WEEK_THU = 3, WEEK_FRI = 4, WEEK_SAT = 5, WEEK_SUN = 6;
    Spinner timeSpinner;
    ArrayAdapter timeAdapter;
    final int GET_CROP_IMAGE_PATH = 1;
    String filePath = "";
    String prefilePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        myHelper = new MyDBHelper(this);

        Intent intent = getIntent();
        String mName = intent.getStringExtra("mediName");
        String mStart = intent.getStringExtra("startDate");
        String mEnd = intent.getStringExtra("endDate");

        final int MediId = intent.getIntExtra("mediId", 0);

        final int iMon = intent.getIntExtra("mon", 0);
        final int iTue = intent.getIntExtra("tue", 0);
        final int iWed = intent.getIntExtra("wed", 0);
        final int iThu = intent.getIntExtra("thu", 0);
        final int iFri = intent.getIntExtra("fri", 0);
        final int iSat = intent.getIntExtra("sat", 0);
        final int iSun = intent.getIntExtra("sun", 0);
        filePath = intent.getStringExtra("photo_id");

        final int mTimes = intent.getIntExtra("timesPerDay", 0);
        String one = intent.getStringExtra("oneTime");
        String two = intent.getStringExtra("twoTime");
        String three = intent.getStringExtra("threeTime");
        String four = intent.getStringExtra("fourTime");
        String five = intent.getStringExtra("fiveTime");

        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_modify = (Button) findViewById(R.id.btn_modify);
        start = (Button) findViewById(R.id.startBtn);
        end = (Button) findViewById(R.id.endBtn);
        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.number);

        final TextView startDate = (TextView) findViewById(R.id.startDate);
        final TextView endDate = (TextView) findViewById(R.id.endDate);

        startDate.setText(" " + mStart.substring(0,4) + "년 " + mStart.substring(5, 7) +"월 " +
                mStart.substring(8, 10) + "일");
        endDate.setText(" " + mEnd.substring(0, 4) + "년 " + mEnd.substring(5, 7) +"월 " +
                mEnd.substring(8, 10) + "일");

        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        startday = mStart;
        endday = mEnd;

        Y = Integer.parseInt(mStart.substring(0,4));
        M = Integer.parseInt(mStart.substring(5, 7));
        D = Integer.parseInt(mStart.substring(8, 10));

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ModifyActivity.this, R.style.DialogTheme, mDateSetListener, mYear, mMonth, mDay).show();
            }

            public DatePickerDialog.OnDateSetListener mDateSetListener =
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            Y = year;
                            M = monthOfYear + 1;
                            D = dayOfMonth;
                            UpdateNow();
                        }
                    };

            void UpdateNow() {
                if(mMonth < 9) {
                    if(mDay < 10) {
                        startDate.setText(String.format(" %d년 0%d월 0%d일", mYear, mMonth + 1, mDay));
                        startday = String.format("%d-0%d-0%d", mYear, mMonth + 1, mDay);
                    } else {
                        startDate.setText(String.format(" %d년 0%d월 %d일", mYear, mMonth + 1, mDay));
                        startday = String.format("%d-0%d-%d", mYear, mMonth + 1, mDay);
                    }
                } else {
                    if(mDay < 10) {
                        startDate.setText(String.format(" %d년 %d월 0%d일", mYear, mMonth + 1, mDay));
                        startday = String.format("%d-%d-0%d", mYear, mMonth + 1, mDay);
                    } else {
                        startDate.setText(String.format(" %d년 %d월 %d일", mYear, mMonth + 1, mDay));
                        startday = String.format("%d-%d-%d", mYear, mMonth + 1, mDay);
                    }
                }
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ModifyActivity.this, R.style.DialogTheme, mDateSetListener, mYear, mMonth, mDay).show();
            }

            public DatePickerDialog.OnDateSetListener mDateSetListener =
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            UpdateNow();
                        }
                    };

            void UpdateNow() {
                Toast toast3 = new Toast(ModifyActivity.this);
                View toastView3 = (View) View.inflate(ModifyActivity.this, R.layout.toast, null);
                TextView toastText3 = (TextView) toastView3.findViewById(R.id.toast1);
                toastText3.setText("잘못된 선택입니다.");
                toast3.setView(toastView3);

                if (mMonth < 9) {
                    if (mYear < Y) {
                        toast3.show();
                    } else if (mYear == Y && mMonth + 1 < M) {
                        toast3.show();
                    } else if (mYear == Y && mMonth + 1 == M && mDay < D) {
                        toast3.show();
                    } else if (mDay < 10) {
                        endDate.setText(String.format(" %d년 0%d월 0%d일", mYear, mMonth + 1, mDay));
                        endday = String.format("%d-0%d-0%d", mYear, mMonth + 1, mDay);
                    } else {
                        endDate.setText(String.format(" %d년 0%d월 %d일", mYear, mMonth + 1, mDay));
                        endday = String.format("%d-0%d-%d", mYear, mMonth + 1, mDay);
                    }
                } else {
                    if (mYear < Y) {
                        toast3.show();
                    } else if (mYear == Y && mMonth  + 1< M) {
                        toast3.show();
                    } else if (mYear == Y && mMonth + 1 == M && mDay < D) {
                        toast3.show();
                    } else if (mDay < 10) {
                        endDate.setText(String.format(" %d년 %d월 0%d일", mYear, mMonth + 1, mDay));
                        endday = String.format("%d-%d-0%d", mYear, mMonth + 1, mDay);
                    } else {
                        endDate.setText(String.format(" %d년 %d월 %d일", mYear, mMonth + 1, mDay));
                        endday = String.format("%d-%d-%d", mYear, mMonth + 1, mDay);
                    }
                }
            }
        });

        final Button[] day = new Button[7];
        final Integer[] dayID = {R.id.mon, R.id.tue, R.id.wed, R.id.thu, R.id.fri, R.id.sat, R.id.sun};
        int i;
        day_array = new Integer[7];

        for(i = 0; i < dayID.length; i++) {
            day[i] = findViewById(dayID[i]);
        }

        for(i = 0; i < 7; i++) {
            day_array[i] = DAY_FALSE;
        }

        day_array[0] = iMon;
        day_array[1] = iTue;
        day_array[2] = iWed;
        day_array[3] = iThu;
        day_array[4] = iFri;
        day_array[5] = iSat;
        day_array[6] = iSun;

        for (i = 0; i < 7; i++) {
            if (day_array[i] == 1) {
                day[i].setBackgroundResource(R.drawable.primary_border_fill_4);
                day[i].setTextColor(Color.WHITE);
            }
        }

        for(i = 0; i < dayID.length; i++) {
            final int index;
            index = i;
            day[index].setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                public void onClick(View view) {
                    if (day[index].getCurrentTextColor() == Color.BLACK) {
                        day[index].setBackgroundResource(R.drawable.primary_border_fill_4);
                        day[index].setTextColor(Color.WHITE);
                        day_array[index] = DAY_TRUE;
                    }
                    else{
                        day[index].setBackgroundResource(R.drawable.primary_border_fill3);
                        day[index].setTextColor(Color.BLACK);
                        day_array[index] = DAY_FALSE;
                    }
                }
            });
        }

        timelayout1 = findViewById(R.id.timelayout1);
        timelayout2 = findViewById(R.id.timelayout2);
        timelayout3 = findViewById(R.id.timelayout3);
        timelayout4 = findViewById(R.id.timelayout4);
        timelayout5 = findViewById(R.id.timelayout5);

        final Button[] timeSet = new Button[5];
        final Integer[] timeSetID = {R.id.modi1, R.id.modi2, R.id.modi3, R.id.modi4, R.id.modi5};
        int j;
        for(j = 0; j < timeSetID.length; j++) {
            timeSet[j] = (Button) findViewById(timeSetID[j]);
        }

        final View[] timePick = new View[5];
        final Integer[] timePickID = {R.layout.timepicker1, R.layout.timepicker2, R.layout.timepicker3, R.layout.timepicker4, R.layout.timepicker5};
        final TimePicker[] times = new TimePicker[5];
        final Integer[] timesID = {R.id.timepicker1, R.id.timepicker2, R.id.timepicker3, R.id.timepicker4, R.id.timepicker5};

        final Integer[] tvID = {R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5};
        int k;
        for (k = 0; k < timePickID.length; k++) {
            timePick[k] = View.inflate(ModifyActivity.this, timePickID[k], null);
            times[k] = timePick[k].findViewById(timesID[k]);
            tv[k] = findViewById(tvID[k]);
        }

        time_array = new String[5];
        for(k = 0; k < 5; k ++) {
            time_array[k] = null;
        }

        time_array[0] = one;
        time_array[1] = two;
        time_array[2]= three;
        time_array[3] = four;
        time_array[4] = five;

        LinearLayout[] timelayout = {timelayout1, timelayout2, timelayout3, timelayout4, timelayout5};
        for(int o = 0; o < mTimes; o++){
            if(Integer.parseInt(time_array[o].substring(0, 2)) < 12){
                tv[o].setText("오전 " + time_array[o].substring(0,2) + " : "
                        + time_array[o].substring(3, 5));
                timelayout[o].setVisibility(View.VISIBLE);
            } else if(Integer.parseInt(time_array[o].substring(0, 2)) > 12){
                int n = Integer.parseInt(time_array[o].substring(0,2));
                if(n - 12 <10) {
                    tv[o].setText("오후 0" + Integer.toString(n - 12) + " : " + time_array[o].substring(3,5));
                    timelayout[o].setVisibility(View.VISIBLE);
                } else{
                    tv[o].setText("오후 " + Integer.toString(n - 12) + " : " + time_array[o].substring(3,5));
                    timelayout[o].setVisibility(View.VISIBLE);
                }
            } else {
                tv[o].setText("오후 " + time_array[o].substring(0,2) + " : " + time_array[o].substring(3, 5));
                timelayout[o].setVisibility(View.VISIBLE);
            }
        }

        for(k = 0; k < timePickID.length; k++) {
            final int index;
            index = k;
            timeSet[index].setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                public void onClick(View view) {
                    AlertDialog.Builder dlg1 = new AlertDialog.Builder(ModifyActivity.this, R.style.DialogTheme);
                    dlg1.setTitle("시간선택");
                    if (timePick[index].getParent() != null) {
                        ((ViewGroup) timePick[index].getParent()).removeView(timePick[index]);
                    }
                    times[index].setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
                    dlg1.setView(timePick[index]);
                    dlg1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (times[index].getCurrentHour() < 12) {
                                if(times[index].getCurrentHour() <10){
                                    if(times[index].getCurrentMinute() < 10){
                                        tv[index].setText("오전 " + 0 + Integer.toString(times[index].getCurrentHour()) + " : " +
                                                0 + Integer.toString(times[index].getCurrentMinute()));
                                        timeSet[index].setText("수정");
                                        time_array[index] = 0 + Integer.toString(times[index].getCurrentHour()) + ":" +
                                                0 + Integer.toString(times[index].getCurrentMinute());
                                    } else {
                                        tv[index].setText("오전 " + 0 + Integer.toString(times[index].getCurrentHour()) + " : "
                                                + Integer.toString(times[index].getCurrentMinute()));
                                        timeSet[index].setText("수정");
                                        time_array[index] = 0 + Integer.toString(times[index].getCurrentHour()) + ":"
                                                + Integer.toString(times[index].getCurrentMinute());
                                    }
                                } else {
                                    if(times[index].getCurrentMinute() < 10){
                                        tv[index].setText("오전 " + Integer.toString(times[index].getCurrentHour()) + " : "
                                                + 0 + Integer.toString(times[index].getCurrentMinute()));
                                        timeSet[index].setText("수정");
                                        time_array[index] = Integer.toString(times[index].getCurrentHour()) + ":"
                                                + 0 + Integer.toString(times[index].getCurrentMinute());
                                    } else {
                                        tv[index].setText("오전 " + Integer.toString(times[index].getCurrentHour()) + " : "
                                                + Integer.toString(times[index].getCurrentMinute()));
                                        timeSet[index].setText("수정");
                                        time_array[index] = Integer.toString(times[index].getCurrentHour()) + ":"
                                                + Integer.toString(times[index].getCurrentMinute());
                                    }
                                }
                            } else {
                                if(times[index].getCurrentHour() - 12 < 10){
                                    if(times[index].getCurrentMinute() < 10) {
                                        tv[index].setText("오후 " + 0 + Integer.toString(times[index].getCurrentHour() - 12) + " : "
                                                + 0 + Integer.toString(times[index].getCurrentMinute()));
                                        timeSet[index].setText("수정");
                                        time_array[index] = Integer.toString(times[index].getCurrentHour()) + ":"
                                                + 0 + Integer.toString(times[index].getCurrentMinute());
                                    } else {
                                        tv[index].setText("오후 " + 0 + Integer.toString(times[index].getCurrentHour() - 12) + " : "
                                                + Integer.toString(times[index].getCurrentMinute()));
                                        timeSet[index].setText("수정");
                                        time_array[index] = Integer.toString(times[index].getCurrentHour()) + ":"
                                                + Integer.toString(times[index].getCurrentMinute());
                                    }
                                } else if(times[index].getCurrentMinute() < 10){
                                    tv[index].setText("오후 " + Integer.toString(times[index].getCurrentHour() - 12) + " : "
                                            + 0 + Integer.toString(times[index].getCurrentMinute()));
                                    timeSet[index].setText("수정");
                                    time_array[index] = Integer.toString(times[index].getCurrentHour()) + ":"
                                            + 0 + Integer.toString(times[index].getCurrentMinute());
                                } else {
                                    tv[index].setText("오후 " + Integer.toString(times[index].getCurrentHour() - 12) + " : "
                                            + Integer.toString(times[index].getCurrentMinute()));
                                    timeSet[index].setText("수정");
                                    time_array[index] = Integer.toString(times[index].getCurrentHour()) + ":"
                                            + Integer.toString(times[index].getCurrentMinute());
                                }
                            }
                        }
                    });
                    dlg1.setNegativeButton("취소", null);
                    dlg1.show();
                }
            });
        }

        name.setText(mName);
        number.setText("1일 " + mTimes + "회 복용");

        timeSpinner = findViewById(R.id.time_spinner);
        timeAdapter = ArrayAdapter.createFromResource(this, R.array.takePerDay, android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setTimeLineEditor(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        timeSpinner.setSelection(mTimes-1);     //index 차이

        btn_inPhoto = (Button) findViewById(R.id.btn_inPhoto);


        btn_inPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpPhoto.class);
                startActivityForResult(intent, GET_CROP_IMAGE_PATH);
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name == null || startday == null || endday == null ||
                        (time_array[0] == null && time_array[1] == null && time_array[2] == null &&
                                time_array[3] == null && time_array[4] == null)
                        || (day_array[0] == DAY_FALSE && day_array[1] == DAY_FALSE && day_array[2] == DAY_FALSE && day_array[3] == DAY_FALSE
                        && day_array[4] == DAY_FALSE && day_array[5] == DAY_FALSE && day_array[6] == DAY_FALSE)){
                    Toast toast1 = new Toast(ModifyActivity.this);
                    View toastView1 = View.inflate(ModifyActivity.this, R.layout.toast, null);
                    TextView toastText1 = toastView1.findViewById(R.id.toast1);
                    toastText1.setText("모든 항목을 입력하세요");
                    toast1.setView(toastView1);
                    toast1.show();
                } else if (timesPerDay == null) {
                    timesPerDay = mTimes;
                } else {
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("UPDATE medi SET mediName = '" + name.getText().toString() + "', " +
                            "startDate = '" + startday + "', endDate = '" + endday + "', " +
                            "timesPerDay = " + timesPerDay + ", mon = " + day_array[0] + ", " +
                            "tue = " + day_array[1] + ", wed = " + day_array[2] + ", " +
                            "thu = " + day_array[3] + ", fri = " + day_array[4] + ", " +
                            "sat = " + day_array[5] + ", sun = " + day_array[6] + ", photo_id = '" +
                            filePath + "' " +
                            " WHERE mediId = " + MediId + ";");

                    sqlDB.execSQL("UPDATE time SET oneTime = '" + time_array[0] + "', " +
                            "twoTime = '" + time_array[1] + "', threeTime = '" + time_array[2] + "', " +
                            "fourTime = '" + time_array[3] + "', fiveTime = '" + time_array[4] + "' " +
                            "WHERE timeId = " + MediId + ";");



                    myHelper.deleteDoneByMediIdNow(MediId);

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar targetCal = null;
                    Date finalDate = null;
                    Calendar finalCal = null;

                    try {
                        targetCal = Calendar.getInstance();

                        finalDate = df.parse(endday);
                        finalCal = Calendar.getInstance();
                        finalCal.setTime(finalDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    while(targetCal.before(finalCal) || targetCal.equals(finalCal)){
                        int targetDay = targetCal.get(Calendar.DAY_OF_WEEK);

                        switch(targetDay){
                            case 1:     //일
                                insertDayToDone(MediId, df.format(targetCal.getTime()), WEEK_SUN);
                                break;
                            case 2:     //월
                                insertDayToDone(MediId, df.format(targetCal.getTime()), WEEK_MON);
                                break;
                            case 3:     //화
                                insertDayToDone(MediId, df.format(targetCal.getTime()), WEEK_TUE);
                                break;
                            case 4:     //수
                                insertDayToDone(MediId, df.format(targetCal.getTime()), WEEK_WED);
                                break;
                            case 5:     //목
                                insertDayToDone(MediId, df.format(targetCal.getTime()), WEEK_THU);
                                break;
                            case 6:     //금
                                insertDayToDone(MediId, df.format(targetCal.getTime()), WEEK_FRI);
                                break;
                            case 7:     //토
                                insertDayToDone(MediId, df.format(targetCal.getTime()), WEEK_SAT);
                                break;

                        }
                        targetCal.add(Calendar.DATE,1);
                    }

                    sqlDB.close();
                    startService(new Intent(ModifyActivity.this, MyService.class));
                    finish();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);

        if(resultCode != RESULT_OK)
            return;

        switch(requestCode) {
            case GET_CROP_IMAGE_PATH:
                String str = data.getStringExtra("filePath");

                if(str != "" && str != null){
                    this.prefilePath = this.filePath;
                    this.filePath = data.getStringExtra("filePath");
                    Log.d("result file path URI", filePath);

                    if(this.prefilePath != null && this.prefilePath.trim() != ""){
                        File file = new File(prefilePath);
                        if(file.exists())
                            file.delete();
                    }

                }
                break;
        }
    }

    public boolean setTimeLineEditor(int times) {
        timesPerDay = null;
        switch (times+1) {
            case 1:
                number.setText("1일 1회 복용");
                timesPerDay = 1;
                timelayout1.setVisibility(View.VISIBLE);
                timelayout2.setVisibility(View.GONE);
                time_array[1] = null;
                timelayout3.setVisibility(View.GONE);
                time_array[2] = null;
                timelayout4.setVisibility(View.GONE);
                time_array[3] = null;
                timelayout5.setVisibility(View.GONE);
                time_array[4] = null;
                break;
            case 2:
                number.setText("1일 2회 복용");
                timesPerDay = 2;
                timelayout1.setVisibility(View.VISIBLE);
                timelayout2.setVisibility(View.VISIBLE);
                timelayout3.setVisibility(View.GONE);
                time_array[2] = null;
                timelayout4.setVisibility(View.GONE);
                time_array[3] = null;
                timelayout5.setVisibility(View.GONE);
                time_array[4] = null;
                break;
            case 3:
                number.setText("1일 3회 복용");
                timesPerDay = 3;
                timelayout1.setVisibility(View.VISIBLE);
                timelayout2.setVisibility(View.VISIBLE);
                timelayout3.setVisibility(View.VISIBLE);
                timelayout4.setVisibility(View.GONE);
                time_array[3] = null;
                timelayout5.setVisibility(View.GONE);
                time_array[4] = null;
                break;
            case 4:
                number.setText("1일 4회 복용");
                timesPerDay = 4;
                timelayout1.setVisibility(View.VISIBLE);
                timelayout2.setVisibility(View.VISIBLE);
                timelayout3.setVisibility(View.VISIBLE);
                timelayout4.setVisibility(View.VISIBLE);
                timelayout5.setVisibility(View.GONE);
                time_array[4] = null;
                break;
            case 5:
                number.setText("1일 5회 복용");
                timesPerDay = 5;
                timelayout1.setVisibility(View.VISIBLE);
                timelayout2.setVisibility(View.VISIBLE);
                timelayout3.setVisibility(View.VISIBLE);
                timelayout4.setVisibility(View.VISIBLE);
                timelayout5.setVisibility(View.VISIBLE);
                break;
        }

        return false;
    }

    public void insertDayToDone(int mediId, String date, int day){
        if(day_array[day] == DAY_TRUE){
            for(String time : time_array){
                if(time != null)
                    myHelper.insertDone(mediId, date, time);
            }
        }
    }
}
