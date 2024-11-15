package pl.ernest.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VehicleTest {

    @Test
    void vehicleCreationPriorityTest(){
        Vehicle noprioVehicle = new Vehicle("car", Road.east);
        Vehicle prioVehicle = new Vehicle("tram", Road.east,12);

        assertEquals(1, noprioVehicle.priority());
        assertEquals(12, prioVehicle.priority());
    }
}
