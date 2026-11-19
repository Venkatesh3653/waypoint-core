package waypoint.model;

import java.util.Objects;

public record TicketId(String value) {
    public TicketId {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Ticket ID must not be blank");
        }
    }
}
