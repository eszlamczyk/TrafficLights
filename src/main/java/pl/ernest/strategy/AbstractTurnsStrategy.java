package pl.ernest.strategy;

import pl.ernest.model.ILight;

import java.util.Collection;

public abstract class AbstractTurnsStrategy {

    private final int priorityConstant;

    protected AbstractTurnsStrategy(int priorityConstant) {
        this.priorityConstant = priorityConstant;
    }

    public int getPriorityConstant() {
        return priorityConstant;
    }

    public abstract int calculateTurns(Collection<ILight> lights);
}
