package com.dailypaymentapp.cashbox.Models;


public class Time {

    private long last_time;
    private long end_time;
    private int isTimerRunning;
    private int isAdClicked;
    private int impression;
    private  int invalid_activity;

    public Time() {

    }

    public Time(long last_time, long end_time, int isTimerRunning, int isAdClicked, int impression, int invalid_activity) {
        this.last_time = last_time;
        this.end_time = end_time;
        this.isTimerRunning = isTimerRunning;
        this.isAdClicked = isAdClicked;
        this.impression = impression;
        this.invalid_activity = invalid_activity;
    }

    public long getLast_time() {
        return last_time;
    }

    public void setLast_time(long last_time) {
        this.last_time = last_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public int getIsTimerRunning() {
        return isTimerRunning;
    }

    public void setIsTimerRunning(int isTimerRunning) {
        this.isTimerRunning = isTimerRunning;
    }

    public int getIsAdClicked() {
        return isAdClicked;
    }

    public void setIsAdClicked(int isAdClicked) {
        this.isAdClicked = isAdClicked;
    }

    public int getImpression() {
        return impression;
    }

    public void setImpression(int impression) {
        this.impression = impression;
    }

    public int getInvalid_activity() {
        return invalid_activity;
    }

    public void setInvalid_activity(int invalid_activity) {
        this.invalid_activity = invalid_activity;
    }
}
