package javafxpractice.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafxpractice.utils.ScheduleDB;
import javafxpractice.utils.ScheduleItem;

public class TimetableController {

    @FXML private ScrollPane timeScroll;
    @FXML private AnchorPane timetablePane;
    @FXML private Button AddItemButton;
    @FXML private Button BackButton;
    @FXML private VBox scheduleCreationBox;

    private static final int HOURS = 24;
    private static final double HOUR_HEIGHT = 80; // pixels per hour
    
    
    //find a way to get the user to add all the parameters within a schedule item into this class, 
    //perhaps have the button load up another screen to add all the details to the item
    @FXML
    public void addItemToSchedule() {
//    	ScheduleDB();
    	scheduleCreationBox.setVisible(true);
    	scheduleCreationBox.setManaged(true);
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
    public void initialize() {

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