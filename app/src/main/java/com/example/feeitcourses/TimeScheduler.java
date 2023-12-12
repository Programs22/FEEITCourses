package com.example.feeitcourses;

public class TimeScheduler {
    String hoursFrom, minutesFrom, hoursTo, minutesTo;

    public TimeScheduler() {

    }

    public TimeScheduler(String hoursFrom, String minutesFrom, String hoursTo, String minutesTo) {
        this.hoursFrom = hoursFrom;
        this.minutesFrom = minutesFrom;
        this.hoursTo = hoursTo;
        this.minutesTo = minutesTo;
    }

    public void setHoursFrom(String hoursFrom) {
        this.hoursFrom = hoursFrom;
    }

    public void setMinutesFrom(String minutesFrom) {
        this.minutesFrom = minutesFrom;
    }

    public void setHoursTo(String hoursTo) {
        this.hoursTo = hoursTo;
    }

    public void setMinutesTo(String minutesTo) {
        this.minutesTo = minutesTo;
    }

    public String getHoursFrom() {
        return hoursFrom;
    }

    public String getMinutesFrom() {
        return minutesFrom;
    }

    public String getHoursTo() {
        return hoursTo;
    }

    public String getMinutesTo() {
        return minutesTo;
    }
}
