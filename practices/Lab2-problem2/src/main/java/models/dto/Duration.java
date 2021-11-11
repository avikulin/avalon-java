package models.dto;

public class Duration {
    private int hours;
    private int minutes;
    private int seconds;

    public Duration(int hours, int minutes, int seconds) throws IllegalStateException {
        setHours(hours);
        setMinutes(minutes);
        setSeconds(seconds);
    }

    public Duration(int minutes, int seconds) throws IllegalStateException {
        setMinutes(minutes);
        setSeconds(seconds);
    }

    public Duration(int seconds) throws IllegalStateException {
        int hoursValue = seconds / 3600;
        int minutesValue = (seconds % 3600) / 60;
        int secondsValue = seconds % 60;
        setHours(hoursValue);
        setMinutes(minutesValue);
        setSeconds(secondsValue);
    }

    public int getHours() {
        return hours;
    }

    private void setHours(int hours) throws IllegalStateException {
        if (hours < 0) {
            throw new IllegalArgumentException("Hours param must be positive value");
        }
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    private void setMinutes(int minutes) throws IllegalStateException {
        if ((minutes < 0) || (minutes > 59)) {
            throw new IllegalArgumentException("Minutes param must be positive integer value less, than 60");
        }
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    private void setSeconds(int seconds) throws IllegalStateException {
        if ((seconds < 0) || (seconds > 59)) {
            throw new IllegalArgumentException("Seconds param must be positive integer value less, than 60");
        }
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        if (hours == 0) {
            return String.format("%02d:%02d", minutes, seconds);
        }
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Duration that = (Duration) o;
        return hours == that.hours &&
                minutes == that.minutes &&
                seconds == that.seconds;
    }

}
