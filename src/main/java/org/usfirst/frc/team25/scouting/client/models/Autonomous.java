package org.usfirst.frc.team25.scouting.client.models;


/** Autonomous object model to deserialize JSON data. 
 * Modify as necessary for each season.
 */
public class Autonomous {

    int highShots, lowShots;
    boolean reached, crossed;

    public int getHighShots() {
        return highShots;
    }

    public int getLowShots() {
        return lowShots;
    }

    public boolean isReached() {
        return reached;
    }

    public boolean isCrossed() {
        return crossed;
    }



    public Autonomous(int highShots, int lowShots, boolean reached, boolean crossed) {
        this.highShots = highShots;
        this.lowShots = lowShots;
        this.reached = reached;
        this.crossed = crossed;
    }

    public Autonomous(){
        //Default empty constructor for JSON parsing
    }
}
