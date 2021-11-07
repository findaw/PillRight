package com.example.medicine;

public class DoneItem {
    int doneId;
    int mediId;
    String day;
    String time;
    int isDone;

    public DoneItem(){

    }
    public DoneItem(int doneId, int mediId, String day, String time, int isDone) {
        this.doneId = doneId;
        this.mediId = mediId;
        this.day = day;
        this.time = time;
        this.isDone = isDone;
    }

    public int getDoneId() {
        return doneId;
    }

    public void setDoneId(int doneId) {
        this.doneId = doneId;
    }

    public int getMediId() {
        return mediId;
    }

    public void setMediId(int mediId) {
        this.mediId = mediId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }
}
