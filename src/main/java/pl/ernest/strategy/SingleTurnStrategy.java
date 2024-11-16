package pl.ernest.strategy;

import pl.ernest.model.ILight;

import java.util.Collection;

public class SingleTurnStrategy implements CalculateTurnsStrategy{
    @Override
    public int calculateTurns(Collection<ILight> lights) {
        return 1;
    }
}
