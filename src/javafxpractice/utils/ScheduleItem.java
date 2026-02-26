package javafxpractice.utils;
import java.time.Duration;
import java.time.LocalDate; // import the LocalDate class
import java.time.LocalTime; // import the LocalTime class

public class ScheduleItem {
	private LocalDate scheduledDate;
	private LocalTime scheduledTime;
	private String task;
	private Duration durationOfTask;
	
	public ScheduleItem(LocalDate sD, LocalTime sT, String t, Duration dOT) {
		scheduledDate = sD;
		scheduledTime = sT;
		task = t;
		durationOfTask = dOT;
	}
	
	@Override
	public String toString() {
	    return "ScheduleItem{" +
	            "name='" + task + '\'' +
	            ", date=" + scheduledDate +
	            ", time=" + scheduledTime +
	            ", duration=" + durationOfTask +
	            '}';
	}
	
	
}
