package org.usfirst.frc.team3630.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick; 
import edu.wpi.first.wpilibj.Ultrasonic; 
import edu.wpi.first.wpilibj.Timer; 


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
	Ultrasonic rightSonar;
	Ultrasonic centerSonar; 
		int centerChIn; 
		int centerChOut; 
	
	Timer timer; 
	
	int autoStage; 
	
	public Drive(int frontLeftCh, int rearLeftCh, int frontRightCh, int rearRightCh,
				 int leftJoyCh, int rightJoyCh, int centerSonarChIn, int centerSonarChOut){
		
		frontLeft = frontLeftCh; 
		rearLeft = rearLeftCh; 
		frontRight = frontRightCh; 
		rearRight = rearRightCh; 
		
		leftJoy = leftJoyCh; 
		rightJoy = rightJoyCh; 
		
		centerChIn = centerSonarChIn; 
		centerChOut = centerSonarChOut; 
		
		
	}
	
	public void driveInit(){
		
		leftStick = new Joystick(leftJoy); 
		rightStick = new Joystick(rightJoy); 		
		
		
		driveTrain = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight); 
		driveTrain.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		driveTrain.setInvertedMotor(RobotDrive.MotorType.kRearRight, true); 
		driveTrain.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
		driveTrain.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
		
		centerSonar = new Ultrasonic(centerChIn, centerChOut); 
		centerSonar.setEnabled(true);
		centerSonar.setAutomaticMode(true);
		
		driveTrain.mecanumDrive_Cartesian(leftStick.getX(), leftStick.getY(), rightStick.getX(), 0);
		
		autoStage = 0; 
		
		timer = new Timer(); 
	}
	
	
	
	public void drivePeriodic(){ 
			driveTrain.mecanumDrive_Cartesian(leftStick.getX(), leftStick.getY(), rightStick.getX(), 0);
			//System.out.println(centerSonar.getRangeInches());
			
			if (leftStick.getRawButton(1) && centerSonar.getRangeInches() < 8){
				driveTrain.setMaxOutput(0.3);
			}
			else{
				driveTrain.setMaxOutput(1);
			}
}

	public void stop(){
		driveTrain.stopMotor();

		
	}
	
	
	public boolean dropAllHooks(){
		if(centerSonar.getRangeInches() > 12){
			return true; 
		}
		else{
			return false; 
		}
		
	}
	
	public void driveAuto(){
		
		double t; 
		t = timer.get(); 
		
		switch(autoStage){ 
		
		case 0:
			timer.start(); 
			if(t > 3){
				System.out.println("case0 Drive");
				autoStage++; 
			}
		break; 
		
		case 1: 
			if(centerSonar.getRangeInches() > 6){
				driveTrain.mecanumDrive_Cartesian(0, -0.5, 0, 0);
				System.out.println("case1 Drive");
			}
			else {
				stop(); 
				System.out.println("case1ELSE Drive");
				autoStage++; 
			}
		break; 
		
		case 2: 
			timer.reset(); 
			if(t > 2){
				driveTrain.mecanumDrive_Cartesian(-0.75, 0, 0, 0);
				System.out.println("case2 Drive");
			}
			else if(t > 5){
				stop();
				System.out.println("case2ELSE Drive");
				autoStage++; 
			}
		break; 
		
		case 3: 
			timer.reset(); 
			if(t > 3){
				driveTrain.mecanumDrive_Cartesian(0, 0.5, 0, 0);
				System.out.println("case3 Drive");
			}
		break; 

		}
			System.out.println("Exit Drive");
	}
	
	public void driveAuto2(){
		
		double t; 
		t = timer.get();
		timer.start();
		
		if (t < 3){
			driveTrain.mecanumDrive_Cartesian(0, 0.5, 0, 0);
		}
		else{
			stop();
		}
	}
	
	public void driveAuto3(){
		driveTrain.mecanumDrive_Cartesian(0, 0.5, 0, 0);
	}

}
