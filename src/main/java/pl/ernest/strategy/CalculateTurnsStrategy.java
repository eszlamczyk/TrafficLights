package pl.ernest.strategy;

import pl.ernest.model.ILight;

import java.util.Collection;

public interface CalculateTurnsStrategy {
    int calculateTurns(Collection<ILight> lights);
}
