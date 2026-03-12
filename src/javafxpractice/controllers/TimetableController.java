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
import javafx.scene.Group;
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
import javafx.scene.shape.Rectangle;
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
    private final List<ScheduleItem> allEvents = new ArrayList<>();
    
    
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

        // Sync internal state with the requested week
        currentWeekStart = weekStart;

        List<LocalDate> week = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            week.add(weekStart.plusDays(i));
        }

        mondayLabel.setText("Week Beginning:" + week.get(0));
        updateDayLabels();

        clearOldEvents();

        List<ScheduleItem> events = getEventsForCurrentWeek();

        for (ScheduleItem event : events) {
            drawUIElement(event);
        }
    }
    
    
    private void clearOldEvents() {
        timetablePane.getChildren().removeIf(node -> node.getStyleClass().contains("event"));
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
    	

    	ScheduleItem inputtedItem = new ScheduleItem(
    		    datePicker.getValue(),
    		    timeSpinner.getValue(),
    		    itemName.getText(),
    		    durationSpinner.getValue()
    		);

    		allEvents.add(inputtedItem);   // <-- add the item to the list of events

    		db.addNewItem(inputtedItem);

    		renderWeek(currentWeekStart);  // redraw properly   		
    		
    	}
    	
    
    
    public List<ScheduleItem> getEventsForCurrentWeek() {
        LocalDate start = currentWeekStart;
        LocalDate end = currentWeekStart.plusDays(6);

        return allEvents.stream()
            .filter(e -> !e.getDate().isBefore(start) && !e.getDate().isAfter(end))
            .toList();
    }
    
    
    
    private void drawUIElement(ScheduleItem event) {
    	//in here, this will describe drawing the rectangle of the event 
    	//also maybe the label of the element describing it
    	int columnIndex = event.getDate().getDayOfWeek().getValue() - 1;
    	double x = (columnIndex + 1) * DAY_WIDTH + 10;
    	double y = event.getTime().getHour() * HOUR_HEIGHT
    	         + (event.getTime().getMinute() / 60.0) * HOUR_HEIGHT;
    	
    	
    	// Compute rectangle size
    	double height = (event.getDuration().toMinutes() / 60.0) * HOUR_HEIGHT;
    	double width = DAY_WIDTH - 20;

    	// Create rectangle
    	Rectangle eventRectangle = new Rectangle(width, height);
    	eventRectangle.setFill(Color.rgb(0, 128, 255, 0.5));
    	eventRectangle.setArcWidth(10);
    	eventRectangle.setArcHeight(10);

    	
    	//create the label
    	Label eventLabel = new Label(event.getTask());
    	eventLabel.setWrapText(true);
    	eventLabel.setEllipsisString("…");

    	// match the rectangle width (minus padding)
    	eventLabel.setMaxWidth(width - 10);

    	// match the rectangle height (minus padding)
    	eventLabel.setMaxHeight(height - 10);
    	eventLabel.setPrefHeight(height - 10);

    	// padding inside the rectangle
    	eventLabel.setTranslateX(5);
    	eventLabel.setTranslateY(5);

    	// Add padding inside the rectangle
    	eventLabel.setTranslateX(5);
    	eventLabel.setTranslateY(5);

    	// Group them
    	Group eventGroup = new Group(eventRectangle, eventLabel);

    	// Position the whole event block
    	eventGroup.setLayoutX(x);
    	eventGroup.setLayoutY(y);

    	// Add to timetable
    	timetablePane.getChildren().add(eventGroup);
    	
    	//node creation
    	eventGroup.getStyleClass().add("event");
    	
    	
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
    private void updateDayLabels() {
        for (int i = 0; i < 7; i++) {
            DayOfWeek dow = DayOfWeek.of(i + 1);
            LocalDate date = getDateFor(dow);

            dayLabels[i].setText(dow.name() + " " + date);
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
    
    


}