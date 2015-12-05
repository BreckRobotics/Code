
package org.usfirst.frc.team3630.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	//Dashboard
	SmartDashboard dashboard;
	
	//DriveTrain
	Joystick leftStick;
	Joystick rightStick;
	
	int LeftFrontMotorChannel;
	int LeftRearMotorChannel;
	int RightFrontMotorChannel;
	int RightRearMotorChannel;
	
	int leftSonarChannelIn;
	int leftSonarChannelOut;
	
	int rightSonarChannelIn; 
	int rightSonarChannelOut; 
	
	int centerSonarChannelIn; 
	int centerSonarChannelOut; 
	
	int encoder1ChannelA; 
	int encoder1ChannelB; 
	
	int encoder2ChannelA; 
	int encoder2ChannelB; 
	
	int encoder3ChannelA; 
	int encoder3ChannelB; 
	
	int encoder4ChannelA; 
	int encoder4ChannelB; 
	
	
	DriveTrain driveTrain;
	
	//Manipulator
	Joystick manipulatorStick;
	
	int elevator1MotorChannel;
	boolean elevator1Reversal;
	int elevator2MotorChannel;
	boolean elevator2Reversal;
	int elevator3MotorChannel;
	boolean elevator3Reversal;
	int elevator4MotorChannel;
	boolean elevator4Reversal;
	
	
	int limitSwitchUpperChannel;
	int limitSwitchLowerChannel;
	
	Manipulator manipulator;
	
	
    public void robotInit() {
    	
    	//Dashboard
    	dashboard = new SmartDashboard();
    	
    	//DriveTrain
    	leftStick = new Joystick(1);
    	rightStick = new Joystick(2);
    	
    	LeftFrontMotorChannel = 1;
    	LeftRearMotorChannel = 2;
    	RightFrontMotorChannel = 3;
    	RightRearMotorChannel = 4;
    	
    	leftSonarChannelIn = 0;
    	leftSonarChannelOut = 1;
    	
    	rightSonarChannelIn = 2; 
    	rightSonarChannelOut = 3; 
    	
    	centerSonarChannelIn = 4; 
    	centerSonarChannelOut = 5;
    	
    	limitSwitchUpperChannel = 6; 
    	limitSwitchLowerChannel = 7; 
    	
    	encoder1ChannelA = 10; 
    	encoder1ChannelB = 11; 
    	
    	encoder2ChannelA = 12; 
    	encoder2ChannelB = 13; 
    	
    	encoder3ChannelA = 14;
    	encoder3ChannelB = 15; 
    	
    	encoder4ChannelA = 16;
    	encoder4ChannelB = 17; 
    	
    	driveTrain = new DriveTrain(LeftFrontMotorChannel, LeftRearMotorChannel, 
    								RightFrontMotorChannel, RightRearMotorChannel, 
    								leftStick, rightStick,
    								leftSonarChannelIn, leftSonarChannelOut,
    								rightSonarChannelIn, rightSonarChannelOut, 
    								centerSonarChannelIn, centerSonarChannelOut, 
    								dashboard);
    	driveTrain.driveTrainInit();
    	
    	//Manipulator
    	manipulatorStick = new Joystick(3);
    	
    	elevator1MotorChannel = 8;
    	elevator1Reversal = false;
    	elevator2MotorChannel = 7;
    	elevator2Reversal = true;
    	elevator3MotorChannel = 6;
    	elevator3Reversal = true;
    	elevator4MotorChannel = 5;
    	elevator4Reversal = true;
    	
    	
    
    	
    	manipulator = new Manipulator(elevator1MotorChannel, elevator2MotorChannel,
    								  elevator3MotorChannel, elevator4MotorChannel,
    								  elevator1Reversal, elevator2Reversal,
    								  elevator3Reversal, elevator4Reversal,
    								  encoder1ChannelA, encoder1ChannelB, 
    								  encoder2ChannelA, encoder2ChannelB, 
    								  encoder3ChannelA, encoder3ChannelB, 
    								  encoder4ChannelA, encoder4ChannelB, 
    								  manipulatorStick, 
    								  limitSwitchUpperChannel, limitSwitchLowerChannel,
    								  dashboard);
    	manipulator.manipulatorInit();
    	
    }

    public void autonomousInit() {
    	//Pick up tote one
    	//Strafe until sonar finds tote two
    	//Align with tote two
    	//Pick up tote two
    	//Strafe until sonar finds tote three
    	//Align with tote three
    	//Pick up tote three
    	//Drive forward to Auto zone
    	//Put down totes
    	//Back up
    }
    
    public void autonomousPeriodic() {

    }

    public void teleopInit() {
    	
    }
    
    public void teleopPeriodic() {
        
    	driveTrain.driveTrainPeriodic();
    	manipulator.manipulatorPeriodic();
    	
    }
    
    public void testPeriodic() {
    
    }
    
}
