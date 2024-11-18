package pl.ernest.model.basicLights;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.ernest.model.IVehicle;
import pl.ernest.model.Road;
import pl.ernest.model.IndicatorLight;
import pl.ernest.model.Car;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicLightTest {
    private Queue<Car> vehicles;
    private BasicLight basicLightGreen;
    private BasicLight basicLightYellow;
    private BasicLight basicLightRed;
    private BasicLight basicLightYellowRed;

    @BeforeEach
    void setUp(){
        vehicles = new LinkedList<>();

        vehicles.add(new Car("car1", Road.east));
        vehicles.add(new Car("car2", Road.east));
        vehicles.add(new Car("car3", Road.east));
        vehicles.add(new Car("car4", Road.east,2));
        vehicles.add(new Car("car5", Road.east,4));

        basicLightGreen = new BasicLight(new LinkedList<>(vehicles), IndicatorLight.Green, Road.north);
        basicLightYellow = new BasicLight(new LinkedList<>(vehicles), IndicatorLight.Yellow,2, Road.north);
        basicLightRed = new BasicLight(new LinkedList<>(vehicles), IndicatorLight.Red,3, Road.north);
        basicLightYellowRed = new BasicLight(new LinkedList<>(vehicles), IndicatorLight.YellowRed,4, Road.north);
    }


    @Test
    void getSumPriorityTest(){
        BasicLight basicEmptyLight = new BasicLight(new LinkedList<>(), IndicatorLight.Green, Road.north);

        assertEquals(9,basicLightGreen.getSumPriority());
        assertEquals(18,basicLightYellow.getSumPriority());
        assertEquals(27,basicLightRed.getSumPriority());
        assertEquals(36,basicLightYellowRed.getSumPriority());
        assertEquals(0,basicEmptyLight.getSumPriority());
    }

    @Test
    void greenCycleTest(){
        List<Optional<Car>> firstResultArray = new ArrayList<>();
        firstResultArray.add(Optional.of(new Car("car1", Road.east)));

        List<Optional<IVehicle>> greenCars = basicLightGreen.moveCarsIntoIntersection();
        List<Optional<IVehicle>> yellowCars = basicLightYellow.moveCarsIntoIntersection();
        List<Optional<IVehicle>> redCars = basicLightRed.moveCarsIntoIntersection();
        List<Optional<IVehicle>> yellowRedCars = basicLightYellowRed.moveCarsIntoIntersection();

        assertEquals(firstResultArray, greenCars);
        assertEquals(Optional.of(new Car("car2", Road.east)), basicLightGreen.moveCarsIntoIntersection().getFirst());
        assertEquals(Optional.of(new Car("car3", Road.east)), basicLightGreen.moveCarsIntoIntersection().getFirst());
        assertEquals(Optional.of(new Car("car4", Road.east, 2)), basicLightGreen.moveCarsIntoIntersection().getFirst());
        assertEquals(Optional.of(new Car("car5", Road.east,4)), basicLightGreen.moveCarsIntoIntersection().getFirst());
        assertTrue(basicLightGreen.moveCarsIntoIntersection().getFirst().isEmpty());
        assertTrue(yellowCars.getFirst().isEmpty());
        assertTrue(redCars.getFirst().isEmpty());
        assertTrue(yellowRedCars.getFirst().isEmpty());

        assertEquals(5, basicLightYellow.getCarsQueueSize());
        assertEquals(5, basicLightRed.getCarsQueueSize());
        assertEquals(5, basicLightYellowRed.getCarsQueueSize());
    }

    @Test
    void greenCarFromIndexTest(){
        Optional<Car> firstResult = Optional.of(new Car("car1", Road.east));

        Optional<IVehicle> greenCar = basicLightGreen.moveCarIntoIntersectionFromLane(0);
        Optional<IVehicle> yellowCar = basicLightYellow.moveCarIntoIntersectionFromLane(0);
        Optional<IVehicle> redCar = basicLightRed.moveCarIntoIntersectionFromLane(0);
        Optional<IVehicle> yellowRedCar = basicLightYellowRed.moveCarIntoIntersectionFromLane(0);

        assertEquals(firstResult, greenCar);
        assertTrue(basicLightGreen.moveCarIntoIntersectionFromLane(-1).isEmpty());
        assertTrue(basicLightGreen.moveCarIntoIntersectionFromLane(-312312123).isEmpty());
        assertTrue(basicLightGreen.moveCarIntoIntersectionFromLane(100).isEmpty());
        assertTrue(basicLightGreen.moveCarIntoIntersectionFromLane(1).isEmpty());
        assertTrue(yellowCar.isEmpty());
        assertTrue(redCar.isEmpty());
        assertTrue(yellowRedCar.isEmpty());


        assertEquals(4, basicLightGreen.getCarsQueueSize());

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

        assertEquals(IndicatorLight.Yellow,basicLightGreen.getCurrentLight());
        assertEquals(IndicatorLight.Red, basicLightYellow.getCurrentLight());
        assertEquals(IndicatorLight.YellowRed, basicLightRed.getCurrentLight());
        assertEquals(IndicatorLight.Green, basicLightYellowRed.getCurrentLight());
    }

    @Test
    void addVehicleTest(){
        Car addedVehicle = new Car("bus", Road.south);
        vehicles.add(addedVehicle);

        basicLightGreen.addVehicle(addedVehicle);

        assertEquals(vehicles.size(), basicLightGreen.getCarsQueueSize());
    }

    @Test
    void getAmountOfLanesTest(){
        assertEquals(1, basicLightGreen.getAmountOfLanes());
        assertEquals(1, basicLightYellow.getAmountOfLanes());
        assertEquals(1, basicLightRed.getAmountOfLanes());
        assertEquals(1, basicLightYellowRed.getAmountOfLanes());
    }
}
