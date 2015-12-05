package org.usfirst.frc.team3630.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Manipulator {

	SmartDashboard dashboard;
	
	Joystick manipulatorStick;
	
	int elevator1MotorChannel;
	boolean elevator1Reversal;
	int elevator2MotorChannel;
	boolean elevator2Reversal;
	int elevator3MotorChannel;
	boolean elevator3Reversal;
	int elevator4MotorChannel;
	boolean elevator4Reversal;
	
	int selectedMotor;

	
	
	// Every one revolution = 15.70 inches 
	// One revolution = 90 ticks 
	// 1 tick = .175 inches 
	
	boolean elevator4up; 
	boolean elevator4down; 
	boolean elevator3up;
	boolean elevator3down;
	boolean elevator2up;
	boolean elevator2down; 
	boolean elevator1up; 
	boolean elevator1down; 
	
	int encoder1ChA; 
	int encoder1ChB; 
	
	int encoder2ChA; 
	int encoder2ChB; 
	
	int encoder3ChA; 
	int encoder3ChB; 
	
	int encoder4ChA; 
	int encoder4ChB; 
	
	
	int limitSwitchUpperChannel;
	int limitSwitchLowerChannel;
	
	DigitalInput limitSwitchUpper;
	DigitalInput limitSwitchLower;
	
	Elevator[] elevators;
	
	//Controls
	int manualElevatorActive;
	
	boolean elevator1Moving;
	boolean elevator2Moving;
	boolean elevator3Moving;
	boolean elevator4Moving;
	
	int manipulatorMode; 
	
	
	public Manipulator(int channel1, int channel2, 
					   int channel3, int channel4, 
					   boolean el1Rev, boolean el2Rev,
					   boolean el3Rev, boolean el4Rev,
					   int enc1ChA, int enc1ChB, 
					   int enc2ChA, int enc2ChB, 
					   int enc3ChA, int enc3ChB, 
					   int enc4ChA, int enc4ChB, 
					   Joystick mStick, 
					   int limUCh, int limLCh,
					   SmartDashboard dashb) {
		
		dashboard = dashb;
		
		manipulatorStick = mStick;
		
		elevator1MotorChannel = channel1;
		elevator1Reversal = el1Rev;
		elevator2MotorChannel = channel2;
		elevator2Reversal = el2Rev;
		elevator3MotorChannel = channel3;
		elevator3Reversal = el3Rev;
		elevator4MotorChannel = channel4;
		elevator4Reversal = el4Rev;
		
		encoder1ChA = enc1ChA; 
		encoder1ChB = enc1ChB; 
		
		encoder2ChA = enc2ChA; 
		encoder2ChB = enc2ChB; 
		
		encoder3ChA = enc3ChA; 
		encoder3ChB = enc3ChB; 
		
		encoder4ChA = enc4ChA; 
		encoder4ChB = enc4ChB; 
		
		
		limitSwitchUpperChannel = limUCh;
		limitSwitchLowerChannel = limLCh;
		
	}
	
	
	public void manipulatorInit() {

		elevators = new Elevator[5];
		elevators[1] = new Elevator(elevator1MotorChannel, elevator1Reversal, dashboard, encoder1ChA, encoder1ChB, false);
		elevators[2] = new Elevator(elevator2MotorChannel, elevator2Reversal, dashboard, encoder2ChA, encoder2ChB, false);
		elevators[3] = new Elevator(elevator3MotorChannel, elevator3Reversal, dashboard, encoder3ChA, encoder3ChB, false);
		elevators[4] = new Elevator(elevator4MotorChannel, elevator4Reversal, dashboard, encoder4ChA, encoder4ChB, false);
		
		elevators[1].elevatorInit();
		elevators[2].elevatorInit();
		elevators[3].elevatorInit();
		elevators[4].elevatorInit();
		
		limitSwitchUpper = new DigitalInput(limitSwitchUpperChannel); 
		limitSwitchLower = new DigitalInput(limitSwitchLowerChannel); 
		
		
		
		manualElevatorActive = 1; //for manual controls
		
		elevator1Moving = false;
		elevator2Moving = false;
		elevator3Moving = false;
		elevator4Moving = false;
		
		elevator4up = true; 
		elevator4down = false; 
		elevator3up = true; 
		elevator3down = false; 
		elevator2up = true; 
		elevator2down = false; 
		elevator1up = true; 
		elevator1down = false; 
		
		selectedMotor = 4;
		
		manipulatorMode = 1; 
		
	}	
		
		
	

	public void manipulatorPeriodic() {
		
		selectManMode(); 
		
		 		
	}
	
	public void selectMotor(){
		if(manipulatorStick.getRawButton(1)){
			selectedMotor = 1;
		}
		else if(manipulatorStick.getRawButton(2)){
			selectedMotor = 2;
		}
		else if(manipulatorStick.getRawButton(3)){
			selectedMotor = 3;
		}
		else if(manipulatorStick.getRawButton(4)){
			selectedMotor = 4; 
		}
		

		
	}
	
	public void manualControl(){
		
		switch(selectedMotor) {
		
		case 4:
			elevators[manualElevatorActive].stop();
			manualElevatorActive = 4;
		break;
		
		case 3: 
			elevators[manualElevatorActive].stop();
			manualElevatorActive = 3;
		break;
		
		case 2: 
			elevators[manualElevatorActive].stop();
			manualElevatorActive = 2; 
		break;
		
		case 1: 
			elevators[manualElevatorActive].stop();
			manualElevatorActive = 1;
		break;
			
		}
		
		//Manual controls - move active elevator
		if(manipulatorStick.getRawButton(8) && !manipulatorStick.getRawButton(6)) {
			elevators[manualElevatorActive].moveUp();
		}
		else if(manipulatorStick.getRawButton(6) && !manipulatorStick.getRawButton(8)) {
			elevators[manualElevatorActive].moveDown();
		}
		else {
			elevators[manualElevatorActive].stop();  
		}
		
	}
	
	public void automaticControl(){
		
		if(manipulatorStick.getRawButton(5) && !manipulatorStick.getRawButton(7)){
			elevators[manualElevatorActive].moveDown(); 
		}
		else {
			elevators[manualElevatorActive].stop(); 
		}
				
	}
	
	public void selectManMode(){
		
		if(manipulatorStick.getRawButton(9)){
			manipulatorMode = 1; 	
		}
		else if(manipulatorStick.getRawButton(10)){
			manipulatorMode = 2; 
		}
		
		switch(manipulatorMode){ 
		
		case 1: 
			automaticControl(); 
		break; 
		
		case 2: 
			selectMotor(); 
			manualControl(); 
		break; 
		}
		
	}
	
	public void gimmeTheGoods(){
		System.out.println(limitSwitchUpper.get());
		System.out.println(limitSwitchLower.get());
	}
	
	
	
	
	
	
	//functions for checking if hooks are in the right place already
	
	
}
