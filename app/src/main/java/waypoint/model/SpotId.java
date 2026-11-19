package waypoint.model;

import java.util.Objects;

public record SpotId(String value) {
    public SpotId {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Spot ID must not be blank");
        }
    }
}
