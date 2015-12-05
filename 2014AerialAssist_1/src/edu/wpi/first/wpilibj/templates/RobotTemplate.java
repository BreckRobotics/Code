/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Joystick; 
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Relay;
//import edu.wpi.first.wpilibj.Gyro;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    RobotDrive drive; 
    Joystick left; 
    Joystick right;
    DriverStationLCD driverstation;
    Talon ballIntake;
    Talon ramp;
    Talon shooterLeft;
    Talon shooterRight;
    Solenoid collectDown;
    Solenoid collectUp;
    Compressor airCompressor;
    Timer autoTimer;
    Relay spike; 
    
    
    //Gyro deliciousGyros;
    
    DigitalInput rampUp;  //ramp upper limit switch
    DigitalInput rampDown;  //ramp lower limit switch
    Encoder shooterEncoder;
    Encoder rampEncoder;
    Ultrasonic ultrasoundLeft;
    Ultrasonic ultrasoundRight;
    Ultrasonic haveBall;
    
    double ultrasoundDistance;
    double ultrasoundAngle;
    double shooterAngle;
    double rampAngle;
    boolean shoot;
    boolean resetShooter;
    boolean shooterDirection;
    boolean rampHalfway;
    boolean iHaveBall;
    boolean collectorDown;
    int autocount;
    int autoState;
    
    public void robotInit() { 
        
        drive = new RobotDrive(1, 2, 3, 4);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        
        left = new Joystick(1);
        right = new Joystick(2);
        
        ballIntake = new Talon(5);
        ramp = new Talon(6);
        shooterRight = new Talon(7);
        shooterLeft = new Talon(8);
        collectDown = new Solenoid(1);
        collectUp = new Solenoid(2);
        rampUp = new DigitalInput(4);
        rampDown = new DigitalInput(5);
        collectorDown = false;
        
        
        //deliciousGyros = new Gyro(14); 
        //hi luis <3 - Darius 
        
        
        
        
        airCompressor = new Compressor (1, 1); 
        

        
        shooterEncoder = new Encoder(2, 3, true);
        rampEncoder = new Encoder(10, 11, true);
        shooterEncoder.start();
        rampEncoder.start();
        rampHalfway = false;
        shoot = false;
        shooterDirection = false;
        
        ultrasoundLeft = new Ultrasonic(6, 7);
        ultrasoundRight = new Ultrasonic(8, 9);
        haveBall = new Ultrasonic(12, 13);
        ultrasoundLeft.setAutomaticMode(true);
        ultrasoundRight.setAutomaticMode(true);
        haveBall.setAutomaticMode(true);
        autoTimer = new Timer();
        iHaveBall = false;
        
        //Set collector up
        collectDown.set(false);
        collectUp.set(true);
    }
    public void disabledInit() {
        airCompressor.stop();
    }

    public void autonomousInit() {
        shooterEncoder.reset();
        airCompressor.start();
        autoTimer.start();
        autocount = 0;
        autoState = 0;
    }
    
    public void autonomousPeriodic() {
        //sonar();
        //shooter();
        //doihaveaball();
        double t;

        t = autoTimer.get();

        //Bring collector down
        collectDown.set(true);
        collectUp.set(false);
        
        switch (autoState) {
            case 0: if (t < 1){ //Drive for 2 second
                        drive.mecanumDrive_Cartesian(0, .25, 0, 0);
                    } else {
                        drive.stopMotor();
                        autoState++;
                        autoTimer.reset();
                    } break;
            case 1:   if (rampDown.get() == true){  //put ramp down until bottom
                         ramp.set(-0.5);
                      } else {
                            ramp.stopMotor();
                            rampEncoder.reset(); //Zero the shooter encoder at bottom+-+++
                            autoState++;
                            autoTimer.reset();
                      } break;
            case 2: if (t > 0) {  //wait 1 seconds
                        autoState++;
                        autoTimer.reset();
                    } break;
            case 3: if (t < 4) {  //suck in ball
                        ballIntake.set(-1);  //THIS LINE SUCKS (in the ball)
                        if (t < 2) {
                            drive.mecanumDrive_Cartesian(0, -.25, 0, 0);
                        } else {
                            drive.stopMotor();
                        }
                    } else {
                        drive.stopMotor();
                        ballIntake.stopMotor();
                        autoState++;
                        autoTimer.reset();
                    } break;
            case 4: if (t < .5) {  //bring ramp up halfway
                        ramp.set(.5);
                    } else {
                        ramp.stopMotor();
                        autoState++;
                        autoTimer.reset();
                    } break;
            case 5: if(ultrasoundDistance >= 30) {  //drive forward until 30 inches from goal
                        drive.mecanumDrive_Cartesian(0, .25, 0, 0);
                    } else {
                        drive.stopMotor();
                        autoState++;
                    } break;
//            case 6: if (t > 5){ //wait 2 seconds
//                        autoState++;
//                        autoTimer.reset();
//                    } break;
            case 6: 
                    shoot = true;
                    autoState++;
                    autoTimer.reset();
                    break;
//            case 8: if (t > 1) {  //wait 1 second
//                        autoState++;
//                        autoTimer.reset();
//                    } break;
//            case 9: if (t < 2) {  //reset shooter
//                        resetShooter = true;
//                    } else {
//                        resetShooter = false;
//                        autoState++;
//                        autoTimer.reset();
//                    } break;
            default: break;
        }
        //driverDisplay();
    }

    public void teleopInit() {
        drive.setSensitivity(0.5);
        airCompressor.start();
        shooterEncoder.reset();
    }

    public void teleopPeriodic() {
        //sonar();
        //shooter();
        //doihaveaball();
        drive.mecanumDrive_Cartesian(left.getX(), left.getY(), right.getX(), 0);

        // Ball Intake: Talon #5
        if (left.getRawButton(3)) {
            ballIntake.set(1);
        } else if (left.getRawButton(2)) {
            ballIntake.set(-1);
        } else {
            ballIntake.stopMotor();
        }

        // Ramp: Talon #6
        if ((left.getRawButton(6)) && rampUp.get() == true) {
            ramp.set(0.5); //put ramp up
        } else if ((left.getRawButton(7)) && rampDown.get() == true) {
            ramp.set(-0.5);  //put ramp down
        } else {
            ramp.stopMotor();
        }
        
//        if (left.getRawButton(4)) {
//            rampHalfway = true; //set the ramp to halfway
//        }
//        
//        if (rampUp.get() == false) {
//            shooterEncoder.reset();  //reset the shooter encoder at the bottom
//        }
        
//        if (rampHalfway && shooterAngle > -9) {
//            ramp.set(0.2); //put ramp up
//        } else if (rampHalfway && shooterAngle < -15) {
//            ramp.set(-0.2);
//        } else {
//            ramp.stopMotor();
//            rampHalfway = false;
//        }
        
        // Shooter: right motor Talon #7, left motor Talon #8
        if (right.getRawButton(11)) {
            //Shoot
            shoot = true;
            shooterRight.set(1.0);
            shooterLeft.set(-1.0);
             System.out.println("Stop");

        }else{
            shoot = false;
            shooterRight.stopMotor();
            shooterLeft.stopMotor();
            System.out.println("Stop");
        } /*
        if (right.getRawButton(9) && right.getRawButton(6)) {
            shooterRight.set(.3);
            shooterLeft.set(-.3);
        }
        else if (right.getRawButton(9) && right.getRawButton(7)) {
            shooterRight.set(-.2);
            shooterLeft.set(.2);
        }
        else if (right.getRawButton(9) && right.getRawButton(8)) {
            shooterEncoder.reset();
        }
        else if (right.getRawButton(9)) {
            shooterRight.stopMotor();
            shooterLeft.stopMotor();
        }
        } else if (right.getRawButton(10)) {
            //reset
            resetShooter = true;
        } else {
            shoot = false;
            resetShooter = false;
        }*/

        //Ball Intake
        if (right.getRawButton(5)) {  //put collector down
            collectDown.set(true);
            collectUp.set(false);
            collectorDown = true;
        }
        if (right.getRawButton(4)) {  //put collector up
            collectDown.set(false);
            collectUp.set(true);
            collectorDown = false;
        }
  //      driverDisplay();
       /*  
        if(right.getRawButton(3)){
            spike.set(Relay.Value.kOn);
            
        }
        else {
            spike.set(Relay.Value.kOff);
        }
       
        
        
        
        
    }*/

//public void sonar() {
  //  double left, right;
    //left = ultrasoundLeft.getRangeInches();
    //right = ultrasoundRight.getRangeInches();
    //ultrasoundDistance = (left + right)/2; //Average the values.
    //ultrasoundAngle = Math.tan((left - right)/20); //Find the angle to surface. 20 is the distance between the sonars
}


