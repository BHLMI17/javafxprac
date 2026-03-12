package javafxpractice.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleItem {

    private LocalDate date;
    private LocalTime time;
    private String task;
    private Duration duration;

    public ScheduleItem(LocalDate date, LocalTime time, String task, Duration dOT) {
        this.date = date;
        this.time = time;
        this.task = task;
        this.duration = dOT;
    }

    @Override
    public String toString() {
        return "ScheduleItem{" +
                "task='" + task + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", duration=" + duration +
                '}';
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getTask() {
        return task;
    }

    public Duration getDuration() {
        return duration;
    }
}