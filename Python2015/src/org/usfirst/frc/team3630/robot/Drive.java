package org.usfirst.frc.team3630.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick; 
import edu.wpi.first.wpilibj.Ultrasonic; 


public class Drive {
	
	// PWM Assignments for driveTrain motors
	RobotDrive driveTrain; 
		int frontLeft;
		int rearLeft;
		int frontRight; 
		int rearRight; 
	

	// USB Assignments for joysticks 
	Joystick leftStick; 
	Joystick rightStick; 
		int leftJoy;
		int rightJoy; 
	

	Ultrasonic leftSonar; 
	Ultrasonic centerSonar; 
	
	public Drive(int frontLeftCh, int rearLeftCh, int frontRightCh, int rearRightCh,
				 int leftJoyCh, int rightJoyCh){
		
		frontLeft = frontLeftCh; 
		rearLeft = rearLeftCh; 
		frontRight = frontRightCh; 
		rearRight = rearRightCh; 
		
		leftJoy = leftJoyCh; 
		rightJoy = rightJoyCh; 

		
	}
	
	public void driveInit(){
		
		leftStick = new Joystick(leftJoy); 
		rightStick = new Joystick(rightJoy); 		
		
		
		driveTrain = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight); 
		driveTrain.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		driveTrain.setInvertedMotor(RobotDrive.MotorType.kRearRight, true); 
		driveTrain.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
		driveTrain.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
		
	}
	
	
	
	public void drivePeriodic(){ 
		driveTrain.mecanumDrive_Cartesian(leftStick.getX(), leftStick.getY() , rightStick.getX(), 0);
		
		
	}
	
	
	public void stop(){
		driveTrain.stopMotor();
		
		
		
	}
	
	
	

}
