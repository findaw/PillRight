package com.example.medicine;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class MyDBHelper extends SQLiteOpenHelper {

    public MyDBHelper(Context context) {
        super(context, "pillright.db", null, 1);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.disableWriteAheadLogging();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS medi (mediId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, mediName TEXT NOT NULL, " +
                "startDate TEXT NOT NULL,  endDate TEXT NOT NULL,  timesPerDay INTEGER NOT NULL," +
                "mon INTEGER, tue INTEGER, wed INTEGER, thu INTEGER," +
                "fri INTEGER, sat INTEGER, sun INTEGER, photo_id TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS time (timeId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, oneTime TEXT NOT NULL, twoTime TEXT, threeTime TEXT, " +
                "fourTime TEXT, fiveTime TEXT, " +
                "FOREIGN KEY(timeId) REFERENCES medi(mediId));");

        db.execSQL("CREATE TABLE IF NOT EXISTS done (doneId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, mediId INTEGER NOT NULL,"
                + "day TEXT NOT NULL,"
                + "time TEXT NOT NULL,"
                + "isDone INTEGER NOT NULL DEFAULT 0,"
                + "FOREIGN KEY(mediId) REFERENCES medi(mediId));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<ListItem> allListItems() {
        ArrayList<ListItem> listItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor, cursor2;
        cursor = db.rawQuery("SELECT * FROM medi;", null);
        cursor2 = db.rawQuery("SELECT * FROM medi JOIN time ON medi.mediId = time.timeId;", null);

        while (cursor.moveToNext() && cursor2.moveToNext()) {
            ListItem listItem = new ListItem();
            listItem.setMediId(cursor.getInt(0));
            listItem.setMediName(cursor.getString(1));
            listItem.setStartDate(cursor.getString(2));
            listItem.setEndDate(cursor.getString(3));
            listItem.setTimesPerDay(cursor.getInt(4));
            listItem.setMon(cursor.getInt(5));
            listItem.setTue(cursor.getInt(6));
            listItem.setWed(cursor.getInt(7));
            listItem.setThu(cursor.getInt(8));
            listItem.setFri(cursor.getInt(9));
            listItem.setSat(cursor.getInt(10));
            listItem.setSun(cursor.getInt(11));
            listItem.setPhotoId(cursor.getString(12));      //13 : medi ID값 이미 있어서 뺌
            listItem.setOneTime(cursor2.getString(14));
            listItem.setTwoTime(cursor2.getString(15));
            listItem.setThreeTime(cursor2.getString(16));
            listItem.setFourTime(cursor2.getString(17));
            listItem.setFiveTime(cursor2.getString(18));

            listItems.add(listItem);
        }

        cursor.close();
        cursor2.close();
        db.close();

        return listItems;
    }

    public ArrayList<PillList> allPListItems() {
        ArrayList<PillList> pListItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor, cursor2;
        cursor = db.rawQuery("SELECT * FROM medi WHERE endDate >= date('now');", null);
        cursor2 = db.rawQuery("SELECT * FROM medi JOIN time ON medi.mediId = time.timeId  WHERE endDate >= date('now');" , null);

        String now = "";
        Calendar calNow = Calendar.getInstance();
        int numNow = calNow.get(Calendar.DAY_OF_WEEK);
        switch (numNow) {
            case 1:
                now = "일";
                break ;
            case 2:
                now = "월";
                break ;
            case 3:
                now = "화";
                break ;
            case 4:
                now = "수";
                break ;
            case 5:
                now = "목";
                break ;
            case 6:
                now = "금";
                break ;
            case 7:
                now = "토";
                break ;
        }

        while (cursor.moveToNext() && cursor2.moveToNext()) {
            boolean flag = false;
            if (now.equals("일") && cursor.getInt(11) == 1) {
                flag = true;
                Log.e("일:", cursor.getString(1));
            } else if (now.equals("월") && cursor.getInt(5) == 1) {
                flag = true;
                Log.e("월:", cursor.getString(1));
            } else if (now.equals("화") && cursor.getInt(6) == 1) {
                flag = true;
                Log.e("화:", cursor.getString(1));
            } else if (now.equals("수") && cursor.getInt(7) == 1) {
                flag = true;
                Log.e("수:", cursor.getString(1));
            } else if (now.equals("목") && cursor.getInt(8) == 1) {
                flag = true;
                Log.e("목:", cursor.getString(1));
            } else if (now.equals("금") && cursor.getInt(9) == 1) {
                flag = true;
                Log.e("금:", cursor.getString(1));
            } else if (now.equals("토") && cursor.getInt(10) == 1) {
                flag = true;
                Log.e("토:", cursor.getString(1));
            }

            if(flag){
                PillList pListItem = new PillList();
                pListItem.setMediId(cursor.getInt(0));
                pListItem.setMediName(cursor.getString(1));
                pListItem.setStartDate(cursor.getString(2));
                pListItem.setEndDate(cursor.getString(3));
                pListItem.setTimesPerDay(cursor.getInt(4));
                pListItem.setPhotoId(cursor.getString(12));
                pListItem.setOneTime(cursor2.getString(14));
                pListItem.setTwoTime(cursor2.getString(15));
                pListItem.setThreeTime(cursor2.getString(16));
                pListItem.setFourTime(cursor2.getString(17));
                pListItem.setFiveTime(cursor2.getString(18));
                pListItems.add(pListItem);
            }

        }

        cursor.close();
        cursor2.close();
        db.close();

        return pListItems;
    }
    public ArrayList<PillList> allPListItems(String option, String targetDate) {
        ArrayList<PillList> pListItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql ="SELECT * FROM medi WHERE startDate <='" + targetDate + "' AND endDate >= '" + targetDate +"';";
        String sql2 = "SELECT * FROM medi JOIN time ON medi.mediId = time.timeId  WHERE startDate <='" + targetDate + "' AND endDate >= '" + targetDate +"';";

        Cursor cursor, cursor2;
        cursor = db.rawQuery(sql, null);
        cursor2 = db.rawQuery(sql2 , null);

        while (cursor.moveToNext() && cursor2.moveToNext()) {
            boolean flag = false;
            if (option.equals("일") && cursor.getInt(11) == 1) {
                flag = true;
                Log.e("일:", cursor.getString(1));
            } else if (option.equals("월") && cursor.getInt(5) == 1) {
                flag = true;
                Log.e("월:", cursor.getString(1));
            } else if (option.equals("화") && cursor.getInt(6) == 1) {
                flag = true;
                Log.e("화:", cursor.getString(1));
            } else if (option.equals("수") && cursor.getInt(7) == 1) {
                flag = true;
                Log.e("수:", cursor.getString(1));
            } else if (option.equals("목") && cursor.getInt(8) == 1) {
                flag = true;
                Log.e("목:", cursor.getString(1));
            } else if (option.equals("금") && cursor.getInt(9) == 1) {
                flag = true;
                Log.e("금:", cursor.getString(1));
            } else if (option.equals("토") && cursor.getInt(10) == 1) {
                flag = true;
                Log.e("토:", cursor.getString(1));
            }

            if(flag){
                PillList pListItem = new PillList();
                pListItem.setMediId(cursor.getInt(0));
                pListItem.setMediName(cursor.getString(1));
                pListItem.setStartDate(cursor.getString(2));
                pListItem.setEndDate(cursor.getString(3));
                pListItem.setTimesPerDay(cursor.getInt(4));
                pListItem.setPhotoId(cursor.getString(12));
                pListItem.setOneTime(cursor2.getString(14));
                pListItem.setTwoTime(cursor2.getString(15));
                pListItem.setThreeTime(cursor2.getString(16));
                pListItem.setFourTime(cursor2.getString(17));
                pListItem.setFiveTime(cursor2.getString(18));
                pListItems.add(pListItem);
            }
        }

        cursor.close();
        cursor2.close();
        db.close();

        return pListItems;
    }

    public int getId(){
        int id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("SELECT mediId FROM medi ORDER BY mediId desc LIMIT 1");
        id = (int) stmt.simpleQueryForLong();
        db.close();

        return id;
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM medi WHERE mediId = '" + id + "';");
        db.execSQL("DELETE FROM time WHERE timeId = '" + id + "';");
        SQLiteStatement stmt = db.compileStatement("DELETE FROM done WHERE mediId = ?");
        stmt.bindLong(1, id);
        stmt.execute();
        db.close();
    }
    public ArrayList<DoneItem> getDoneByMediId(int mediId, String day){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM done WHERE mediId = '" + mediId + "' AND day = '" + day.trim() +"';", null);
        ArrayList<DoneItem> doneList = new ArrayList<>();

        while (cursor.moveToNext()) {
            DoneItem item = new DoneItem();
            item.setDoneId(cursor.getInt(0));
            item.setMediId(cursor.getInt(1));
            item.setDay(cursor.getString(2));
            item.setTime(cursor.getString(3));
            item.setIsDone(cursor.getInt(4));
            doneList.add(item);
        }
        cursor.close();
        db.close();
        return doneList;
    }
    public ArrayList<DoneItem> getDoneByDay(String day){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM done WHERE day = '" + day.trim() +"';", null);
        ArrayList<DoneItem> doneList = new ArrayList<>();
        while (cursor.moveToNext()) {
            DoneItem item = new DoneItem();
            item.setDoneId(cursor.getInt(0));
            item.setMediId(cursor.getInt(1));
            item.setDay(cursor.getString(2));
            item.setTime(cursor.getString(3));
            item.setIsDone(cursor.getInt(4));
            doneList.add(item);
        }

        cursor.close();
        db.close();

        return doneList;
    }


    public void setDone(int doneId, int value){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("UPDATE done SET isDone = ? WHERE doneId = ?");
        stmt.bindLong(1, value);
        stmt.bindLong(2, doneId);
        stmt.execute();
        db.close();
    }

    public int insertDone(int mediId, String day, String time){

        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("INSERT INTO done (mediId, day, time) VALUES(?,?,?)");
        stmt.bindLong(1, mediId);
        stmt.bindString(2, day.trim());
        stmt.bindString(3, time.trim());

        int id = (int) stmt.executeInsert();
        db.close();
        return id;
    }

    public void deleteDoneByMediIdNow(int mediId){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("DELETE FROM done WHERE mediId = ? AND day >= date('now')");
        stmt.bindLong(1, mediId);
        stmt.execute();
        db.close();
    }
    public void deleteDoneByDoneId(int doneId){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("DELETE FROM done WHERE doneId = ?");
        stmt.bindLong(1, doneId);
        stmt.execute();
        db.close();
    }
}
