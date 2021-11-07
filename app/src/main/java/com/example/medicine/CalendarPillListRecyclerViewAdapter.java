package com.example.medicine;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarPillListRecyclerViewAdapter extends PillListRecyclerViewAdapter{

    private ItemViewHolder itemHolder;
    private Date date;
    private Button[] lineButton;
    private CheckBox[] checkBox;
    private String[] checkBoxId = {"btn_check1", "btn_check2", "btn_check3", "btn_check4", "btn_check5"};

    public CalendarPillListRecyclerViewAdapter(){
        super();
    }
    public CalendarPillListRecyclerViewAdapter(ArrayList<PillList> pList, Context context, Date date) {
        super(pList, context);
        this.context = context;
        this.date = date;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_pill_item_list, parent, false);
        itemHolder = new ItemViewHolder(view);
        lineButton = new Button[checkBoxId.length];
        lineButton[0] = view.findViewById(R.id.btn_line1);
        lineButton[1] = view.findViewById(R.id.btn_line2);
        lineButton[2] = view.findViewById(R.id.btn_line3);
        lineButton[3] = view.findViewById(R.id.btn_line4);
        lineButton[4] = view.findViewById(R.id.btn_line5);
        checkBox = new CheckBox[checkBoxId.length];
        checkBox[0] = view.findViewById(R.id.btn_check1);
        checkBox[1] = view.findViewById(R.id.btn_check2);
        checkBox[2] = view.findViewById(R.id.btn_check3);
        checkBox[3] = view.findViewById(R.id.btn_check4);
        checkBox[4] = view.findViewById(R.id.btn_check5);
        return itemHolder;
    }
    @Override
    public ArrayList<DoneItem> getDoneList(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return dbHelper.getDoneByMediId(id, df.format(date));
    }
    @Override
    public void callHolderBind(@NonNull final ItemViewHolder holder, int position){
        holder.onBind(doneItemList.get(position), position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        Log.d("onBindViewHolder()", "start");
        super.onBindViewHolder(holder, position);
        hideCheckBox();
        if(date.getTime() > System.currentTimeMillis()){
            for(Button button : lineButton) setButtonLaterStyle(button);
        }
        if(date.getTime() <= System.currentTimeMillis()){
            setButtonListener();
            changeButtonStyle();
        }
    }
    public void setButtonListener(){
        for(int i = 0; i < lineButton.length; i++){
            final int index = i;
            lineButton[i].setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     setAlertDialog(index);
                 }
            });
        }
    }
    public void setAlertDialog(final int index){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_check_title).setMessage(R.string.dialog_check_content);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean checked = checkBox[index].isChecked();
                checkBox[index].setChecked(!checked);
                Toast.makeText(context.getApplicationContext(), "변경 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void setCheckBoxListener(final ItemViewHolder holder){
        for(int i = 0; i < checkBox.length; i++){
            final String id = checkBoxId[i];
            final int index = i;
            checkBox[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkedHandle(isChecked, id);
                    changeButtonStyle(isChecked, lineButton[index]);
                }
            });
        }
    }
    public void changeButtonStyle(){
        for(int i = 0; i < checkBox.length; i++) {
            if(checkBox[i].isChecked())
                setButtonTakestyle(lineButton[i]);
            else
                setButtonNotTakeStyle(lineButton[i]);
        }
    }
    public void changeButtonStyle(boolean isChecked, Button button){
        if(isChecked)
            setButtonTakestyle(button);
        else
            setButtonNotTakeStyle(button);
    }
    public void setButtonLaterStyle(Button button){
        button.setBackgroundColor(context.getResources().getColor(R.color.doneListLaterButton));
        button.setTextColor(context.getResources().getColor(R.color.colorWhite));
        button.setText(R.string.button_take_later);
    }
    public void setButtonTakestyle(Button button){
        button.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        button.setTextColor(context.getResources().getColor(R.color.doneListClickButton));
        button.setText(R.string.button_take);
    }
    public void setButtonNotTakeStyle(Button button){
        button.setBackgroundColor(context.getResources().getColor(R.color.doneListClickButton));
        button.setTextColor(context.getResources().getColor(R.color.colorWhite));
        button.setText(R.string.button_not_take);
    }
    public void hideCheckBox(){
        Log.d("hideCheckBox()", "start");
        itemHolder.btn_check1.setVisibility(View.GONE);
        itemHolder.btn_check2.setVisibility(View.GONE);
        itemHolder.btn_check3.setVisibility(View.GONE);
        itemHolder.btn_check4.setVisibility(View.GONE);
        itemHolder.btn_check5.setVisibility(View.GONE);
    }
}
