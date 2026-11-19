package waypoint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class RegistrationTest {
    @Test
    void trimsAndNormalizesValidRegistrations() {
        assertEquals("MH12AB1234", new Registration(" mh12ab1234 ").value());
        assertEquals("MH12A1234", new Registration("mh12a1234").value());
    }

    @Test
    void rejectsInvalidRegistrations() {
        assertThrows(IllegalArgumentException.class, () -> new Registration("MH1AB1234"));
        assertThrows(IllegalArgumentException.class, () -> new Registration("MH12ABC12345"));
        assertThrows(NullPointerException.class, () -> new Registration(null));
    }
}
