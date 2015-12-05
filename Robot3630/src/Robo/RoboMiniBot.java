/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Robo;

import edu.wpi.first.wpilibj.Servo;
/**
 *
 * @author paul
 */

public class RoboMiniBot extends RoboTask{
    Servo deploymentServo = new Servo(4, 9);
    Servo alignmentServo = new Servo(4, 10);

    boolean deploymentRequested = false;
    final double gameTime = 120;
    final double minibotDeploymentPeriod = 10;

    void deploy(boolean on){
        deploymentRequested = on;
    }

    public void setServo1(double d){
        deploymentServo.set(d);
    }
    public void setServo2(double d){
        alignmentServo.set(d);
    }

    public void start(){
        super.start();
        deploymentServo.set(0.0);
    }
    
    public void handle(){
        super.handle();
        if(taskTime >= (120 - 10) && deploymentRequested){
            deploymentServo.set(1.0);
        }
    }
}
