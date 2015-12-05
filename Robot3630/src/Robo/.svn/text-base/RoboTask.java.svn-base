/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Robo;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author paul
 */
public class RoboTask {
    double taskTime = 0;
    boolean active = false;
    public static RoboRobot robot = null;
    Timer timer;

    RoboTask(){
        timer = new Timer();
    }

    public void start(){
        timer.reset();
        timer.start();
        active = true;
    }
    public void stop(){
        active = false;
    }
    public void handle(){
        taskTime = timer.get();
    }

    public double getTaskTime(){
        return taskTime;
    }
    double signum(double x){
        if(x > 0) return 1.0;
        else if (x < 0) return -1.0;
        else return 0.0;
    }
}
