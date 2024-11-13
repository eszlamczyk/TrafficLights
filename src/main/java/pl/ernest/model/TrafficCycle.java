package pl.ernest.model;

public enum TrafficCycle {
    Red,
    YellowRed,
    Green,
    Yellow;

    public TrafficCycle next(){
        return switch (this){
            case Red -> YellowRed;
            case YellowRed -> Green;
            case Green -> Yellow;
            case Yellow -> Red;
        };
    }
}