/*public void shooter (){
     if (shoot) {
            //Shoot
            shooterRight.set(1);
            shooterLeft.set(-1);
        } else if (resetShooter) {
            //reset
            shooterRight.set(-0.3);
            shooterLeft.set(0.3);
        } else {
            shooterRight.stopMotor();
            shooterLeft.stopMotor();
        }
}*/

public void driverDisplay () {
        SmartDashboard.putNumber("Shooter Angle", shooterAngle);
        SmartDashboard.putBoolean("Shoot", shoot);
        SmartDashboard.putBoolean("ShooterDirection", shooterDirection);
        SmartDashboard.putBoolean("ShooterStopped", shooterEncoder.getStopped());
        SmartDashboard.putNumber("ultrasoundLeft", ultrasoundLeft.getRangeInches());
        SmartDashboard.putNumber("ultrasoundRight", ultrasoundRight.getRangeInches());
        SmartDashboard.putNumber("ultrasoundDistance", ultrasoundDistance);
        SmartDashboard.putNumber("ultrasoundAngle", ultrasoundAngle);
        SmartDashboard.putNumber("autoState", autoState);
        SmartDashboard.putBoolean("RampDown", rampUp.get());
        SmartDashboard.putBoolean("RampUp", rampDown.get());
        SmartDashboard.putNumber("rampAngle", rampAngle);
        SmartDashboard.putBoolean("Pressure", airCompressor.getPressureSwitchValue());
        SmartDashboard.putBoolean("iHaveBall", iHaveBall);
        SmartDashboard.putBoolean("collectorDown", collectorDown);
        SmartDashboard.putNumber("BallSonar", haveBall.getRangeInches());
}

 public void shooter() {
    shooterAngle = shooterEncoder.get();
    rampAngle = rampEncoder.get();
    shooterDirection = shooterEncoder.getDirection();
        
    if (shoot && shooterAngle < 20) {
            // Shoot
            shooterRight.set(1);
            shooterLeft.set(-1);
            System.out.print("Encoder Set");
        } 
        else if (shooterDirection && shooterAngle > 0) {
            // Reset
            shoot = false;
            shooterRight.set(-.2);
            shooterLeft.set(.2);
            System.out.print("Shooter stop");
        } else if (!shooterDirection && shooterAngle > 0) {
            shooterRight.set(-0.2);
            shooterLeft.set(0.2);
            System.out.print("Encoder reset");
        }
        else {
            shooterRight.stopMotor();
            shooterLeft.stopMotor();
        }
}

public void doihaveaball () {
    iHaveBall = haveBall.getRangeInches() < 4;
}  
}