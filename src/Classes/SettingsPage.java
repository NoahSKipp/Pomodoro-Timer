package Classes;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SettingsPage extends VBox {

    private TextField pomodoroTextField;
    private TextField shortBreakTextField;
    private TextField longBreakTextField;
    private Button applyButton; // Added apply button

    public SettingsPage(int pomodoroLength, int shortBreakLength, int longBreakLength) {
        setPadding(new Insets(20));
        setSpacing(10);

        Label titleLabel = new Label("Settings");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Pomodoro Timer Length Text Field
        Label pomodoroLabel = new Label("Work Timer Length:");
        pomodoroTextField = new TextField(Integer.toString(pomodoroLength));

        // Short Break Length Text Field
        Label shortBreakLabel = new Label("Short Break Length:");
        shortBreakTextField = new TextField(Integer.toString(shortBreakLength));

        // Long Break Length Text Field
        Label longBreakLabel = new Label("Long Break Length:");
        longBreakTextField = new TextField(Integer.toString(longBreakLength));

        applyButton = new Button("Apply"); // Initialize apply button
        applyButton.setOnAction(event -> {
            // Fire the event to apply the settings
            fireEvent(new SettingsAppliedEvent());
        });

        getChildren().addAll(titleLabel, pomodoroLabel, pomodoroTextField, shortBreakLabel, shortBreakTextField, longBreakLabel, longBreakTextField, applyButton); // Add apply button
    }

    // Getter methods for accessing text field values
    public int getPomodoroLength() {
        return Integer.parseInt(pomodoroTextField.getText());
    }

    public int getShortBreakLength() {
        return Integer.parseInt(shortBreakTextField.getText());
    }

    public int getLongBreakLength() {
        return Integer.parseInt(longBreakTextField.getText());
    }

    // Custom event class for settings applied event
    public static class SettingsAppliedEvent extends javafx.event.Event {
        public static final javafx.event.EventType<SettingsAppliedEvent> SETTINGS_APPLIED =
                new javafx.event.EventType<>(javafx.event.Event.ANY, "SETTINGS_APPLIED");

        public SettingsAppliedEvent() {
            super(SETTINGS_APPLIED);
        }
    }

    // Method to set event handler for settings applied event
    public void setOnSettingsApplied(javafx.event.EventHandler<SettingsAppliedEvent> eventHandler) {
        addEventHandler(SettingsAppliedEvent.SETTINGS_APPLIED, eventHandler);
    }
}
