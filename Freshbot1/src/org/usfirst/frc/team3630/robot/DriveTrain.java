package org.usfirst.frc.team3630.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

public class DriveTrain {
	
	Joystick leftStick;
	Joystick rightStick;
	
	int LeftFrontChannel;
	int LeftRearChannel;
	int RightFrontChannel;
	int RightRearChannel;
	
	double strafe; 
	double forward; 
	double rotate; 
	
	RobotDrive mainDrive;
	
	public DriveTrain(int lfChannel, int lrChannel, 
			  int rfChannel, int rrChannel, 
			  Joystick lStick, Joystick rStick){
		
		LeftFrontChannel = lfChannel;
		LeftRearChannel = lrChannel;
		RightFrontChannel = rfChannel;
		RightRearChannel = rrChannel;
		
		leftStick = lStick;
		rightStick = rStick;
	}

	public void driveTrainInit() {
		
		mainDrive = new RobotDrive(LeftFrontChannel, LeftRearChannel, 
								   RightFrontChannel, RightRearChannel);
		mainDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
		mainDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
		mainDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		mainDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		
		mainDrive.mecanumDrive_Cartesian(leftStick.getX(), leftStick.getY(), rightStick.getX(), 0);
		
		mainDrive.setSafetyEnabled(false);
	}
	
	public void driveTrainPeriodic() {

		mainDrive.mecanumDrive_Cartesian(leftStick.getX(), leftStick.getY(), rightStick.getX(), 0);
	
	}
}
