package pl.ernest.basicLights;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.ernest.model.Road;
import pl.ernest.model.IndicatorLight;
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

        basicLightGreen = new BasicLight(new LinkedList<>(vehicles), IndicatorLight.Green);
        basicLightYellow = new BasicLight(new LinkedList<>(vehicles), IndicatorLight.Yellow,2);
        basicLightRed = new BasicLight(new LinkedList<>(vehicles), IndicatorLight.Red,3);
        basicLightYellowRed = new BasicLight(new LinkedList<>(vehicles), IndicatorLight.YellowRed,4);
    }


    @Test
    void getSumPriorityTest(){
        BasicLight basicEmptyLight = new BasicLight(new LinkedList<>(), IndicatorLight.Green);

        assertEquals(9,basicLightGreen.getSumPriority());
        assertEquals(18,basicLightYellow.getSumPriority());
        assertEquals(27,basicLightRed.getSumPriority());
        assertEquals(36,basicLightYellowRed.getSumPriority());
        assertEquals(0,basicEmptyLight.getSumPriority());
    }

    @Test
    void greenCycleTest(){
        List<Optional<Vehicle>> firstResultArray = new ArrayList<>();
        firstResultArray.add(Optional.of(new Vehicle("car1", Road.east)));

        List<Optional<Vehicle>> greenCars = basicLightGreen.greenCars();
        List<Optional<Vehicle>> yellowCars = basicLightYellow.greenCars();
        List<Optional<Vehicle>> redCars = basicLightRed.greenCars();
        List<Optional<Vehicle>> yellowRedCars = basicLightYellowRed.greenCars();

        assertEquals(firstResultArray, greenCars);
        assertEquals(Optional.of(new Vehicle("car2", Road.east)), basicLightGreen.greenCars().getFirst());
        assertEquals(Optional.of(new Vehicle("car3", Road.east)), basicLightGreen.greenCars().getFirst());
        assertEquals(Optional.of(new Vehicle("car4", Road.east, 2)), basicLightGreen.greenCars().getFirst());
        assertEquals(Optional.of(new Vehicle("car5", Road.east,4)), basicLightGreen.greenCars().getFirst());
        assertTrue(basicLightGreen.greenCars().getFirst().isEmpty());
        assertTrue(yellowCars.getFirst().isEmpty());
        assertTrue(redCars.getFirst().isEmpty());
        assertTrue(yellowRedCars.getFirst().isEmpty());

        assertEquals(5, basicLightYellow.getCarsQueue().size());
        assertEquals(5, basicLightRed.getCarsQueue().size());
        assertEquals(5, basicLightYellowRed.getCarsQueue().size());
    }

    @Test
    void greenCarFromIndexTest(){
        Optional<Vehicle> firstResult = Optional.of(new Vehicle("car1", Road.east));

        Optional<Vehicle> greenCar = basicLightGreen.greenCarFromIndex(0);
        Optional<Vehicle> yellowCar = basicLightYellow.greenCarFromIndex(0);
        Optional<Vehicle> redCar = basicLightRed.greenCarFromIndex(0);
        Optional<Vehicle> yellowRedCar = basicLightYellowRed.greenCarFromIndex(0);

        assertEquals(firstResult, greenCar);
        assertTrue(basicLightGreen.greenCarFromIndex(-1).isEmpty());
        assertTrue(basicLightGreen.greenCarFromIndex(-312312123).isEmpty());
        assertTrue(basicLightGreen.greenCarFromIndex(100).isEmpty());
        assertTrue(basicLightGreen.greenCarFromIndex(1).isEmpty());
        assertTrue(yellowCar.isEmpty());
        assertTrue(redCar.isEmpty());
        assertTrue(yellowRedCar.isEmpty());


        assertEquals(4, basicLightGreen.getCarsQueue().size());

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

        assertEquals(IndicatorLight.Yellow,basicLightGreen.getTrafficCycle());
        assertEquals(IndicatorLight.Red, basicLightYellow.getTrafficCycle());
        assertEquals(IndicatorLight.YellowRed, basicLightRed.getTrafficCycle());
        assertEquals(IndicatorLight.Green, basicLightYellowRed.getTrafficCycle());
    }

    @Test
    void addVehicleTest(){
        Vehicle addedVehicle = new Vehicle("bus", Road.south);
        vehicles.add(addedVehicle);

        basicLightGreen.addVehicle(addedVehicle);

        assertEquals(vehicles, basicLightGreen.getCarsQueue());
    }

    @Test
    void getAmountOfLanesTest(){
        assertEquals(1, basicLightGreen.getAmountOfLanes());
        assertEquals(1, basicLightYellow.getAmountOfLanes());
        assertEquals(1, basicLightRed.getAmountOfLanes());
        assertEquals(1, basicLightYellowRed.getAmountOfLanes());
    }
}
