package javafxpractice.utils;
import java.util.ArrayList;

import javafxpractice.utils.ScheduleItem;

public class ScheduleDB {
	
	private ArrayList<ScheduleItem> userSchedule;
	
	
	public void addNewItem(ScheduleItem item) {
		userSchedule.add(item);
	}
	
	public void removeItem(ScheduleItem item) {
		userSchedule.remove(item);
	}

}
