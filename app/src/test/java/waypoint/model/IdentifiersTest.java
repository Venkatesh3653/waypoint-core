package waypoint.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class IdentifiersTest {
    @Test
    void rejectsNullAndBlankIdentifiers() {
        assertThrows(NullPointerException.class, () -> new TicketId(null));
        assertThrows(IllegalArgumentException.class, () -> new TicketId(" \t "));
        assertThrows(NullPointerException.class, () -> new SpotId(null));
        assertThrows(IllegalArgumentException.class, () -> new SpotId(""));
        assertThrows(NullPointerException.class, () -> new VehicleId(null));
        assertThrows(IllegalArgumentException.class, () -> new VehicleId(" "));
    }
}
