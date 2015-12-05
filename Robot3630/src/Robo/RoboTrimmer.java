/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Robo;

/**
 *
 * @author Developer
 */

public class RoboTrimmer {
    String names[] = {
        "stage0_time1y",
        "stage0_speed1y",
        "stage0_time2y",
        "stage0_speed2y",

        "stage2_time1y",
        "stage2_speed1y",
        "stage2_time2y",
        "stage2_speed2y",

        "stage6_time1y",
        "stage6_speed1y",
        "stage6_time2y",
        "stage6_speed2y",

    };

    int currentTrim = 0;
    int nofTrims = 12;
    public RoboRobot robot;

    int getCount(){
        return nofTrims;
    }

    String getName(int n){
        return names[n];
    }

    // trimmer variable access (not elegant, but supports simple types without boxing anything)
    double get(int n){
        switch(n){
            case 0: return robot.autoPilot.stage0_time1y;
            case 1: return robot.autoPilot.stage0_speed1y;
            case 2: return robot.autoPilot.stage0_time2y;
            case 3: return robot.autoPilot.stage0_speed2y;

            case 4: return robot.autoPilot.stage2_time1y;
            case 5: return robot.autoPilot.stage2_speed1y;
            case 6: return robot.autoPilot.stage2_time2y;
            case 7: return robot.autoPilot.stage2_speed2y;

            case 8: return robot.autoPilot.stage6_time1y;
            case 9: return robot.autoPilot.stage6_speed1y;
            case 10:return robot.autoPilot.stage6_time2y;
            case 11:return robot.autoPilot.stage6_speed2y;

            default:return 0;
        }
    }

    // trimmer variable setting (not elegant, but supports simple types without boxing anything)
    void set(int n, double v){
        switch(n){
            case 0: robot.autoPilot.stage0_time1y = v; break;
            case 1: robot.autoPilot.stage0_speed1y = v; break;
            case 2: robot.autoPilot.stage0_time2y = v; break;
            case 3: robot.autoPilot.stage0_speed2y = v; break;

            case 4: robot.autoPilot.stage2_time1y = v; break;
            case 5: robot.autoPilot.stage2_speed1y = v; break;
            case 6: robot.autoPilot.stage2_time2y = v; break;
            case 7: robot.autoPilot.stage2_speed2y = v; break;

            case 8: robot.autoPilot.stage6_time1y = v; break;
            case 9: robot.autoPilot.stage6_speed1y = v; break;
            case 10:robot.autoPilot.stage6_time2y = v; break;
            case 11:robot.autoPilot.stage6_speed2y = v; break;

            default:
                break;
        }
    }

    // move trimmer to next value
    public void next(){
        if(++currentTrim >= nofTrims) currentTrim = 0;
    }
    // move trimmer to previous value
    public void previous(){
        if(--currentTrim < 0) currentTrim = (nofTrims - 1);
    }
    public String name(){
        return getName(currentTrim);
    }
    public double value(){
        return get(currentTrim);
    }
    public void change(double delta){
        set(currentTrim, get(currentTrim) + delta);
    }
}
