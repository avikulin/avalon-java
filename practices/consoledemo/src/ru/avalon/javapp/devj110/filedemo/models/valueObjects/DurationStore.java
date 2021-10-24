package ru.avalon.javapp.devj110.filedemo.models.valueObjects;
/*
    Read-only value object.
 */
public class DurationStore {
    private int hours;
    private int minutes;
    private int seconds;

    public DurationStore(int hours, int minutes, int seconds) {
        setHours(hours);
        setMinutes(minutes);
        setSeconds(seconds);
    }

    public void setHours(int hours) {
        if (hours < 0){
            throw new IllegalArgumentException("Hours must be positive value");
        }
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        if ((minutes < 0)&&(minutes > 59)){
            throw new IllegalArgumentException("Minutes must be positive value between 0 and 59");
        }
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        if ((seconds < 0)&&(seconds > 59)){
            throw new IllegalArgumentException("Seconds must be positive value between 0 and 59");
        }
        this.seconds = seconds;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    @Override
    public String toString() {
        return String.format("%d:%d:%d", hours, minutes, seconds);
    }
}
