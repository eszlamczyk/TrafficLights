package pl.ernest.basicLights;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.ernest.model.Road;
import pl.ernest.model.TrafficCycle;
import pl.ernest.model.Vehicle;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicLightTest {
    private Queue<Vehicle> vehicles;
    private BasicLight basicLightGreen;
    private BasicLight basicLightYellow;
    private BasicLight basicLightRed;
    private BasicLight basicLightYellowRed;

    @BeforeEach
    void setUp(){
        vehicles = new LinkedList<>();

        vehicles.add(new Vehicle("car1", Road.east));
        vehicles.add(new Vehicle("car2", Road.east));
        vehicles.add(new Vehicle("car3", Road.east));
        vehicles.add(new Vehicle("car4", Road.east,2));
        vehicles.add(new Vehicle("car5", Road.east,4));

        basicLightGreen = new BasicLight(vehicles, TrafficCycle.Green);
        basicLightYellow = new BasicLight(vehicles, TrafficCycle.Yellow);
        basicLightRed = new BasicLight(vehicles, TrafficCycle.Red);
        basicLightYellowRed = new BasicLight(vehicles, TrafficCycle.YellowRed);
    }


    @Test
    void getSumPriorityTest(){
        BasicLight basicEmptyLight = new BasicLight(new LinkedList<>(), TrafficCycle.Green);

        assertEquals(9,basicLightGreen.getSumPriority());
        assertEquals(0,basicEmptyLight.getSumPriority());
    }

    @Test
    void greenCycleTest(){
        List<Vehicle> firstResultArray = new ArrayList<>();
        firstResultArray.add(new Vehicle("car1", Road.east));

        List<Vehicle> greenCars = basicLightGreen.greenCycle();
        List<Vehicle> yellowCars = basicLightYellow.greenCycle();
        List<Vehicle> redCars = basicLightRed.greenCycle();
        List<Vehicle> yellowRedCars = basicLightYellowRed.greenCycle();

        assertEquals(firstResultArray, greenCars);
        assertEquals(new Vehicle("car2", Road.east), basicLightGreen.greenCycle().getFirst());
        assertEquals(new Vehicle("car3", Road.east), basicLightGreen.greenCycle().getFirst());
        assertEquals(new Vehicle("car4", Road.east, 2), basicLightGreen.greenCycle().getFirst());
        assertEquals(new Vehicle("car5", Road.east,4), basicLightGreen.greenCycle().getFirst());
        assertTrue(basicLightGreen.greenCycle().isEmpty());
        assertTrue(yellowCars.isEmpty());
        assertTrue(redCars.isEmpty());
        assertTrue(yellowRedCars.isEmpty());
    }

    @Test
    void getGreenPriorityTest(){
        assertEquals(9,basicLightGreen.getGreenPriority());
        assertEquals(0, basicLightYellow.getGreenPriority());
        assertEquals(0,basicLightRed.getGreenPriority());
        assertEquals(0,basicLightYellowRed.getGreenPriority());
    }

    @Test
    void nextCycleTest(){
        basicLightGreen.nextCycle();
        basicLightYellow.nextCycle();
        basicLightRed.nextCycle();
        basicLightYellowRed.nextCycle();

        assertEquals(TrafficCycle.Yellow,basicLightGreen.getTrafficCycle());
        assertEquals(TrafficCycle.Red, basicLightYellow.getTrafficCycle());
        assertEquals(TrafficCycle.YellowRed, basicLightRed.getTrafficCycle());
        assertEquals(TrafficCycle.Green, basicLightYellowRed.getTrafficCycle());
    }

    @Test
    void addVehicleTest(){
        Vehicle addedVehicle = new Vehicle("bus", Road.south);
        vehicles.add(addedVehicle);

        basicLightGreen.addVehicle(addedVehicle);

        assertEquals(vehicles, basicLightGreen.getCarsQueue());
    }
}
