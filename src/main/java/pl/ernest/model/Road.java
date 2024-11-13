package pl.ernest.model;

public enum Road {
    north, east, south, west;

    public Road getStraight(){
        return switch (this){
            case north -> south;
            case east -> west;
            case south -> north;
            case west -> east;
        };
    }

    public Road getLeftTurn(){
        return switch (this){
            case north -> east;
            case east -> south;
            case south -> west;
            case west -> north;
        };
    }

    public Road getRightTurn(){
        return switch (this){
            case north -> west;
            case east -> north;
            case south -> east;
            case west -> south;
        };
    }
}
