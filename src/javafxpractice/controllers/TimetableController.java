package javafxpractice.controllers;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
    @FXML private DatePicker datePicker;
    @FXML private TextField itemName;

    private static final int HOURS = 24;
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
            timetablePane.setPrefWidth(newVal.getWidth());
        });

        // Give the timetable enough height to scroll
        timetablePane.setPrefHeight((HOURS + 1) * HOUR_HEIGHT);

        // Generate the hour lines
        generateHourLines();

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
    	
    	if(itemName.getText().isEmpty() && datePicker.getValue() != null && timeSpinner.getValue() != null) {
    		//make the values match from all of the items here
    		//realising i missed an item that allows the user to pick the duration of task
    		new ScheduleItem(null, null, null, 0);
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

    private void addTestLine() {
        Line testLine = new Line(0, 100, 0, 100);
        testLine.endXProperty().bind(timetablePane.widthProperty());
        testLine.setStroke(Color.RED);
        testLine.setStrokeWidth(2);

        timetablePane.getChildren().add(testLine);
    }
}