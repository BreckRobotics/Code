package org.usfirst.frc.team3630.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder; 

public class Elevator {
	
	SmartDashboard dashboard;
	
	int motorChannel;
	boolean motorReversal;
	
	boolean encoderReversal; 
	
	int encoderChA; 
	int encoderChB; 
	
	Encoder encoder; 
	
	
	
	Talon motor;

	
	
	public Elevator(int channel, boolean motorRev, SmartDashboard dashb, int encChA, int encChB, boolean encoderRev) {
		
		dashboard = dashb;
		
		motorChannel = channel;
		motorReversal = motorRev;
		
		encoderChA = encChA; 
		encoderChB = encChB; 
		
		encoderReversal = encoderRev; 
		
	}

	public void elevatorInit() {
		
		motor = new Talon(motorChannel);
		encoder = new Encoder(encoderChA, encoderChB); 
		
		encoder.setDistancePerPulse(0.0175);
		encoder.setReverseDirection(encoderReversal); 
		
	}
	
	public void moveUp() {
		if(!motorReversal) {
			motor.set(1.0);
		}
		else {
			motor.set(-1.0);
		}
		
		System.out.println(motor.get());
		System.out.println(motorReversal); 
		
		
	}
	
	public void moveDown() {
		if(!motorReversal) {
			motor.set(-1.0);
		}
		else {
			motor.set(1.0);
		}
		
		System.out.println(motor.get()); 
		System.out.println(motorReversal); 
	}
	
	public double distanceTraveled() {
		return encoder.getDistance();
	}
	
	public void stop() {
		motor.stopMotor();
	}
	
}
