package waypoint.model;

public final class TicketAlreadyClosed extends IllegalStateException {
    private static final long serialVersionUID = 1L;

    public TicketAlreadyClosed() {
        super("Ticket is already closed");
    }
}
