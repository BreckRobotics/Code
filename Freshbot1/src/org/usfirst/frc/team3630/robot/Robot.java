
package org.usfirst.frc.team3630.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
  
//driveTrain
	Joystick leftStick;
	Joystick rightStick;
	
	int LeftFrontMotorChannel;
	int LeftRearMotorChannel;
	int RightFrontMotorChannel;
	int RightRearMotorChannel;
	
	DriveTrain driveTrain;
	
	
    public void robotInit() {
      	leftStick = new Joystick(1);
    	rightStick = new Joystick(2);
    	
    	LeftFrontMotorChannel = 0;
    	LeftRearMotorChannel = 1;
    	RightFrontMotorChannel = 2;
    	RightRearMotorChannel = 3;
    	
    	driveTrain = new DriveTrain(LeftFrontMotorChannel, LeftRearMotorChannel, RightFrontMotorChannel, RightRearMotorChannel, leftStick, rightStick);
    	
    	driveTrain.driveTrainInit();
    }


    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	driveTrain.driveTrainPeriodic();
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
