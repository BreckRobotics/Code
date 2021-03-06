/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Robo;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDOutput;

/**
 *
 * @author paul
 */
public class RoboDrive extends RoboTask
{
    // current power settings (or target angle if gyro enabled)
    public double xSpeed = 0;
    public double ySpeed = 0;
    public double rotSpeed = 0;


    public double targetAngle = 0;
    public double targetRotation = 0;
    private double pidRotSpeed = 0;
    boolean autoRotationActive = false;
    boolean turning = false;

    // simulated position
    double heading = 0;
    double x = 0;
    double y = 0;
    double width = 27;
    double length = 48;
    double gyroZero = 0;
    private double ySpeedScale = 80.0;   // max speed in/sec
    private double xSpeedScale = 50.0;   // max speed in/sec
    private double rotSpeedScale = 180.0; // max rot deg/sec
    private double lastSimTime = 0;

    private Gyro gyro = new Gyro(1, 1);
    private DigitalOutput led = new DigitalOutput(4, 14);
    private DigitalInput configSwitch1 = new DigitalInput(4, 11);
    private DigitalInput configSwitch2 = new DigitalInput(4, 12);

    double lastTime = 0;
    double lastError = 0;
    double lastA = 0;

    // drive traincomponents
    final int frontLeft = 1;
    final int frontRight = 3;
    final int backLeft = 2;
    final int backRight = 4;
    final int port1 = 1;
    final int port2 = 2;
    private RobotDrive teamRobot = new RobotDrive(frontLeft, backLeft, frontRight, backRight);

    // speed scale factors (for testing safety - should be 1.0 in competition)
    public double xScale = 1.0;
    public double yScale = -1.0;    // reversed in the test robot!
    public double rotScale = 1.0;
    public double maxPowerRotation = 200;   // full power rotational velocity
    public double maxDecelleration = 100;   // decelleration when coasting to a stop
    // robot drive base object

    RoboDrive(){
        teamRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        teamRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        // set the MotorSafety expiration timer
        teamRobot.setExpiration(15);

        gyro.reset();
        targetAngle = 0.0;
    }

    public void go(double ahead, double right, double turn){
        ySpeed = ahead;
        xSpeed = right;
        rotSpeed = turn;
    }

    public double readGyro(){
        return gyro.getAngle();
    }
    public void resetGyro(){
        gyro.reset();
    }

    public void setLed(boolean on){
        led.set(on);
    }

    public boolean getConfigSwitch1(){
        return configSwitch1.get();
    }

    public boolean getConfigSwitch2(){
        return configSwitch2.get();
    }

    public void start(){
        super.start();
        teamRobot.mecanumDrive_Cartesian(0, 0, 0, 0);
        targetAngle = readGyro();
    }

    public void handle(){
        super.handle();

        if(!autoRotationActive){
            teamRobot.mecanumDrive_Cartesian(
                xSpeed * xScale,
                ySpeed * yScale,
                rotSpeed * rotScale,
                0.0);

            return;
        }
        // auto-rotation support - nice and steady (for autonomous only)

        // calculate angle error
        double a = readGyro();
        double da = targetAngle - a;
        double dabs = Math.abs(da);
        double r = 0;
        if(dabs > 15){
            r = signum(da) * 0.5;
            turning = true;
        }
        else if(dabs > 2){
            r = signum(da) * 0.3;
            turning = true;
        }
        else {
            r = 0;
            turning = false;
        }
        teamRobot.mecanumDrive_Cartesian(
            xSpeed * xScale,
            ySpeed * yScale,
            r * rotScale,
            0.0);
    }

    public void stop(){
        super.stop();
        ySpeed = 0;
        xSpeed = 0;
        rotSpeed = 0;
        teamRobot.mecanumDrive_Cartesian(0, 0, 0, 0);
    }

    public void enableAutoRotation(){
        targetAngle = gyro.getAngle();
        autoRotationActive = true;
    }

    public void disableAutoRotation(){
        autoRotationActive = false;
    }

    public boolean autoRotationEnabled(){
        return autoRotationActive;
    }

    public boolean isTurning(){
        return turning;
    }

    // note: called about 20 times per second
    void simulate(){
        double dt = robot.simDt;
        x = x + Math.cos(Math.toRadians(heading)) * xSpeed  * xSpeedScale * dt + Math.sin(Math.toRadians(heading)) * ySpeed * ySpeedScale * dt;
        y = y + Math.sin(Math.toRadians(heading)) * xSpeed * xSpeedScale * dt + Math.cos(Math.toRadians(heading)) * ySpeed * ySpeedScale * dt;
        heading += rotSpeed * rotSpeedScale * dt;
    }
}
