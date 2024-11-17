package pl.ernest.json;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.ernest.model.*;
import pl.ernest.model.basicLights.BasicLight;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerTest {

    @BeforeEach
    public void clearLogs(){
        Logger logger = Logger.getInstance();
        logger.clearLogs();
    }

    @Test
    public void testSingleton() {
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();
        assertSame(logger1, logger2);
    }

    @Test
    public void testAddStepBasic() {
        Logger logger = Logger.getInstance();
        logger.startBasicLogging();
        logger.addStep();
        assertEquals(1, logger.getStepStatuses().size());
        assertInstanceOf(BasicStepStatus.class, logger.getStepStatuses().getFirst());
    }

    @Test
    public void testAddStepFancy() {
        Logger logger = Logger.getInstance();
        logger.startFancyLogging();
        logger.addStep();
        assertEquals(1, logger.getStepStatuses().size());
        assertInstanceOf(FancyStepStatus.class, logger.getStepStatuses().getFirst());
    }

    @Test
    public void compatibilityTest() {
        Logger logger = Logger.getInstance();
        logger.startFancyLogging();
        logger.logLeftCar(new Vehicle("Car1", Road.west));
        logger.addStep();
        logger.startBasicLogging();
        logger.logLeftCar(new Vehicle("Car1", Road.west));
        logger.addStep();
        List<IStepStatus> stepStatuses = logger.getStepStatuses();
        assertEquals(2, stepStatuses.size());
        assertInstanceOf(FancyStepStatus.class, stepStatuses.getFirst());
        assertInstanceOf(BasicStepStatus.class, stepStatuses.get(1));
    }

    @Test
    public void testLogLeftCar() {
        Logger logger = Logger.getInstance();
        Vehicle vehicle = new Vehicle("Car123", Road.west);
        logger.logLeftCar(vehicle);
        assertTrue(logger.getCurrentFancyStep().leftVehicles.contains("Car123"));
        assertTrue(logger.getCurrentBasicStep().leftVehicles.contains("Car123"));
    }

    @Test
    public void testLogNewCycle() {
        Logger logger = Logger.getInstance();
        ILight light = new BasicLight(new LinkedList<Vehicle>(), IndicatorLight.Green,Road.north);
        logger.logNewCycle(light);
        assertTrue(logger.getCurrentFancyStep().newLightCycle.contains(Road.north + " road; [" + IndicatorLight.Green + "]"));
    }

    @Test
    public void testLogPedestrians() {
        Logger logger = Logger.getInstance();
        List<Pedestrian> pedestrians = List.of(new Pedestrian("John", Road.north), new Pedestrian("Jane", Road.west));
        logger.logPedestrians(pedestrians);
        assertTrue(logger.getCurrentFancyStep().leftPedestrians.contains("John"));
        assertTrue(logger.getCurrentFancyStep().leftPedestrians.contains("Jane"));
    }


}
