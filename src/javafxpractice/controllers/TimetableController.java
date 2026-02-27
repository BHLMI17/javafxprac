package javafxpractice.controllers;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.StringConverter;
import javafxpractice.utils.ScheduleDB;
import javafxpractice.utils.ScheduleItem;

public class TimetableController {

    @FXML private ScrollPane timeScroll;
    @FXML private AnchorPane timetablePane;
    @FXML private Button AddItemButton;
    @FXML private Button BackButton;
    @FXML private VBox scheduleCreationBox;
    @FXML private Button confirmItemButton;
    @FXML private Spinner<LocalTime> timeSpinner;
    @FXML private Spinner<Duration> durationSpinner;
    @FXML private DatePicker datePicker;
    @FXML private TextField itemName;
    private String[] dayName = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private final ScheduleDB db = new ScheduleDB();



    
    private static final int HOURS = 24;
    private static final int DAYS = 7;
    private static final double DAY_WIDTH = 200; // pixels per day
    private static final double HOUR_HEIGHT = 80; // pixels per hour
    
    
    
    
    
    @FXML
    public void initialize() {
    	List<LocalTime> times = new ArrayList<>();
    	
    	for(int hour = 0; hour < 24; hour++) {
    		for(int minute = 0; minute < 60; minute +=15) {
    			times.add(LocalTime.of(hour, minute));
    		}
    	}

        // Make timetablePane resize with ScrollPane viewport
        timeScroll.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
//            timetablePane.setPrefWidth(newVal.getWidth());
        });
        
        

        // Give the timetable enough height to scroll
        timetablePane.setPrefHeight((HOURS + 1) * HOUR_HEIGHT);
        
     // Give the timetable enough width to scroll
        timetablePane.setPrefWidth((DAYS + 1) * DAY_WIDTH);
        

        // Generate the hour lines
        generateHourLines();
        
        //Generate the day lines
        generateDayLines();
        
        //Add the date at the top of each row on the graph
        generateDOW();
        
        //Add the time on the side of each column on the graph
        generateHOD();

        // Optional: test line
//        addTestLine();
        
        
        SpinnerValueFactory<LocalTime> factory =
                new SpinnerValueFactory.ListSpinnerValueFactory<>(
                    FXCollections.observableArrayList(times)
                );

            factory.setConverter(new StringConverter<LocalTime>() {
                @Override
                public String toString(LocalTime time) {
                    return time.format(DateTimeFormatter.ofPattern("HH:mm"));
                }

                @Override
                public LocalTime fromString(String string) {
                    return LocalTime.parse(string, DateTimeFormatter.ofPattern("HH:mm"));
                }
            });

            timeSpinner.setValueFactory(factory);

    }

    
    
    
    
    
    //find a way to get the user to add all the parameters within a schedule item into this class, 
    //perhaps have the button load up another screen to add all the details to the item
    @FXML
    public void addItemToSchedule() {
    	

//    	ScheduleDB();
    	scheduleCreationBox.setVisible(true);
    	scheduleCreationBox.setManaged(true);
    	scheduleCreationBox.setDisable(false);
    	BackButton.setManaged(true);
    	BackButton.setVisible(true);
    }
    
    @FXML
    public void closeScheduleScreen() {
    	scheduleCreationBox.setVisible(false);
    	scheduleCreationBox.setManaged(false);
    	BackButton.setManaged(false);
    }
    
    
    @FXML
    public void confirmItemAddition() {
    	

    	if(itemName.getText().isEmpty() == false && datePicker.getValue() != null && timeSpinner.getValue() != null) {
    		//make the values match from all of the items here
    		//realising i missed an item that allows the user to pick the duration of task
    		ScheduleItem inputtedItem = new ScheduleItem(datePicker.getValue(), timeSpinner.getValue(),itemName.getText(), null);
    		db.addNewItem(inputtedItem);
    		System.out.println(db.toString());
    		
    	}
    	
    }
    
    
    private void generateDayLines() {
        for (int i = 0; i <= DAYS; i++) {
            double x = i * DAY_WIDTH;

            Line line = new Line();
            line.setStartX(x);
            line.setStartY(0);
            line.endYProperty().bind(timetablePane.heightProperty());
            line.setEndX(x);

            line.setStroke(Color.GRAY);
            line.setStrokeWidth(1);

            timetablePane.getChildren().add(line);
        }
    }
    
    
       
    private void generateHourLines() {
        for (int i = 0; i <= HOURS; i++) {
            double y = i * HOUR_HEIGHT;

            Line line = new Line();
            line.setStartX(0);
            line.setStartY(y);
            line.endXProperty().bind(timetablePane.widthProperty());
            line.setEndY(y);

            line.setStroke(Color.GRAY);
            line.setStrokeWidth(1);

            timetablePane.getChildren().add(line);
        }
    }
    
    
    //generate the day label at the right place
    private void generateDOW() {
    	for (int i = 1; i<=DAYS; i++) {
    	Label dayLabel = new Label();
    	dayLabel.setLayoutX((i * DAY_WIDTH) + 10);
    	dayLabel.setLayoutY(0);
    	dayLabel.setText(dayName[i-1]);
    	timetablePane.getChildren().add(dayLabel);
    	}
    }
    
    //generate the hour labels at the correct location
    private void generateHOD() {
    	for (int i = 1; i<=HOURS; i++) {
        	Label hourLabel = new Label();
        	hourLabel.setLayoutX(10);
        	hourLabel.setLayoutY((i * HOUR_HEIGHT) + 10);
        	if(i<10) {
        		hourLabel.setText("0"+(i - 1)+":00");
        	}
        	else {
        		hourLabel.setText(((i - 1))+":00");
        	}
        	
        	timetablePane.getChildren().add(hourLabel);
        	}
    }
    
    

//    private void addTestLine() {
//        Line testLine = new Line(0, 100, 0, 100);
//        testLine.endXProperty().bind(timetablePane.widthProperty());
//        testLine.setStroke(Color.RED);
//        testLine.setStrokeWidth(2);
//
//        timetablePane.getChildren().add(testLine);
//    }
}