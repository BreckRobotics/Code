package org.usfirst.frc.team3630.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder; 


public class Elevator {
	
	boolean keepMotorMoving; 
	
	boolean isMotorReversed; 
	
	boolean goingUp; 
	
	int motorReversed; 
	
	// PWM Assignment for elevator Talon
	Talon elevatorDrive;
		int elevatorChannel; 
	
	// DIO Assignments for elevator encoder 
	Encoder elevatorEncoder; 
		int encoderChA; 
		int encoderChB; 
	
	double error; 
	
		
		
	public Elevator(int encChA, int encChB, int elevatorCh, boolean reversal){
		
		// DIO Channels for elevator encoder...
		//...values are defined in Robot.java 
		encoderChA = encChA; 
		encoderChB = encChB; 
		
		// PWM Channel for elevator Talon...
		//...values are defined in Robot.java 
		elevatorChannel = elevatorCh; 
		
		isMotorReversed = reversal; 
	}
	
	
	public void elevatorInit(){
		
		elevatorDrive = new Talon(elevatorChannel); 
		elevatorEncoder = new Encoder(encoderChA, encoderChB); 
		elevatorEncoder.setDistancePerPulse(0.149);
		elevatorEncoder.reset();
		keepMotorMoving = false;
		goingUp = true; 
		
		
	}
	
	public void moveTo(double targetHeight){

		if(distanceTraveled() < targetHeight && calcErr(targetHeight) > 1){
			elevatorDrive.set(1 * motorReversed(isMotorReversed)); 
		}
		else if(distanceTraveled() > targetHeight && calcErr(targetHeight) > 1){
			elevatorDrive.set(-1 * motorReversed(isMotorReversed));
		}
		else{
			elevatorDrive.set(0); 
			keepMotorMoving = false; 
			if(targetHeight == 0){
				encReset(); 
				goingUp = true; 
			}
		}
	}
	
	public void moveToTop(boolean reachedTop){
		
		if(reachedTop){
			elevatorDrive.set(1 * motorReversed(isMotorReversed));
		}
		else{
			elevatorDrive.set(0);
			setElevatorStatus(false);
			goingUp = false; 
		}
		
	}
	
	public void moveToBottom(boolean reachedBottom){
		
		if(reachedBottom){
			elevatorDrive.set(-1 * motorReversed(isMotorReversed)); 
		}
		else{
			elevatorDrive.set(0);
			setElevatorStatus(false); 
		}
			
	}
	
	
	public double calcErr(double x){
		error = Math.abs(distanceTraveled() - x);
	//	System.out.println(error);
		return error; 
	}
	
	
	public void moveUp(){
		elevatorDrive.set(1 * motorReversed(isMotorReversed));
		
	}
	
	public void moveDown(){
		elevatorDrive.set(-1 * motorReversed(isMotorReversed));
		
	}
	
	public void stopDrive(){
		elevatorDrive.stopMotor(); 
		
	}
	
	public void encReset(){ 
		elevatorEncoder.reset(); 
		
	}
	
	public double distanceTraveled(){
	//	System.out.println(Math.abs(elevatorEncoder.getDistance()));
		return Math.abs(elevatorEncoder.getDistance()); 
		
	}
	
	public boolean getElevatorStatus(){
		return keepMotorMoving; 
	}
	
	public void setElevatorStatus(boolean status){
		keepMotorMoving = status; 
		
	}
	
	public int motorReversed(boolean reversal){
		if(reversal){
			motorReversed = -1; 
		}
		else if(!reversal){
			motorReversed = 1; 
		}
		return motorReversed; 	
	}
	
	public boolean elevatorGoingUp(){
		return goingUp; 
		
	}
	

}
