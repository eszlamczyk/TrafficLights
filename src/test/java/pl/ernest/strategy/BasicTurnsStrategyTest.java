package pl.ernest.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.ernest.model.ILight;
import pl.ernest.model.IndicatorLight;
import pl.ernest.model.Road;
import pl.ernest.model.Car;
import pl.ernest.model.basicLights.BasicLight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicTurnsStrategyTest {

    private ILight lightGreen;
    private ILight lightRed;
    private Collection<ILight> lights;

    @BeforeEach
    void setUp(){
        lightGreen = new BasicLight(new LinkedList<>(), IndicatorLight.Green, Road.north);
        lightRed = new BasicLight(new LinkedList<>(), IndicatorLight.Red, Road.north);
        lights = new ArrayList<>();
        lights.add(lightGreen);
        lights.add(lightRed);
    }


    @Test
    void carsOnlyInGreenLightTest(){
        lightGreen.addVehicle(new Car("car1", Road.west));

        AbstractTurnsStrategy turnsStrategy = new BasicTurnsStrategy(10);

        assertEquals(10, turnsStrategy.calculateTurns(lights));
    }

    @Test
    void carsOnlyInNotGreenLightTest(){
        lightRed.addVehicle(new Car("car1", Road.west));

        AbstractTurnsStrategy turnsStrategy = new BasicTurnsStrategy(10);

        //red
        assertEquals(0, turnsStrategy.calculateTurns(lights));

        //YellowRed
        lightRed.nextCycle();
        assertEquals(0, turnsStrategy.calculateTurns(lights));

        //Yellow
        lightRed.nextCycle();
        lightRed.nextCycle();
        assertEquals(0, turnsStrategy.calculateTurns(lights));
    }

    @Test
    void sequentialBasicTurnsStrategyTest(){
        lightGreen.addVehicle(new Car("car1", Road.west));
        lightRed.addVehicle(new Car("10 cars in one :o", Road.west, 10));

        AbstractTurnsStrategy turnsStrategy = new BasicTurnsStrategy(10);

        assertEquals(1, turnsStrategy.calculateTurns(lights));

        lightGreen.addVehicle(new Car("car2", Road.west));
        assertEquals(2, turnsStrategy.calculateTurns(lights));

        lightGreen.addVehicle(new Car("car3", Road.west));
        assertEquals(3, turnsStrategy.calculateTurns(lights));

        lightGreen.addVehicle(new Car("car4", Road.west));
        assertEquals(3, turnsStrategy.calculateTurns(lights));

        lightGreen.addVehicle(new Car("car5", Road.west));
        assertEquals(4, turnsStrategy.calculateTurns(lights));

        lightGreen.addVehicle(new Car("car6", Road.west));
        assertEquals(4, turnsStrategy.calculateTurns(lights));

        lightGreen.addVehicle(new Car("car7", Road.west));
        assertEquals(5, turnsStrategy.calculateTurns(lights));

        lightGreen.addVehicle(new Car("car8", Road.west));
        assertEquals(5, turnsStrategy.calculateTurns(lights));

        lightGreen.addVehicle(new Car("car9", Road.west));
        assertEquals(5, turnsStrategy.calculateTurns(lights));

        lightGreen.addVehicle(new Car("car10", Road.west));
        assertEquals(5, turnsStrategy.calculateTurns(lights));


        lightGreen.addVehicle(new Car("VERY important car", Road.west, 80));
        assertEquals(9, turnsStrategy.calculateTurns(lights));

    }

}
