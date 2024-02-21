package Classes;

import javafx.event.Event;
import javafx.event.EventType;

public class SettingsAppliedEvent extends Event {
    public static final EventType<SettingsAppliedEvent> SETTINGS_APPLIED =
            new EventType<>(Event.ANY, "SETTINGS_APPLIED");

    public SettingsAppliedEvent() {
        super(SETTINGS_APPLIED);
    }
}
