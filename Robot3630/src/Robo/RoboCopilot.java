/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Robo;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SmartDashboard;

/**
 *
 * @author paul
 */

public class RoboCopilot extends RoboTask {
    double lastTime = 0;
    final double maxRotSpeed = 200.0; // deg/second

    // arm grab support
    private boolean grabArmMode = false;

    // parameters at the time arm is grabbed
    private double grabR = 0;
    private double grabA = 0;
    private double grabRobotAngle = 0;
    private double grabArmAngle = 0;
    private double grabElevatorHeight = 0;

    // grab motion range
    final double grabRobotAngleRange = 19;
    final double grabElevatorHeightRange = 6;
    final double grabArmAngleRange = 15;

    // rapid turn support
    private boolean right90 = false;
    private boolean left90 = false;

    // line grab support
    boolean lineGrabMode = false;

    public void start(){
        super.start();
    }

    public void stop(){
        super.stop();
    }

    public void handle(){
        if(!active) return;
        super.handle();
        double dt = 0.0;
        if(lastTime > 0){
            dt = taskTime - lastTime;
        }
        lastTime = taskTime;

        // robot.drive.go(-leftDriveStick.getY(), leftDriveStick.getX(), rightDriveStick.getX());

        // read driver controls
        double x = robot.driverStation.leftDriveStick.getX();
        double y = -robot.driverStation.leftDriveStick.getY();
        double r = robot.driverStation.rightDriveStick.getX();
        double a = robot.driverStation.rightDriveStick.getY();  // for arm grab

        // read arm controls
        int pegButton = robot.driverStation.getPegButton();
        if(pegButton >= 0){
            robot.driverStation.setLed(pegButton);
            robot.manipulator.goToPeg(pegButton);
        }

        boolean grabArmButton = robot.driverStation.rightDriveStick.getRawButton(1);
        boolean elUpButton = robot.driverStation.leftDriveStick.getRawButton(3);
        boolean elDownButton = robot.driverStation.leftDriveStick.getRawButton(2);
        boolean armUpButton = robot.driverStation.rightDriveStick.getRawButton(3);
        boolean armDownButton = robot.driverStation.rightDriveStick.getRawButton(2);

        boolean tubeInButton = robot.driverStation.leftDriveStick.getRawButton(4);
        boolean tubeOutButton = robot.driverStation.leftDriveStick.getRawButton(5);

        boolean tubeTwistUpButton = robot.driverStation.rightDriveStick.getRawButton(4);
        boolean tubeTwistDownButton = robot.driverStation.rightDriveStick.getRawButton(5);

        boolean grabLineButton = robot.driverStation.leftDriveStick.getRawButton(1);

        boolean left90Button = false; // robot.driverStation.rightDriveStick.getRawButton(4);
        boolean right90Button = false; // robot.driverStation.rightDriveStick.getRawButton(5);

        if(grabArmButton){
            if(!grabArmMode){
                // activating grabArmMode
                grabArmMode = true;
                grabR = r;
                grabA = a;
                grabArmAngle = robot.manipulator.currentAngle;
                grabRobotAngle = robot.drive.readGyro();
                robot.drive.rotSpeed = 0;
                robot.drive.enableAutoRotation();
                grabElevatorHeight = robot.manipulator.currentHeight;
            }
            // now adjust relative to grabbed position
            robot.drive.targetAngle = grabRobotAngle + (r - grabR) * grabRobotAngleRange;
            // adjust arm or elevator depending on arm starting position
            if(grabArmAngle < 20){
                // almost vertical - use the elevator
                robot.manipulator.moveElevatorTo(grabElevatorHeight + (a - grabA) * grabElevatorHeightRange);
            }
            else {
                // arm lowered - move the arm
                robot.manipulator.moveArmTo(grabArmAngle - (a - grabA) * grabArmAngleRange);
            }
        }
        else {
            if(grabArmMode){
                robot.drive.disableAutoRotation();
            }
            robot.drive.rotSpeed = r;
            // manual manipulator control
            grabArmMode = false;
            if(robot.driverStation.leftDriveStick.getRawButton(11)) {
                robot.manipulator.enable();
            }
            if(robot.driverStation.leftDriveStick.getRawButton(10)) {
                robot.manipulator.disable();
            }
            if(robot.driverStation.leftDriveStick.getRawButton(6)) {
                robot.manipulator.zeroEncoders();
            }

            if(true){
                // handle manual elevator motion
                if(elUpButton) robot.manipulator.moveElevator(1.0);
                else if(elDownButton) robot.manipulator.moveElevator(-0.5);
                else if(!robot.manipulator.isAuto()) robot.manipulator.moveElevator(0.0);

                // handfle manual arm motion
                if(armUpButton) robot.manipulator.moveArm(1.0);
                else if(armDownButton) robot.manipulator.moveArm(-0.6);
                else if(!robot.manipulator.isAuto()) robot.manipulator.moveArm(0.0);
            } 
        }

        // handle grabber
        if(tubeInButton){
            robot.manipulator.clawGrab();
        }
        else if(tubeOutButton){
            robot.manipulator.clawRelease();
        }
        else if(tubeTwistUpButton){
            robot.manipulator.clawTwistUp();
        }
        else if(tubeTwistDownButton){
            robot.manipulator.clawTwistDown();
        }
        else {
            robot.manipulator.clawStop();
        }

        // display line visibility feedbackAuto
        robot.drive.setLed(!robot.lineTracker.lineVisible);

        // check if in line grab mode and line visible
        if(grabLineButton && robot.lineTracker.lineVisible){
            double slide = 0.3;
            // auto-approach
            if(robot.lineTracker.cross){
                robot.drive.ySpeed = 0;
                robot.drive.xSpeed = 0;
            }
            else {
                robot.drive.ySpeed = y;
                if(robot.lineTracker.left) {
                    robot.drive.xSpeed = -slide;
                }
                else if(robot.lineTracker.right) {
                    robot.drive.xSpeed = slide;
                }
            }
        }
        else {
            // manual driving
            robot.drive.ySpeed = y;
            robot.drive.xSpeed = x;
        }

        // minibot deployment
        if(robot.driverStation.leftDriveStick.getRawButton(8) && robot.driverStation.leftDriveStick.getRawButton(9)){
            robot.miniBot.deploy(true);
        }
        else {
            robot.miniBot.deploy(false);
        }

        if(robot.driverStation.rightDriveStick.getRawButton(8)){
            robot.miniBot.setServo1(0);
        }
        if(robot.driverStation.rightDriveStick.getRawButton(9)){
            robot.miniBot.setServo1(1);
        }

        /*
        if(robot.driverStation.rightDriveStick.getRawButton(6)){
            robot.miniBot.setServo1(1.0);
        }

        if(robot.driverStation.rightDriveStick.getRawButton(7)){
            robot.miniBot.setServo1(0.0);
        }
        if(robot.driverStation.rightDriveStick.getRawButton(6)){
            robot.miniBot.setServo1(1.0);
        }
        if(robot.driverStation.rightDriveStick.getRawButton(10)){
            robot.miniBot.setServo2(0.0);
        }
        if(robot.driverStation.rightDriveStick.getRawButton(11)){
            robot.miniBot.setServo2(0.0);
        }
         */
    }
}
