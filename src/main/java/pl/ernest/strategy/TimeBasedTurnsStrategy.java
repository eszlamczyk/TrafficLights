package pl.ernest.strategy;

import pl.ernest.model.ILight;

import java.time.Clock;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Set;


public class TimeBasedTurnsStrategy extends AbstractTurnsStrategy{

    private final Clock clock;
    protected TimeBasedTurnsStrategy(int priorityConstant, Clock clock) {
        super(priorityConstant);
        this.clock = clock;
    }
    private static final Set<Integer> RUSH_HOURS = Set.of(7,8,9,15,16,17);
    private static final Set<Integer> NIGHT_HOURS = Set.of(22,23,0,1,2,3,4,5);
    @Override
    public int calculateTurns(Collection<ILight> lights) {
        LocalTime time = LocalTime.now(clock);
        int hour = time.getHour();
        if (RUSH_HOURS.contains(hour)){
            return super.getPriorityConstant() * 4;
        }
        if (NIGHT_HOURS.contains(hour)){
            return super.getPriorityConstant();
        }
        else {
            return super.getPriorityConstant() * 2;
        }
    }
}
