package javafxpractice.controllers;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML private Label mondayLabel;
    @FXML private Button pWB;
    @FXML private Button nWB;
    
    
    
//    @FXML private Button NSButton;
//    private String[] dayName = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private final ScheduleDB db = new ScheduleDB();
    
    //date related values
    LocalDate selected = LocalDate.now(); // or from DatePicker
    LocalDate monday = selected.with(DayOfWeek.MONDAY);
    private LocalDate currentWeekStart;
    
    //ui values
    private static final int HOURS = 24;
    private static final int DAYS = 7;
    private static final double DAY_WIDTH = 200; // pixels per day
    private static final double HOUR_HEIGHT = 80; // pixels per hour
    private Label[] dayLabels = new Label[7];
    
    
    
    
    
    @FXML
    public void initialize() {

        // ------------------------------------------------------------
        // 1. Establish the current week BEFORE rendering anything
        // ------------------------------------------------------------
        currentWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);

        // ------------------------------------------------------------
        // 2. Prepare the timetable canvas size
        // ------------------------------------------------------------
        timetablePane.setPrefHeight(HOURS * HOUR_HEIGHT);
        timetablePane.setPrefWidth(DAYS * DAY_WIDTH);

        // ------------------------------------------------------------
        // 3. Create static UI elements (day labels, grid lines)
        // ------------------------------------------------------------
        createDayLabels();      // creates labels ONCE
        generateHourLines();    // horizontal hour lines
        generateDayLines();     // vertical day lines
        generateHOD();          // hour-of-day labels

        // ------------------------------------------------------------
        // 4. Render the week (updates day labels + events)
        // ------------------------------------------------------------
        renderWeek(currentWeekStart);

        // ------------------------------------------------------------
        // 5. Build the list of valid LocalTime values (00:00 → 23:45)
        // ------------------------------------------------------------
        List<LocalTime> times = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                times.add(LocalTime.of(hour, minute));
            }
        }

        // ------------------------------------------------------------
        // 6. Configure the time spinner (start time of event)
        // ------------------------------------------------------------
        SpinnerValueFactory<LocalTime> timeFactory =
            new SpinnerValueFactory.ListSpinnerValueFactory<>(
                FXCollections.observableArrayList(times)
            );

        timeFactory.setConverter(new StringConverter<LocalTime>() {
            @Override
            public String toString(LocalTime time) {
                return time.format(DateTimeFormatter.ofPattern("HH:mm"));
            }

            @Override
            public LocalTime fromString(String string) {
                return LocalTime.parse(string, DateTimeFormatter.ofPattern("HH:mm"));
            }
        });

        timeSpinner.setValueFactory(timeFactory);

        // ------------------------------------------------------------
        // 7. Configure the duration spinner (15 min → 5 hours)
        // ------------------------------------------------------------
        ObservableList<Duration> durations = FXCollections.observableArrayList();

        Duration min = Duration.ofMinutes(15);
        Duration max = Duration.ofHours(5);
        Duration step = Duration.ofMinutes(15);

        for (Duration d = min; !d.minus(max).isPositive(); d = d.plus(step)) {
            durations.add(d);
        }

        SpinnerValueFactory<Duration> durationFactory =
            new SpinnerValueFactory.ListSpinnerValueFactory<>(durations);

        durationFactory.setConverter(new StringConverter<Duration>() {
            @Override
            public String toString(Duration d) {
                long hours = d.toHours();
                long minutes = d.toMinutesPart();

                if (hours > 0 && minutes > 0) return hours + "h " + minutes + "m";
                if (hours > 0) return hours + "h";
                return minutes + "m";
            }

            @Override
            public Duration fromString(String s) {
                return null; // spinner not editable
            }
        });

        durationSpinner.setValueFactory(durationFactory);
        durationSpinner.setEditable(false);

        // ------------------------------------------------------------
        // 8. ScrollPane behaviour (optional future resizing logic)
        // ------------------------------------------------------------
        timeScroll.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
            // Reserved for dynamic resizing if needed
        });
    }

   
    
    public void renderWeek(LocalDate weekStart) {
        List<LocalDate> week = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            week.add(weekStart.plusDays(i));
        }

        // Update your UI labels here
         mondayLabel.setText("Week Beginning:" + week.get(0).toString());
         updateDayLabels();
        // ...
    }
    
    
    @FXML
    private void nextWeek() {
        currentWeekStart = currentWeekStart.plusWeeks(1);
        renderWeek(currentWeekStart);
    }

    @FXML
    private void previousWeek() {
        currentWeekStart = currentWeekStart.minusWeeks(1);
        renderWeek(currentWeekStart);
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
    	

    	if(itemName.getText().isEmpty() == false && datePicker.getValue() != null && timeSpinner.getValue() != null && durationSpinner != null) {
    		//make the values match from all of the items here
    		//realising i missed an item that allows the user to pick the duration of task
    		ScheduleItem inputtedItem = new ScheduleItem(datePicker.getValue(), timeSpinner.getValue(),itemName.getText(), durationSpinner.getValue());
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
    
    private void createDayLabels() {
        for (int i = 0; i < 7; i++) {
            Label lbl = new Label();
            lbl.setLayoutX((i + 1) * DAY_WIDTH + 10);
            lbl.setLayoutY(0);

            dayLabels[i] = lbl;
            timetablePane.getChildren().add(lbl);
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
    private void updateDayLabels() {
        for (int i = 0; i < 7; i++) {
            DayOfWeek dow = DayOfWeek.of(i + 1);
            LocalDate date = getDateFor(dow);

            dayLabels[i].setText(dow.name() + " " + date);
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
    
    
    public LocalDate getDateFor(DayOfWeek day) {
        return currentWeekStart.plusDays(day.getValue() - 1);
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