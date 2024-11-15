package pl.ernest.model;

public enum Road {
    north, east, south, west;

    public Road getOppositeRoad(){
        return switch (this){
            case north -> south;
            case east -> west;
            case south -> north;
            case west -> east;
        };
    }

    public Road getRoadOnTheLeft(){
        return switch (this){
            case north -> east;
            case east -> south;
            case south -> west;
            case west -> north;
        };
    }

    public Road getRoadOnTheRight(){
        return switch (this){
            case north -> west;
            case east -> north;
            case south -> east;
            case west -> south;
        };
    }
}
