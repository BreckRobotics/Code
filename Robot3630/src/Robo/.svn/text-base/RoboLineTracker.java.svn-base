/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Robo;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author paul
 */
public class RoboLineTracker extends RoboTask {
    public boolean negativeTape = false; // set to true for shiny floors!
    public boolean lineVisible = false;
    public boolean right = false;
    public boolean left = false;
    public boolean center = false;
    public boolean cross = false;
    public boolean lastSeenRight = false;
    public boolean lastSeenLeft = false;
    boolean change = false;

    boolean timeBased = false;
    boolean straightLine = true;
    boolean splitLine = false;
    double lastSeenTime = 0;
    private boolean trace = false;

    DigitalInput leftsensor;
    DigitalInput middlesensor;
    DigitalInput rightsensor;

    RoboLineTracker(){
        // create the digital input objects to read from the sensors
        leftsensor = new DigitalInput(1);
        middlesensor = new DigitalInput(2);
        rightsensor = new DigitalInput(3);
    }
    
    public void handle(){
        super.handle();

        left = leftsensor.get() ^ negativeTape;
        center = middlesensor.get() ^ negativeTape;
        right = rightsensor.get() ^ negativeTape;

        lineVisible = (left || right || center);
        cross = (left && right && center);
        if(left && !right) {
            lastSeenLeft = true;
            lastSeenRight = false;
        }
        if(right && !left) {
            lastSeenRight = true;
            lastSeenLeft = false;
        }
        if(lineVisible) {
            lastSeenTime = taskTime;
        }
        else if((lastSeenTime + 1) < taskTime){
            // last seen time valid for 1 seconds only!
            lastSeenLeft = false;
            lastSeenRight = false;
        }
    }

    void simulate(){
        if(timeBased){
            if(taskTime < 10) {
                int n = (int) taskTime % 4;
                switch(n){
                    case 0:
                    case 2:
                        // straight
                        lineVisible = true;
                        right = false;
                        left = false;
                        center = true;
                        break;
                    case 1:
                        // left
                        lineVisible = true;
                        right = false;
                        center = false;
                        lastSeenRight = false;
                        left = true;
                        lastSeenLeft = true;
                        break;
                    case 3:
                        // right
                        lineVisible = true;
                        right = true;
                        lastSeenRight = true;
                        left = false;
                        center = false;
                        lastSeenLeft = false;
                        break;
                    default:
                        break;
                }
            }
            else {
                lineVisible = true;
                right = true;
                left = true;
                center = true;
                cross = true;
            }
        }
        if(straightLine || splitLine){
            // straight line, X = 0, Y = <-10, +fieldLength>, cross at fieldLength

            // ldx/ldy - offset for the left sensor (relative to center
            double ldx = -2 * Math.cos(Math.toRadians(robot.drive.heading));
            double ldy =  2 * Math.sin(Math.toRadians(robot.drive.heading));
            // rdx/rdy - offset for the left sensor (relative to center
            double rdx = -ldx;
            double rdy = -ldy;

            // center sensor coordinates
            double cx = robot.drive.x + Math.sin(Math.toRadians(robot.drive.heading)) * robot.drive.length / 2;
            double cy = robot.drive.y + Math.cos(Math.toRadians(robot.drive.heading)) * robot.drive.length / 2;

            left = onLine(cx + ldx, cy + ldy);
            right = onLine(cx + rdx, cy + rdy);
            center = onLine(cx, cy);

            lineVisible = (left || right || center);
            cross = (left && right && center);
            if(left && !right) {
                lastSeenLeft = true;
                lastSeenRight = false;
            }
            if(right && !left) {
                lastSeenRight = true;
                lastSeenLeft = false;
            }
            if(lineVisible) {
                lastSeenTime = taskTime;
            }
            else if((lastSeenTime + 1) < taskTime){
                // last seen time valid for 1 seconds only!
                lastSeenLeft = false;
                lastSeenRight = false;
            }
            // "stretch" left and right peripheral vision
            //if(!center && lastSeenLeft) left = true;
            //if(!center && lastSeenRight) right = true;
        }
    }

    private boolean onLine(double x, double y){
        int fieldLength = 220;

        if(straightLine){
            // line
            if(y >= -20 && y <= fieldLength && x >= -1 && x <= 1){
                return true;
            }
            // cross
            if(y >= (fieldLength-1) && y <= (fieldLength+4) && x >= -7 && x <= 7){
                return true;
            }
        }
        if(splitLine){
            int dx = 12 * 3;
            int dyT = 12 * 1;   // top
            int dyD = 12 * 6;   // diagonal
            // bottom straight segment
            if(y >= -20 && y <= (fieldLength - dyT - dyD)&& x >= -1 && x <= 1){
                return true;
            }
            // diagonal
            if(y >= (fieldLength - dyT - dyD) && y <= (fieldLength - dyT)){
                // left diagonal
                int xx = (int)((y - (fieldLength-dyT-dyD)) * dx / dyD);
                if(x >= (-xx-1) && x <= (-xx+1)){
                    return true;
                }
                if(x >= (xx-1) && x <= (xx+1)){
                    return true;
                }
            }
            // top straight left
            if(y >= (fieldLength-dyT) && y <= fieldLength && x >= (-dx-1) && x <= (-dx+1)){
                return true;
            }
            // top straight right
            if(y >= (fieldLength-dyT) && y <= fieldLength && x >= (dx-1) && x <= (dx+1)){
                return true;
            }

            // cross
            if(y >= (fieldLength-1) && y <= (fieldLength+4) && x >= (-dx-7) && x <= (-dx+7)){
                return true;
            }
            if(y >= (fieldLength-1) && y <= (fieldLength+1) && x >= (dx-7) && x <= (dx+7)){
                return true;
            }

        }
        return false;
    }
}
