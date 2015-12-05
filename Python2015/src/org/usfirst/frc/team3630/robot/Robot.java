
package org.usfirst.frc.team3630.robot;

import edu.wpi.first.wpilibj.IterativeRobot;


public class Robot extends IterativeRobot {
	
	// Drive class named mainDrive
	Drive mainDrive;	
		// PWM Channels for mainDrive
		int frontLeftMotor; 
		int rearLeftMotor;
		int frontRightMotor;
		int rearRightMotor; 	
		// USB Channels for joySticks used by mainDrive
		int leftJoyPort;
		int rightJoyPort; 
	
	Manipulator mainMani; 
		// PWM Channels for mainMani 
		int elevator1; 
		int elevator2; 
		int elevator3; 
		int elevator4; 
		// DIO Channels for encoders used by mainMani 
		int encoder1ChA; 
		int encoder1ChB; 
		
		int encoder2ChA; 
		int encoder2ChB;
		
		int encoder3ChA; 
		int encoder3ChB;
		
		int encoder4ChA; 
		int encoder4ChB;
		// USB Channels for gamepad used by mainMani 
		int controlJoyPort; 
		
		// DIO Channels for limit switches used by mainMani
		int upperLimitSwitch; 
		int lowerLimitSwitch;
		
		boolean elevator1Reverse; 
		boolean elevator2Reverse; 
		boolean elevator3Reverse; 
		boolean elevator4Reverse; 
	

    public void robotInit() {
    	//MAIN DRIVE ****************************************************************
    	//PWM Assignments for mainDrive
    	frontLeftMotor = 1; 
    	rearLeftMotor = 2; 
    	frontRightMotor = 3; 
    	rearRightMotor = 4; 
    	
    	//USB Assignments for mainDrive
    	leftJoyPort = 0; 
    	rightJoyPort = 1;
    	
    	mainDrive = new Drive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor,
    						  leftJoyPort, rightJoyPort); 
    	mainDrive.driveInit(); 
    	//END OF MAIN DRIVE***********************************************************
    	//----------------------------------------------------------------------------
    	
    	
    	//MANIPULATOR*****************************************************************
    	//PWM Assignments for mainMani
    	elevator1 = 8; 
    	elevator2 = 7; 
    	elevator3 = 6; 
    	elevator4 = 5; 
    	
    	//DIO Assignments for mainMani
    	encoder1ChA = 10; 
    	encoder1ChB = 11; 
    	
    	encoder2ChA = 12; 
    	encoder2ChB = 13; 
    	
    	encoder3ChA = 8; 
    	encoder3ChB = 9; 
    	
    	encoder4ChA = 0; 
    	encoder4ChB = 1; 
    	
    	//USB Assignments for mainMani
    	controlJoyPort = 2; 
    	
    	//DIO Assignments for mainMani
    	upperLimitSwitch = 7; 
    	lowerLimitSwitch = 6; 
    	
    	
    	elevator1Reverse = false; 
    	elevator2Reverse = false; 
    	elevator3Reverse = false; 
    	elevator4Reverse = false; 
    	
    	mainMani = new Manipulator(elevator1, elevator2, elevator3, elevator4, 
    							   encoder1ChA, encoder1ChB, encoder2ChA, encoder2ChB, 
    							   encoder3ChA, encoder3ChB, encoder4ChA, encoder4ChB,
    							   controlJoyPort, upperLimitSwitch, lowerLimitSwitch,
    							   elevator1Reverse, elevator2Reverse, elevator3Reverse, 
    							   elevator4Reverse); 
    	mainMani.manipulatorInit();
    	
    	//END OF MANIPULATOR**********************************************************
    	//----------------------------------------------------------------------------
    	
    	
    	
    	
    }


    public void autonomousPeriodic() {

    }


    public void teleopPeriodic() {
    	
    	mainDrive.drivePeriodic();
    	mainMani.manipulatorPeriodic(); 
  
    }
    
    public void disabledInit() {
    	
    	mainDrive.stop(); 

    }
    
    
    

    public void testPeriodic() {
    
    }
    
}
