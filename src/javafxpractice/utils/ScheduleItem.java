package javafxpractice.utils;
import java.time.LocalDate; // import the LocalDate class
import java.time.LocalTime; // import the LocalTime class

public class ScheduleItem {
	private LocalDate scheduledDate;
	private LocalTime scheduledTime;
	private String task;
	private int durationOfTask;
	
	public ScheduleItem(LocalDate sD, LocalTime sT, String t, int dOT) {
		scheduledDate = sD;
		scheduledTime = sT;
		task = t;
		durationOfTask = dOT;
	}
	
	
	
	
}
