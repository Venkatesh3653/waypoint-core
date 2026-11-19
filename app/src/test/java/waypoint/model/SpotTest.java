package waypoint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SpotTest {
    @Test
    void assignmentIsExclusiveAndReleaseIsIdempotent() {
        Spot spot = new Spot(new SpotId("A1"));
        VehicleId firstVehicle = new VehicleId("V1");
        VehicleId secondVehicle = new VehicleId("V2");

        assertTrue(spot.isFree());
        assertTrue(spot.tryAssign(firstVehicle));
        assertFalse(spot.tryAssign(firstVehicle));
        assertFalse(spot.tryAssign(secondVehicle));
        spot.release(secondVehicle);
        assertFalse(spot.isFree());
        spot.release(firstVehicle);
        spot.release(firstVehicle);
        assertTrue(spot.isFree());
    }

    @Test
    void equalityUsesIdRegardlessOfOccupancy() {
        Spot occupied = new Spot(new SpotId("A1"));
        Spot free = new Spot(new SpotId("A1"));
        occupied.tryAssign(new VehicleId("V1"));

        assertEquals(occupied, free);
        assertEquals(occupied.hashCode(), free.hashCode());
    }
}