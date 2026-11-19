package waypoint.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

/** Exact monetary amount stored in minor units. */
public final class Money implements Comparable<Money> {
    private static final Currency INR = Currency.getInstance("INR");

    private final long paise;
    private final Currency currency;

    private Money(long paise, Currency currency) {
        this.paise = paise;
        this.currency = Objects.requireNonNull(currency, "currency");
    }

    public static Money ofMinor(long amount, Currency currency) {
        return new Money(amount, currency);
    }

    public static Money rupees(long rupees) {
        return ofMinor(Math.multiplyExact(rupees, 100L), INR);
    }

    public long minorUnits() {
        return paise;
    }

    public Currency currency() {
        return currency;
    }

    public Money plus(Money other) {
        requireSameCurrency(other);
        return ofMinor(Math.addExact(paise, other.paise), currency);
    }

    public Money minus(Money other) {
        requireSameCurrency(other);
        return ofMinor(Math.subtractExact(paise, other.paise), currency);
    }

    public Money times(int multiplier) {
        return ofMinor(Math.multiplyExact(paise, multiplier), currency);
    }

    public boolean isNegative() {
        return paise < 0;
    }

    @Override
    public int compareTo(Money other) {
        requireSameCurrency(other);
        return Long.compare(paise, other.paise);
    }

    public List<Money> allocate(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Allocation count must be positive");
        }

        long base = paise / count;
        long remainder = paise % count;
        long extraShares = Math.abs(remainder);
        long direction = Long.signum(remainder);
        List<Money> shares = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            shares.add(ofMinor(base + (i < extraShares ? direction : 0), currency));
        }
        return List.copyOf(shares);
    }

    private void requireSameCurrency(Money other) {
        Objects.requireNonNull(other, "other");
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must match");
        }
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || other instanceof Money money
                && paise == money.paise
                && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paise, currency);
    }

    @Override
    public String toString() {
        return currency.getCurrencyCode() + " " + BigDecimal.valueOf(paise, 2).toPlainString();
    }
}