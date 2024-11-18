package pl.ernest.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.ernest.model.basicLights.BasicLight;
import pl.ernest.model.fancyLights.LaneTurn;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class LaneTest {
    private Lane lane;

    private Queue<IndicatorLight> lights;

    @BeforeEach
    void setUp(){
        Queue<IVehicle> vehicles = new LinkedList<>();

        vehicles.add(new Car("Car1", Road.north));
        vehicles.add(new Car("Car2", Road.east));
        vehicles.add(new Car("Car3", Road.west));
        vehicles.add(new Car("ImportantCar", Road.north,3));
        vehicles.add(new Car("Bus", Road.south,5));

        lights = new LinkedList<>();
        lights.add(IndicatorLight.Green);
        lights.add(IndicatorLight.Green);
        lights.add(IndicatorLight.Yellow);
        lights.add(IndicatorLight.Red);
        lights.add(IndicatorLight.YellowRed);

        this.lane = new Lane(LaneTurn.LeftStraightRight,new LinkedList<>(lights), vehicles);

    }

    @Test
    void vehiclesInQueueTest(){
        Lane emptyLane = new Lane(LaneTurn.UTurn, new LinkedList<>(lights), new LinkedList<>());

        assertTrue(this.lane.vehiclesInQueue());
        assertFalse(emptyLane.vehiclesInQueue());
    }

    @Test
    void nextLightTest(){
        lane.nextLight();
        assertEquals(IndicatorLight.Green, lane.getLight());

        lane.nextLight();
        assertEquals(IndicatorLight.Yellow, lane.getLight());

        lane.nextLight();
        assertEquals(IndicatorLight.Red, lane.getLight());

        lane.nextLight();
        assertEquals(IndicatorLight.YellowRed, lane.getLight());

        lane.nextLight();
        assertEquals(IndicatorLight.Green, lane.getLight());

        for (int i = 0; i < lights.size(); i++) {
            lane.nextLight();
            assertEquals(4, lane.getNextLights().size());
        }
    }

    @Test
    void getSumPriorityTest(){
        BasicLight basicEmptyLight = new BasicLight(new LinkedList<>(), IndicatorLight.Green, Road.north);

        assertEquals(0,basicEmptyLight.getSumPriority());
        assertEquals(11, lane.getSumPriority());
    }

}
