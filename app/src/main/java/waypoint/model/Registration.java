package waypoint.model;

import java.util.Locale;
import java.util.Objects;

/** Vehicle registration in the format AA00AA0000 or AA00A0000. */
public record Registration(String value) {
    public Registration {
        value = Objects.requireNonNull(value, "value").trim().toUpperCase(Locale.ROOT);
        if (!value.matches("[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}")) {
            throw new IllegalArgumentException("Invalid vehicle registration: " + value);
        }
    }
}