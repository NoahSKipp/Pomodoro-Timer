import Classes.SettingsPage;
import Classes.TimerSettings;
import javafx.application.Application;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Toolkit;

public class PomodoroTimer extends Application {

    private int pomodoroMinutes;
    private Label timerLabel;
    private Button pomodoroButton;
    private Button shortBreakButton;
    private Button longBreakButton;
    private Button startButton;
    private Button stopButton;
    private Button skipButton;
    private Button settingsButton;

    private Timeline timeline;
    private TimerSettings timerSettings;
    private int seconds = 0;
    private int pomodoroCount = 0;
    private boolean isBreak = true;
    private boolean isPaused = false;

    @Override
    public void start(Stage primaryStage) {
        timerSettings = new TimerSettings(25, 5, 15); // Initialize timer settings

        timerLabel = new Label();
        timerLabel.setFont(new Font("Montserrat", 80));
        timerLabel.setTextFill(Color.WHITE);

        pomodoroButton = createButton("Work", event -> switchToPomodoro());
        shortBreakButton = createButton("Short Break", event -> switchToBreak(timerSettings.getShortBreakLength()));
        longBreakButton = createButton("Long Break", event -> switchToBreak(timerSettings.getLongBreakLength()));
        startButton = createButton("Start", event -> startTimer());
        stopButton = createButton("Stop", event -> stopTimer());
        skipButton = createButton("Skip", event -> skipTimer());
        settingsButton = createButton("Settings", event -> showSettingsPage(primaryStage));

        HBox topButtons = new HBox(10, pomodoroButton, shortBreakButton, longBreakButton);
        topButtons.setPadding(new Insets(10));
        topButtons.setStyle("-fx-alignment: center;");

        HBox bottomButtons = new HBox(10, startButton, stopButton, skipButton, settingsButton);
        bottomButtons.setPadding(new Insets(10));
        bottomButtons.setStyle("-fx-alignment: center;");

        StackPane timerPane = new StackPane(timerLabel);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(Color.web("#808080"), null, null)));
        root.setTop(topButtons);
        root.setCenter(timerPane);
        root.setBottom(bottomButtons);

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setTitle("Pomodoro Timer");
        primaryStage.setScene(scene);
        primaryStage.show();

        switchToPomodoro();
    }

    private Button createButton(String text, javafx.event.EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setOnAction(handler);
        button.getStyleClass().add("button");
        return button;
    }

    private void startTimer() {
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                if (!isPaused) {
                    if (pomodoroMinutes == 0 && seconds == 0) {
                        handleCycleChange(); // This should trigger when the timer runs out naturally
                        Toolkit.getDefaultToolkit().beep(); // Play a beep sound
                    } else {
                        if (seconds == 0) {
                            pomodoroMinutes--;
                            seconds = 59;
                        } else {
                            seconds--;
                        }
                        timerLabel.setText(formatTime(pomodoroMinutes, seconds)); // Update the timer label
                    }
                }
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
            startButton.setDisable(true);
            stopButton.setDisable(false);
            skipButton.setDisable(false); // Enable the skip button
        }

    private void stopTimer() {
        isPaused = !isPaused;
        if (isPaused) {
            timeline.pause();
            stopButton.setText("Resume");
        } else {
            timeline.play();
            stopButton.setText("Stop");
        }
    }

    private void resetTimer() {
        if (timeline != null) {
            timeline.stop();
        }
        isPaused = false;
        startButton.setDisable(false);
        stopButton.setDisable(true);
        skipButton.setDisable(true);
    }

    private void skipTimer() {
        boolean wasBreak = isBreak;

        if (wasBreak) {
            // If the previous timer was a break, switch to the next Pomodoro session
            switchToPomodoro();
        } else {
            // If the previous timer was a Pomodoro session, switch to the next break
            if (pomodoroCount % 4 == 0) {
                switchToBreak(timerSettings.getLongBreakLength());
            } else {
                switchToBreak(timerSettings.getShortBreakLength());
            }
        }

        resetTimer(); // Reset timer
        startTimer(); // Start the timer automatically
        timerLabel.setText(formatTime(pomodoroMinutes, seconds)); // Update the timer label with the new values after switching timers
    }

    // Simulate a button press for the next cycle when the timer ends naturally
    private void handleCycleChange() {
        Toolkit.getDefaultToolkit().beep(); // Play beep sound

        if (!isBreak) {
            if (pomodoroCount % 4 == 0) {
                switchToBreak(timerSettings.getLongBreakLength());
            } else {
                switchToBreak(timerSettings.getShortBreakLength());
            }
        } else {
            switchToPomodoro();
        }

        resetTimer(); // Reset timer
        startTimer(); // Start the timer automatically
        timerLabel.setText(formatTime(pomodoroMinutes, seconds)); // Update the timer label with the new values after switching timers
    }

    private void switchToPomodoro() {
        resetTimer();
        pomodoroMinutes = timerSettings.getPomodoroLength();
        seconds = 0;
        isBreak = false; // Set isBreak to false when switching to a Pomodoro session
        pomodoroButton.setDisable(true);
        shortBreakButton.setDisable(false);
        longBreakButton.setDisable(false);
        timerLabel.setText(formatTime(pomodoroMinutes, seconds));
        pomodoroCount++; // Increment pomodoroCount when switching to a Pomodoro session
    }


    private void switchToBreak(int breakMinutes) {
        resetTimer();
        pomodoroMinutes = breakMinutes;
        seconds = 0;
        isBreak = true; // Set isBreak to true when switching to a break
        pomodoroButton.setDisable(false);
        shortBreakButton.setDisable(true);
        longBreakButton.setDisable(true);
        timerLabel.setText(formatTime(pomodoroMinutes, seconds));
    }

    private String formatTime(int minutes, int seconds) {
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void showSettingsPage(Stage primaryStage) {
        SettingsPage settingsPage = new SettingsPage(timerSettings.getPomodoroLength(), timerSettings.getShortBreakLength(), timerSettings.getLongBreakLength());
        settingsPage.setOnSettingsApplied(event -> applySettings(settingsPage, primaryStage));
        Scene settingsScene = new Scene(settingsPage, 400, 300);
        primaryStage.setScene(settingsScene);
    }

    private void applySettings(SettingsPage settingsPage, Stage primaryStage) {
        int newPomodoroLength = settingsPage.getPomodoroLength();
        int newShortBreakLength = settingsPage.getShortBreakLength();
        int newLongBreakLength = settingsPage.getLongBreakLength();

        int remainingSeconds = (newPomodoroLength * 60) + seconds;

        pomodoroMinutes = newPomodoroLength;
        timerSettings = new TimerSettings(newPomodoroLength, newShortBreakLength, newLongBreakLength);

        timerLabel.setText(formatTime(pomodoroMinutes, remainingSeconds % 60));

        primaryStage.setScene(createMainScene());
    }

    private Scene createMainScene() {
        timerLabel.setText(formatTime(pomodoroMinutes, seconds));

        StackPane timerPane = new StackPane(timerLabel);

        HBox topButtons = new HBox(10, pomodoroButton, shortBreakButton, longBreakButton);
        topButtons.setPadding(new Insets(10));
        topButtons.setStyle("-fx-alignment: center;");

        HBox bottomButtons = new HBox(10, startButton, stopButton, skipButton, settingsButton);
        bottomButtons.setPadding(new Insets(10));
        bottomButtons.setStyle("-fx-alignment: center;");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(Color.web("#808080"), null, null)));
        root.setTop(topButtons);
        root.setCenter(timerPane);
        root.setBottom(bottomButtons);

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

