package javafxpractice.utils;
import java.util.ArrayList;

import javafxpractice.utils.ScheduleItem;

public class ScheduleDB {
	
	private ArrayList<ScheduleItem> userSchedule;
	
	
	public ScheduleDB() {
        this.userSchedule = new ArrayList<>();
    }

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder("ScheduleDB:\n");

	    for (ScheduleItem item : userSchedule) {
	        sb.append(" - ").append(item).append("\n");
	    }

	    return sb.toString();
	}

	
	public void addNewItem(ScheduleItem item) {
		userSchedule.add(item);
	}
	
	public void removeItem(ScheduleItem item) {
		userSchedule.remove(item);
	}

}
