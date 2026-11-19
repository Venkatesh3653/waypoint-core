package waypoint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.Test;

class MoneyTest {
    private static final Currency INR = Currency.getInstance("INR");

    @Test
    void arithmeticPreservesMinorUnitsAndCurrency() {
        Money amount = Money.ofMinor(125, INR);

        assertEquals(275, amount.plus(Money.ofMinor(150, INR)).minorUnits());
        assertEquals(75, amount.minus(Money.ofMinor(50, INR)).minorUnits());
        assertEquals(375, amount.times(3).minorUnits());
        assertEquals("INR 1.25", amount.toString());
        assertEquals(amount, Money.rupees(1).plus(Money.ofMinor(25, INR)));
    }

    @Test
    void allocationDistributesRemainderAndHandlesNegativeAmounts() {
        List<Money> positive = Money.ofMinor(5, INR).allocate(3);
        List<Money> negative = Money.ofMinor(-5, INR).allocate(2);

        assertEquals(List.of(2L, 2L, 1L), positive.stream().map(Money::minorUnits).toList());
        assertEquals(List.of(-3L, -2L), negative.stream().map(Money::minorUnits).toList());
        assertEquals(5, positive.stream().mapToLong(Money::minorUnits).sum());
        assertEquals(-5, negative.stream().mapToLong(Money::minorUnits).sum());
    }

    @Test
    void rejectsInvalidAllocationAndMismatchedCurrencies() {
        Money rupees = Money.rupees(1);

        assertThrows(IllegalArgumentException.class, () -> rupees.allocate(0));
        assertThrows(IllegalArgumentException.class,
                () -> rupees.plus(Money.ofMinor(1, Currency.getInstance("USD"))));
        assertThrows(IllegalArgumentException.class,
                () -> rupees.compareTo(Money.ofMinor(1, Currency.getInstance("USD"))));
    }

    @Test
    void detectsOverflowAndRequiresCurrency() {
        assertThrows(ArithmeticException.class,
                () -> Money.ofMinor(Long.MAX_VALUE, INR).plus(Money.ofMinor(1, INR)));
        assertThrows(ArithmeticException.class, () -> Money.rupees(Long.MAX_VALUE));
        assertThrows(NullPointerException.class, () -> Money.ofMinor(0, null));
    }
}