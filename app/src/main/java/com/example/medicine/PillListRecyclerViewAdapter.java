package com.example.medicine;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class PillListRecyclerViewAdapter  extends RecyclerView.Adapter<PillListRecyclerViewAdapter.ItemViewHolder> {
    protected ArrayList<PillList> pList;
    protected LayoutInflater pInflate;
    protected Context context;
    protected SparseBooleanArray selectedItems = new SparseBooleanArray();
    protected int prePosition = -1;
    protected String[] time_array;
    protected int times;
    protected String oneTime, twoTime, threeTime, fourTime, fiveTime;
    protected ArrayList<String> photo_ids = new ArrayList<>();
    protected View view;
    protected MyDBHelper dbHelper;
    protected int id;
    protected final int PILL_DONE = 1, PILL_NOT_DONE = 0;
    protected HashMap<String, Integer> timeLine;
    protected boolean[] isTimeExist;
    protected ArrayList<ArrayList<DoneItem>> doneItemList =  new ArrayList<>();
    protected ArrayList<DoneItem> doneItems;

    public PillListRecyclerViewAdapter(){
    }
    public PillListRecyclerViewAdapter(ArrayList<PillList> pList, Context context) {
        this.pList = pList;
        this.pInflate = LayoutInflater.from(context);
        this.context = context;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pill_item_list, parent, false);
        return new ItemViewHolder(view);
    }
    public void callHolderBind(@NonNull final ItemViewHolder holder, final int position){
        holder.onBind(pList.get(position), position);
    }
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        timeLine = new HashMap<>();
        dbHelper = new MyDBHelper(context);
        id = pList.get(position).getMediId();
        doneItems = getDoneList();      //해당하는 약의 Done List 만 담김(아이템 클릭시 값이 변경됨 따라서 List에 따로 담고 position으로 인덱싱)
        doneItemList.add(doneItems);    //해당 날짜 전체약의 Done Item List
        photo_ids.add(pList.get(position).getPhotoId());
        callHolderBind(holder, position);
        for(int i = 0; i < time_array.length; i++){
            timeLine.put("btn_check" + i, PILL_NOT_DONE);
        }
        setCheckBox(doneItemList.get(position), holder, position);
        setCheckBoxListener(holder);
    }

    public ArrayList<DoneItem> getDoneList(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return dbHelper.getDoneByMediId(id, df.format(Calendar.getInstance().getTime()));
    }
    public void setCheckBox(ArrayList<DoneItem> doneItems, final ItemViewHolder holder, final int position){
        isTimeExist = new boolean[time_array.length];
        Arrays.fill(isTimeExist, false);

        if(doneItems != null) {
            for(int i=0; i < doneItems.size(); i++){
                isTimeExist[i] = true;
                for(int j = 1; j <= time_array.length; j++){
                    int resId = holder.itemView.getResources().getIdentifier("btn_check" + j, "id", context.getPackageName());
                    CheckBox btn = holder.itemView.findViewById(resId);
                    if(doneItems.get(i).getTime().equals(time_array[j-1]) && doneItems.get(i).getIsDone() == PILL_DONE){
                        btn.setChecked(true);
                        timeLine.put("btn_check" + j, doneItems.get(i).getDoneId());
                    }else if(doneItems.get(i).getTime().equals(time_array[j-1])){
                        btn.setChecked(false);
                        timeLine.put("btn_check" + j, doneItems.get(i).getDoneId());
                    }else{
                    }

                }
            }
            hideTimeLine(holder);   //done목록보다 큰 인덱스의 타임라인은 숨김
        }
    }
    public void setCheckBoxListener(final ItemViewHolder holder){
        holder.btn_check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedHandle(isChecked, "btn_check1");
            }
        });
        holder.btn_check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedHandle(isChecked, "btn_check2");
            }
        });
        holder.btn_check3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedHandle(isChecked, "btn_check3");
            }
        });
        holder.btn_check4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedHandle(isChecked, "btn_check4");
            }
        });
        holder.btn_check5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedHandle(isChecked, "btn_check5");
            }
        });
    }
    public void hideTimeLine(final ItemViewHolder holder){
        for(int i = 0; i < isTimeExist.length; i++){
            if(!isTimeExist[i]){
                int number = i + 1;
                Log.d("number", Integer.toString(number));
                int resId = holder.itemView.getResources().getIdentifier("lay" + number, "id", context.getPackageName());
                LinearLayout lay = holder.itemView.findViewById(resId);
                Log.d("number", lay.toString());
                lay.setVisibility(View.GONE);
            }
        }
    }
    public void checkedHandle(boolean isChecked, String hashId){
        int doneId = -1;
        if (isChecked) {
            doneId = timeLine.get(hashId);
            dbHelper.setDone(doneId, PILL_DONE);
        } else {
            doneId = timeLine.get(hashId);
            dbHelper.setDone(doneId, PILL_NOT_DONE);
        }
    }
    @Override
    public int getItemCount() {
        return pList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView txt_pill_item_list_mediName;
        protected TextView txt_pill_item_list_time1, txt_pill_item_list_time2, txt_pill_item_list_time3,
                txt_pill_item_list_time4, txt_pill_item_list_time5;
        protected ImageView btn_clock;
        protected CheckBox btn_check1, btn_check2, btn_check3, btn_check4, btn_check5;
        protected PillList pillList;
        protected int position;
        protected LinearLayout lay1, lay2, lay3, lay4, lay5;
        protected View itemView;
        protected boolean isDoneBind = false;
        protected String photo_path = new String();

        ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            txt_pill_item_list_mediName = itemView.findViewById(R.id.txt_pill_item_list_mediName);
            txt_pill_item_list_time1 = itemView.findViewById(R.id.txt_pill_item_list_time1);
            txt_pill_item_list_time2 = itemView.findViewById(R.id.txt_pill_item_list_time2);
            txt_pill_item_list_time3 = itemView.findViewById(R.id.txt_pill_item_list_time3);
            txt_pill_item_list_time4 = itemView.findViewById(R.id.txt_pill_item_list_time4);
            txt_pill_item_list_time5 = itemView.findViewById(R.id.txt_pill_item_list_time5);

            btn_clock = itemView.findViewById(R.id.btn_clock);
            btn_check1 = itemView.findViewById(R.id.btn_check1);
            btn_check2 = itemView.findViewById(R.id.btn_check2);
            btn_check3 = itemView.findViewById(R.id.btn_check3);
            btn_check4 = itemView.findViewById(R.id.btn_check4);
            btn_check5 = itemView.findViewById(R.id.btn_check5);

            lay1 = itemView.findViewById(R.id.lay1);
            lay2 = itemView.findViewById(R.id.lay2);
            lay3 = itemView.findViewById(R.id.lay3);
            lay4 = itemView.findViewById(R.id.lay4);
            lay5 = itemView.findViewById(R.id.lay5);
        }
        public void setTimeLineTextView(String[] time_array, int times){
            TextView[] pill = new TextView[5];

            pill[0] = txt_pill_item_list_time1;
            pill[1] = txt_pill_item_list_time2;
            pill[2] = txt_pill_item_list_time3;
            pill[3] = txt_pill_item_list_time4;
            pill[4] = txt_pill_item_list_time5;

            for(int i = 0; i < times; i++) {
                if (time_array[i] != null) {
                    if (Integer.parseInt(time_array[i].substring(0, 2)) < 12) {
                        pill[i].setText("오전 " + time_array[i].substring(0, 2) + " : "
                                + time_array[i].substring(3, 5));
                    } else if (Integer.parseInt(time_array[i].substring(0, 2)) > 12) {
                        int n = Integer.parseInt(time_array[i].substring(0, 2));
                        if (n - 12 < 10) {
                            pill[i].setText("오후 0" + Integer.toString(n - 12) + " : " + time_array[i].substring(3, 5));
                        } else {
                            pill[i].setText("오후 " + Integer.toString(n - 12) + " : " + time_array[i].substring(3, 5));
                        }
                    } else {
                        pill[i].setText("오후 " + time_array[i].substring(0, 2) + " : " + time_array[i].substring(3, 5));
                    }
                }
            }
        }
        void onBind(ArrayList<DoneItem> donelist, int position){
            this.pillList = pList.get(position);
            this.position = position;
            this.isDoneBind = true;

            time_array = new String[5];

            for(int i = 0; i < donelist.size(); i++){
                time_array[i] = donelist.get(i).getTime();
            }


            times = donelist.size();
            time_array = new String[5];
            String[] str = {oneTime, twoTime, threeTime, fourTime, fiveTime};
            for(int i = 0; i < donelist.size() && i < 5; i++){
                str[i] = donelist.get(i).getTime();
                time_array[i] = str[i];
            }
            Log.d("time_array", time_array.toString());

            setTimeLineTextView(time_array, times);
            setListener();

            String path = photo_ids.get(position);
            Log.d("photo_path position", Integer.toString(position));
            if(path != null && path.trim() != ""){
                setImageView(path);
            }else{
                btn_clock.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_alarm_pill));
            }
        }
        void onBind(PillList pillList, int position) {
            this.pillList = pillList;
            this.position = position;

            times = pillList.getTimesPerDay();
            oneTime = pillList.getOneTime();
            twoTime = pillList.getTwoTime();
            threeTime = pillList.getThreeTime();
            fourTime = pillList.getFourTime();
            fiveTime = pillList.getFiveTime();

            time_array = new String[5];
            time_array[0] = oneTime;
            time_array[1] = twoTime;
            time_array[2] = threeTime;
            time_array[3] = fourTime;
            time_array[4] = fiveTime;
            Log.d("time_array", time_array.toString());

            setTimeLineTextView(time_array, times);
            setListener();

            String path = photo_ids.get(position);
            Log.d("photo_path position", Integer.toString(position));
            if(path != null && path.trim() != ""){
                setImageView(path);
            }else{
                btn_clock.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_alarm_pill));
            }
        }
        public void setImageView(String path){
            photo_path = path;
            Log.d("photo_path", photo_path);
            File file = new File(photo_path);
            ImageView[] iv = new ImageView[1];
            iv[0] = btn_clock;
            if(file.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                iv[0].setImageBitmap(bitmap);
            }
        }
        public void setListener(){
            txt_pill_item_list_mediName.setText(pillList.getMediName());
            changeVisibility(selectedItems.get(position));

            itemView.setOnClickListener(this);
            txt_pill_item_list_mediName.setOnClickListener(this);
            txt_pill_item_list_time1.setOnClickListener(this);
            txt_pill_item_list_time2.setOnClickListener(this);
            txt_pill_item_list_time3.setOnClickListener(this);
            txt_pill_item_list_time4.setOnClickListener(this);
            txt_pill_item_list_time5.setOnClickListener(this);
            btn_clock.setOnClickListener(this);

            lay1.setOnClickListener(this);
            lay2.setOnClickListener(this);
            lay3.setOnClickListener(this);
            lay4.setOnClickListener(this);
            lay5.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lay_pill:
                    if (selectedItems.get(position)) {
                        selectedItems.delete(position);
                    } else {
                        selectedItems.put(position, true);
                    }
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    prePosition = position;
                    break;
                case R.id.btn_clock:
                    if (selectedItems.get(position)) {
                        selectedItems.delete(position);
                    } else {
                        selectedItems.put(position, true);
                    }
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    prePosition = position;
                    break;
                case R.id.txt_pill_item_list_mediName:
                    if (selectedItems.get(position)) {
                        selectedItems.delete(position);
                    } else {
                        selectedItems.put(position, true);
                    }
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    prePosition = position;
                    break;
            }
        }
        private void changeVisibility(final boolean isExpanded) {
            int dpValue = 50;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            va.setDuration(150);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    int times = pillList.getTimesPerDay();
                    if (isDoneBind)
                        times = doneItemList.get(position).size();
                    LinearLayout[] layout = {lay1, lay2, lay3, lay4, lay5};
                    for(int i = 0; i < times; i++){
                        layout[i].getLayoutParams().height = value;
                        layout[i].requestLayout();
                        layout[i].setVisibility(isExpanded ? isTimeExist[0] ? View.VISIBLE : View.GONE : View.GONE);
                    }
                }
            });
            va.start();
        }

    }
}
