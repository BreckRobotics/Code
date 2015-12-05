package org.usfirst.frc.team3630.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Ultrasonic;

public class DriveTrain {
	
	SmartDashboard dashboard;
	
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
	
	int sonarLeftChannelIn;
	int sonarLeftChannelOut;
	int sonarRightChannelIn;
	int sonarRightChannelOut; 
	int sonarCenterChannelIn; 
	int sonarCenterChannelOut; 
	
	int drivingStage; 
	
	Ultrasonic leftSonar;
	Ultrasonic rightSonar; 
	Ultrasonic centerSonar; 
	
	public DriveTrain(int lfChannel, int lrChannel, 
					  int rfChannel, int rrChannel, 
					  Joystick lStick, Joystick rStick, 
					  int sonarLeftChIn, int sonarLeftChOut, 
					  int sonarRightChIn, int sonarRightChOut,
					  int sonarCenterChIn, int sonarCenterChOut, 
					  SmartDashboard dashb) {
		
		LeftFrontChannel = lfChannel;
		LeftRearChannel = lrChannel;
		RightFrontChannel = rfChannel;
		RightRearChannel = rrChannel;
		
		leftStick = lStick;
		rightStick = rStick;
		
		sonarLeftChannelIn = sonarLeftChIn;
		sonarLeftChannelOut = sonarLeftChOut;
		sonarRightChannelIn = sonarRightChIn; 
		sonarRightChannelOut = sonarRightChOut; 
		sonarCenterChannelIn = sonarCenterChIn; 
		sonarCenterChannelOut = sonarCenterChOut; 
		
		dashboard = dashb;
	}
	
	public void driveTrainInit() {
		
		mainDrive = new RobotDrive(LeftFrontChannel, LeftRearChannel, 
								   RightFrontChannel, RightRearChannel);
		mainDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
		mainDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
		mainDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		mainDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		
		leftSonar = new Ultrasonic(sonarLeftChannelIn, sonarLeftChannelOut);
		rightSonar = new Ultrasonic(sonarRightChannelIn, sonarRightChannelOut); 
		centerSonar = new Ultrasonic(sonarCenterChannelIn, sonarCenterChannelOut); 
		leftSonar.setEnabled(true);
		leftSonar.setAutomaticMode(true);
		rightSonar.setEnabled(true);
		rightSonar.setAutomaticMode(true); 
		centerSonar.setEnabled(true);
		centerSonar.setAutomaticMode(true);
		
		mainDrive.mecanumDrive_Cartesian(leftStick.getX(), leftStick.getY(), rightStick.getX(), 0);
		
		drivingStage = 6; 
		
		mainDrive.setSafetyEnabled(true);

		
	}
	
	public void driveTrainPeriodic() {

		/*
		if(leftStick.getRawButton(1)){
			approachTote(); 
		}
		else {
			mainDrive.setMaxOutput(1);
			mainDrive.mecanumDrive_Cartesian(leftStick.getX(), leftStick.getY(), rightStick.getX(), 0);
		}
		*/
		
		
		mainDrive.mecanumDrive_Cartesian(leftStick.getX(), leftStick.getY(), rightStick.getX(), 0);
		
	/*	
		
		
		if(centerSonar.getRangeInches() < 45){
			mainDrive.setMaxOutput(0.8);
		}
		else if(centerSonar.getRangeInches() < 30){
			mainDrive.setMaxOutput(0.65);
		}
		else if(centerSonar.getRangeInches() < 15){
			mainDrive.setMaxOutput(0.5); 
		}
		else if(centerSonar.getRangeInches() < 10){
			mainDrive.setMaxOutput(0.2); 
		}
		else if(centerSonar.getRangeInches() < 5){
			mainDrive.setMaxOutput(0.05);
		}
		else if(centerSonar.getRangeInches() > 50){
			mainDrive.setMaxOutput(1); 
		}
		
	*/	
	/*	
		if(leftStick.getRawButton(1)){
			approachStage(); 
			
			switch(drivingStage){
			
			
			case 1: 
				mainDrive.setMaxOutput(0.8);
			break;
			
			case 2: 
				mainDrive.setMaxOutput(0.65);
			break; 
			
			case 3: 
				mainDrive.setMaxOutput(0.5);
			break; 
			
			case 4: 
				mainDrive.setMaxOutput(0.2); 
			break; 
			
			case 5: 
				mainDrive.setMaxOutput(0.05);
			break; 
			
			case 6:
				mainDrive.setMaxOutput(1); 
			break; 
			}
		}
			
		else {
			mainDrive.mecanumDrive_Cartesian(leftStick.getX(), leftStick.getY(), rightStick.getX(), 0);
		}
		*/
		
	
	}
	
	public void approachStage(){
		
		if(centerSonar.getRangeInches() < 45){
			drivingStage = 1;
		}
		else if(centerSonar.getRangeInches() < 30){
			drivingStage = 2; 
		}
		else if(centerSonar.getRangeInches() < 15){
			drivingStage = 3; 
		}
		else if(centerSonar.getRangeInches() < 10){
			drivingStage = 4; 
		}
		else if(centerSonar.getRangeInches() < 5){
			drivingStage = 5;
		}
		else if(centerSonar.getRangeInches() > 50){
			drivingStage = 6; 
		}
		
		
	}
	
	
	public void approachTote(){
		
		double kP = 0.2; 
		double minSpeed = 0.1; 
		double maxSpeed = 0.3; 
		double distance = (centerSonar.getRangeInches() + leftSonar.getRangeInches() + rightSonar.getRangeInches())/3; 
		if(distance != 10){
			while(true){
				distance = 10 - centerSonar.getRangeInches(); 
				double p = distance * kP; 
				
				if(Math.abs(p) > maxSpeed){
					p = maxSpeed * (Math.abs(p)/p);
				}
				else if(Math.abs(p) < minSpeed){
					p = 0; 
				}
				
				//mainDrive.mecanumDrive_Cartesian(0, p, rightStick.getX(), 0);
				
				mainDrive.setMaxOutput(p);
				//mainDrive.mecanumDrive_Cartesian(leftStick.getX(), leftStick.getY(), rightStick.getX(), 0);
				
				if(p == 0){
					break; 
				}
			}
		}		
	}
	
	public void gimmeTheGoods(){
		
		System.out.println(centerSonar.getRangeInches()); 
		System.out.println(leftSonar.getRangeInches());
		System.out.println(rightSonar.getRangeInches()); 
	
	}
	
	
}

	
		


	
	
	

