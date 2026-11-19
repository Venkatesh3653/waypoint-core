package waypoint.model;

import java.time.Instant;
import java.util.Objects;

/** ENTITY. Invariants:
 *   I1  entry is always present
 *   I2  exit is null, or not before entry
 *   I3  fare is null, or not negative
 *   I4  fare exists only if exit exists
 *   I5  a closed ticket never reopens
 */
public final class Ticket {
    private final TicketId ticketId;
    private final SpotId spotId;
    private final Instant entry;
    private Instant exit;
    private Money fare;

    private Ticket(TicketId ticketId, SpotId spotId, Instant entry) {
        this.ticketId = ticketId;
        this.spotId = spotId;
        this.entry = entry;
    }

    public static Ticket open(TicketId ticketId, SpotId spotId, Instant entry) {
        return new Ticket(
                Objects.requireNonNull(ticketId, "ticketId"),
                Objects.requireNonNull(spotId, "spotId"),
                Objects.requireNonNull(entry, "entry"));
    }

    public TicketId ticketId() {
        return ticketId;
    }

    public SpotId spotId() {
        return spotId;
    }

    public Instant entry() {
        return entry;
    }

    public Instant exit() {
        return exit;
    }

    public Money fare() {
        return fare;
    }

    public boolean isOpen() {
        return exit == null;
    }

    public Money close(Instant at, FarePolicy policy) {
        Objects.requireNonNull(at, "at");
        Objects.requireNonNull(policy, "policy");
        if (!isOpen()) {
            throw new TicketAlreadyClosed();
        }
        if (at.isBefore(entry)) {
            throw new IllegalArgumentException("Exit cannot be before entry");
        }

        Money calculatedFare = Objects.requireNonNull(
                policy.fareFor(entry, at, spotId),
                "Fare policy returned null");
        if (calculatedFare.isNegative()) {
            throw new IllegalArgumentException("Fare cannot be negative");
        }

        exit = at;
        fare = calculatedFare;
        return calculatedFare;
    }
}