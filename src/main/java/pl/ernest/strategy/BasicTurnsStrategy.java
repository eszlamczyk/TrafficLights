package pl.ernest.strategy;

import pl.ernest.model.ILight;

import java.util.Collection;

public class BasicTurnsStrategy implements CalculateTurnsStrategy{
    @Override
    public int calculateTurns(Collection<ILight> lights) {
        int greenPriority = 0;
        for (ILight light : lights){
            greenPriority += light.getGreenPriority();
        }

        int allPriority = 0;

        for (ILight light : lights){
            allPriority += light.getSumPriority();
        }

        if (allPriority == 0) return 0;

        return greenPriority / allPriority;
    }
}
