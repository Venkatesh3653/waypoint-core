package waypoint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class TicketTest {
    @Test
    void closeCalculatesFareAndCannotBeRepeated() {
        Instant entry = Instant.parse("2026-01-01T10:00:00Z");
        Instant exit = Instant.parse("2026-01-01T11:00:00Z");
        SpotId spotId = new SpotId("A1");
        Ticket ticket = Ticket.open(new TicketId("T1"), spotId, entry);
        Money expectedFare = Money.rupees(20);

        assertTrue(ticket.isOpen());
        Money actualFare = ticket.close(exit, (actualEntry, actualExit, actualSpotId) -> {
            assertEquals(entry, actualEntry);
            assertEquals(exit, actualExit);
            assertEquals(spotId, actualSpotId);
            return expectedFare;
        });

        assertEquals(expectedFare, actualFare);
        assertEquals(exit, ticket.exit());
        assertEquals(expectedFare, ticket.fare());
        assertFalse(ticket.isOpen());
        assertThrows(TicketAlreadyClosed.class, () -> ticket.close(exit, (e, x, s) -> expectedFare));
    }

    @Test
    void invalidClosuresLeaveTicketOpen() {
        Instant entry = Instant.parse("2026-01-01T10:00:00Z");
        Ticket ticket = Ticket.open(new TicketId("T1"), new SpotId("A1"), entry);

        assertThrows(IllegalArgumentException.class,
                () -> ticket.close(entry.minusSeconds(1), (e, x, s) -> Money.rupees(1)));
        assertThrows(IllegalArgumentException.class,
                () -> ticket.close(entry, (e, x, s) -> Money.ofMinor(-1, Currency.getInstance("INR"))));
        assertTrue(ticket.isOpen());
        assertNull(ticket.exit());
        assertNull(ticket.fare());
    }
}