package pl.ernest.strategy;

import pl.ernest.model.ILight;

import java.util.Collection;

public class SingleTurnStrategy extends AbstractTurnsStrategy {
    protected SingleTurnStrategy(int priorityConstant) {
        super(priorityConstant);
    }

    @Override
    public int calculateTurns(Collection<ILight> lights) {
        return super.getPriorityConstant();
    }
}
