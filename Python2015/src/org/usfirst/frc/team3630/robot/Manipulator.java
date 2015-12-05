package org.usfirst.frc.team3630.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DigitalInput; 


public class Manipulator {
	
	boolean mode; 
	int i;
	
	DigitalInput limitSwitchUpper; 
	DigitalInput limitSwitchLower; 
		int lowerLimitChannel; 
		int upperLimitChannel; 
	
	
	Joystick controlJoystick; 
		int controlJoy; 
	
	
	Elevator[] elevators;
		// PWM Assignments for elevators 
		int elevator1Channel;
		int elevator2Channel;
		int elevator3Channel; 
		int elevator4Channel; 
	
		// DIO Assignments for elevator encoders 
		int encoder1ChannelA; 
		int encoder1ChannelB; 
	
		int encoder2ChannelA; 
		int encoder2ChannelB; 
	
		int encoder3ChannelA; 
		int encoder3ChannelB; 
	
		int encoder4ChannelA; 
		int encoder4ChannelB; 
		
		boolean elevator1Reversed;
		boolean elevator2Reversed;
		boolean elevator3Reversed;
		boolean elevator4Reversed; 
	
	
	public Manipulator(int ele1Ch, int ele2Ch, int ele3Ch, int ele4Ch, 
					   int en1ChA, int en1ChB, int en2ChA, int en2ChB, 
					   int en3ChA, int en3ChB, int en4ChA, int en4ChB, 
					   int controlJoyCh, int upperCh, int lowerCh,
					   boolean ele1Rev, boolean ele2Rev, boolean ele3Rev, boolean ele4Rev){
		
		elevator1Channel = ele1Ch; 
		elevator2Channel = ele2Ch; 
		elevator3Channel = ele3Ch; 
		elevator4Channel = ele4Ch; 
		
		encoder1ChannelA = en1ChA;
		encoder1ChannelB = en1ChB;
		
		encoder2ChannelA = en2ChA;
		encoder2ChannelB = en2ChB;
		
		encoder3ChannelA = en3ChA;
		encoder3ChannelB = en3ChB;
		
		encoder4ChannelA = en4ChA;
		encoder4ChannelB = en4ChB;
		
		controlJoy = controlJoyCh; 
		
		upperLimitChannel = upperCh; 
		lowerLimitChannel = lowerCh; 
		
		elevator1Reversed = ele1Rev;
		elevator2Reversed = ele2Rev; 
		elevator3Reversed = ele3Rev; 
		elevator4Reversed = ele4Rev; 
	}
	
	public void manipulatorInit(){
		
		elevators = new Elevator[5]; 
		elevators[1] = new Elevator(encoder1ChannelA, encoder1ChannelB, elevator1Channel, elevator1Reversed); 
		elevators[2] = new Elevator(encoder2ChannelA, encoder2ChannelB, elevator2Channel, elevator2Reversed); 
		elevators[3] = new Elevator(encoder3ChannelA, encoder3ChannelB, elevator3Channel, elevator3Reversed); 
		elevators[4] = new Elevator(encoder4ChannelA, encoder4ChannelB, elevator4Channel, elevator4Reversed); 
		
		elevators[1].elevatorInit();
		elevators[2].elevatorInit(); 
		elevators[3].elevatorInit(); 
		elevators[4].elevatorInit(); 
		
		controlJoystick = new Joystick(controlJoy); 
		
		limitSwitchUpper = new DigitalInput(upperLimitChannel); 
		limitSwitchLower = new DigitalInput(lowerLimitChannel); 
		
		mode = true; 
		i = 4; 
	}
	
	
	public void manipulatorPeriodic(){
		
		if(controlJoystick.getRawButton(9)){
			elevators[1].encReset(); 
			elevators[2].encReset(); 
			elevators[3].encReset(); 
			elevators[4].encReset(); 
		}
		
		if(controlJoystick.getRawButton(7)){
			mode = true;
		}
		else if(controlJoystick.getRawButton(8)){
			mode = false; 
		}
	
	 
		if(mode){
			moveElevator(); 
			if(elevators[4].getElevatorStatus() == true && !elevators[3].getElevatorStatus() && !elevators[2].getElevatorStatus() && !elevators[1].getElevatorStatus()){
				elevators[4].moveToTop(limitSwitchUpper.get()); 
			}		

			if(elevators[3].getElevatorStatus() == true && !elevators[4].getElevatorStatus() && !elevators[2].getElevatorStatus() && !elevators[1].getElevatorStatus()){
				elevators[3].moveTo(38);
			}

			if(elevators[2].getElevatorStatus() == true && !elevators[3].getElevatorStatus() && !elevators[4].getElevatorStatus() && !elevators[1].getElevatorStatus()){
				elevators[2].moveTo(28);
			}
	
			if(elevators[1].getElevatorStatus() == true && !elevators[3].getElevatorStatus() && !elevators[2].getElevatorStatus() && !elevators[4].getElevatorStatus()){
				elevators[1].moveTo(9);
			}
			if(elevators[1].getElevatorStatus() == true && elevators[2].getElevatorStatus() == true && 
			   elevators[3].getElevatorStatus() == true && elevators[4].getElevatorStatus() == true){
				
				elevators[1].moveToBottom(limitSwitchLower.get());
				
			}
			if(!elevators[1].getElevatorStatus() && elevators[2].getElevatorStatus() == true && 
			    elevators[3].getElevatorStatus() == true && elevators[4].getElevatorStatus() == true){
				
				elevators[2].moveTo(0);
				
			}
			if(!elevators[1].getElevatorStatus() && !elevators[2].getElevatorStatus() && 
			    elevators[3].getElevatorStatus() == true && elevators[4].getElevatorStatus() == true){
				
				elevators[3].moveTo(0);
				
			}
			if(!elevators[1].getElevatorStatus() && !elevators[2].getElevatorStatus() && 
			   !elevators[3].getElevatorStatus() && elevators[4].getElevatorStatus() == true && !limitSwitchLower.get()){
				
				elevators[4].moveTo(0); 
				
			}
			
			
			
			
			
		}

		else{	
			moveElevator(); 
			if(controlJoystick.getRawButton(5) && !controlJoystick.getRawButton(6)){
				elevators[i].moveDown();
			}
			else if(controlJoystick.getRawButton(6) && !controlJoystick.getRawButton(5)){
				elevators[i].moveUp(); 
			}
			else{
				elevators[i].stopDrive(); 
			}
		}
	}
					   
	public int moveElevator(){ 
		if(controlJoystick.getRawButton(1)){
			if(mode){
				elevators[1].setElevatorStatus(true);
			} 
			i = 1;
		}
		if(controlJoystick.getRawButton(2)){
			if(mode){
				elevators[2].setElevatorStatus(true);
			}
			i = 2;
		}
		if(controlJoystick.getRawButton(3)){
			if(mode){
				elevators[3].setElevatorStatus(true);
			}
			i = 3;
		}
		if(controlJoystick.getRawButton(4)){
			if(mode){
				elevators[4].setElevatorStatus(true); 
			}
			i = 4;
		}
		if(controlJoystick.getRawButton(10)){
			if(mode){
				elevators[4].setElevatorStatus(true);
				elevators[3].setElevatorStatus(true);
				elevators[2].setElevatorStatus(true);
				elevators[1].setElevatorStatus(true);
			}
			
			
		}
		
		return i; 
	}
	

}
