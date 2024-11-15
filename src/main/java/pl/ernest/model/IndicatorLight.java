package pl.ernest.model;

public enum IndicatorLight {
    Red,
    YellowRed,
    Green,
    Yellow;

    public IndicatorLight next(){
        return switch (this){
            case Red -> YellowRed;
            case YellowRed -> Green;
            case Green -> Yellow;
            case Yellow -> Red;
        };
    }
}
