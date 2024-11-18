package pl.ernest.strategy;

import org.junit.jupiter.api.Test;
import java.time.*;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class TimeBasedTurnsStrategyTest {

    @Test
    void testCalculateTurnsDuringRushHours() {
        Clock fixedClock = Clock.fixed(
                LocalDateTime.of(2024, 11, 18, 8, 0).toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault()
        );
        AbstractTurnsStrategy strategy = new TimeBasedTurnsStrategy(10, fixedClock);


        assertEquals(40, strategy.calculateTurns(Collections.emptyList()));
    }

    @Test
    void testCalculateTurnsDuringNightHours() {
        Clock fixedClock = Clock.fixed(
                LocalDateTime.of(2024, 11, 18, 2, 0).toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault()
        );
        AbstractTurnsStrategy strategy = new TimeBasedTurnsStrategy(10, fixedClock);


        assertEquals(10, strategy.calculateTurns(Collections.emptyList()));
    }

    @Test
    void testCalculateTurnsDuringNormalHours() {
        Clock fixedClock = Clock.fixed(
                LocalDateTime.of(2024, 11, 18, 12, 0).toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault()
        );
        AbstractTurnsStrategy strategy = new TimeBasedTurnsStrategy(10, fixedClock);


        assertEquals(20, strategy.calculateTurns(Collections.emptyList()));
    }
}
