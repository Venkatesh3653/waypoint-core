package waypoint.model;

import java.util.Objects;

public record VehicleId(String value) {
    public VehicleId {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID must not be blank");
        }
    }
}
