package pl.ernest.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.ernest.strategy.AbstractTurnsStrategy;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class TrafficLightsTest {

    @Mock
    private ILight northLight;

    @Mock
    private ILight eastLight;

    @Mock
    private ILight southLight;

    @Mock
    private ILight westLight;

    @Mock
    private AbstractTurnsStrategy turnsStrategy;

    private TrafficLights trafficLights;

    @BeforeEach
    void setUp() throws CollidingLightConfigurationException {
        MockitoAnnotations.openMocks(this);

        // Mocking the turn strategy
        when(turnsStrategy.calculateTurns(anyCollection())).thenReturn(4);

        trafficLights = new TrafficLights(
                northLight, eastLight, southLight, westLight, turnsStrategy
        );
    }

    @Test
    void testConstruction_noCollisions() {
        assertDoesNotThrow(() -> new TrafficLights(northLight, eastLight, southLight, westLight, turnsStrategy));
    }

    @Test
    void testStepSimulation() {
        when(northLight.moveCarsIntoIntersection()).thenReturn(List.of(Optional.empty()));
        when(eastLight.moveCarsIntoIntersection()).thenReturn(List.of(Optional.empty()));
        when(southLight.moveCarsIntoIntersection()).thenReturn(List.of(Optional.empty()));
        when(westLight.moveCarsIntoIntersection()).thenReturn(List.of(Optional.empty()));

        trafficLights.stepSimulation();
        trafficLights.stepSimulation();
        trafficLights.stepSimulation();
        trafficLights.stepSimulation();
        trafficLights.stepSimulation();
        trafficLights.stepSimulation();

        verify(northLight).nextCycle();
        verify(eastLight).nextCycle();
        verify(southLight).nextCycle();
        verify(westLight).nextCycle();
    }

    @Test
    void testAddVehicle() {
        Vehicle mockVehicle = mock(Vehicle.class);
        trafficLights.addVehicle(Road.north, mockVehicle);
        verify(northLight).addVehicle(mockVehicle);
    }

    @Test
    void testAddPedestrian() {
        Pedestrian mockPedestrian = mock(Pedestrian.class);
        trafficLights.addPedestrian(Road.east, mockPedestrian);
        verify(eastLight).addPedestrian(mockPedestrian);
    }

    @Test
    void testPedestriansBlockVehicles() {
        when(northLight.isBlockedByPedestrians()).thenReturn(true);
        when(northLight.pedestriansCrossing()).thenReturn(List.of());

        trafficLights.stepSimulation();

        verify(northLight).pedestriansCrossing();
    }

    @Test
    void testLightCyclesAfterTurnExpiration() {
        trafficLights.stepSimulation();
        trafficLights.stepSimulation();
        trafficLights.stepSimulation();
        trafficLights.stepSimulation();
        trafficLights.stepSimulation();
        trafficLights.stepSimulation();


        verify(northLight, times(1)).nextCycle();
        verify(eastLight, times(1)).nextCycle();
        verify(southLight, times(1)).nextCycle();
        verify(westLight, times(1)).nextCycle();
    }

}
