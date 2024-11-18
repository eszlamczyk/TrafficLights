package pl.ernest.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarTest {

    @Test
    void vehicleCreationPriorityTest(){
        Car noprioVehicle = new Car("car", Road.east);
        Car prioVehicle = new Car("tram", Road.east,12);

        assertEquals(1, noprioVehicle.priority());
        assertEquals(12, prioVehicle.priority());
    }
}
