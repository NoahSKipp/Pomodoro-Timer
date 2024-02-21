package Classes;

public class TimerSettings {
    private int pomodoroLength;
    private int shortBreakLength;
    private int longBreakLength;

    public TimerSettings(int pomodoroLength, int shortBreakLength, int longBreakLength) {
        this.pomodoroLength = pomodoroLength;
        this.shortBreakLength = shortBreakLength;
        this.longBreakLength = longBreakLength;
    }

    // Getter methods for accessing timer lengths
    public int getPomodoroLength() {
        return pomodoroLength;
    }

    public int getShortBreakLength() {
        return shortBreakLength;
    }

    public int getLongBreakLength() {
        return longBreakLength;
    }

    // Setter methods if needed
    public void setPomodoroLength(int pomodoroLength) {
        this.pomodoroLength = pomodoroLength;
    }

    public void setShortBreakLength(int shortBreakLength) {
        this.shortBreakLength = shortBreakLength;
    }

    public void setLongBreakLength(int longBreakLength) {
        this.longBreakLength = longBreakLength;
    }
}
