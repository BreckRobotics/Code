
package org.usfirst.frc.team3630.robot;

import java.sql.Driver;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

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
		
		int centerSonarChannelIn; 
		int centerSonarChannelOut; 
	
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

//Autonomous**************************************************
		Timer timer;
		int autoState;
//************************************************************
				
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
    	
    	centerSonarChannelIn = 4; 
    	centerSonarChannelOut = 5; 
    	
    	timer = new Timer();
    	
    	mainDrive = new Drive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor,
    						  leftJoyPort, rightJoyPort, centerSonarChannelIn, 
    						  centerSonarChannelOut); 
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
    	
    	autoState = 0; 
    	
    	
    	
    }

    public void autonomousPeriodic() {
   
    /*	Drive forward ONLY
      if (t	< 4){
      	mainDrive.driveTrain.mecanumDrive_Cartesian(0, -0.5, 0, 0);
      }
     */
    	
    	
    /*	One tote and back up
    	double t;
    	t = timer.get();
    	
    	switch(autoState){
    	  	
    	  case 0:
    	  	timer.start();
    	 	mainMani.autoMode0();
    	  	autoState++;
    	  	break;
    	  
    	  case 1:
    	  	mainMani.autoMode1();
    	  	if(t > 3){
    	  		timer.reset();
    	  		autoState++;
    	  	}
    	  	
    	  	break;
    	 
    	  case 2:
    	 	if(t < 3){
    	 		 mainDrive.driveAuto2();
    	 	}
    	 	else{
    	 		timer.reset();
    	 		autoState++;
    	 	}
    	 	break;
    	 	
    	  case 3:
    		  mainMani.autoMode3();
    		  if (t > 3){
    			  timer.reset();
    			  autoState++;
    		}
    		break;
    		  
    	  case 4:
    		  if (t < 1){
    			  mainDrive.driveAuto3();
    		  }
    	
    	  }
    	  
    	 
    	*/
    	
    // Pick up bin and tote	
    	double t;
    	t = timer.get();
    	
    	switch(autoState){
    		
    		case 0: 
    			timer.start();
    			mainMani.autoMode0();
    			autoState++;
    		
    		break;
    			
    		case 1: 
    		mainMani.autoMode1();
    		if(t > 3.2){
    			timer.reset();
    			mainMani.stopElevator4();
    	  		autoState++;
    		} 
    		break;
    			
    			
    		case 2: 
    		if (t < 2){ 
                mainDrive.driveTrain.mecanumDrive_Cartesian(0, -0.21, 0, 0);
    
    		}
    		else{
    			timer.reset();
    	 		autoState++;
    			
    		} break;
    		
    		case 3: 
    		mainMani.autoMode2();
    		if(t > 2){
    			timer.reset();
    			mainMani.stopElevator4();
    	  		autoState++;
    		} 
    		break;
    		
    		case 4: 
    		if (t < 1){
    			mainDrive.driveTrain.mecanumDrive_Cartesian(0, 0, -0.42, 0);
    		}
    		else{
    			timer.reset();
    			autoState++;
    		} 
    		break;
    		
    		case 5:  
            	mainDrive.driveTrain.mecanumDrive_Cartesian(0, -0.5, 0, 0);
    		
            if (t == 3.5){
    			mainDrive.driveTrain.stopMotor();
    			timer.reset();
    			autoState++;
    	}
    		case 6: 
    			mainMani.autoMode3();

    		if (t > 2.5){
    			timer.reset();
    			autoState++;
    		}
    		break;
    		
    		case 7: if (t < 0.1){
				mainDrive.driveAuto3();
    		}
    	}
    			 
    }


    public void teleopPeriodic() {
    	
    	mainDrive.drivePeriodic();
    	mainMani.manipulatorPeriodic(mainDrive.dropAllHooks()); 
  
    }
    
    public void disabledInit() {
    	
    	mainDrive.stop(); 

    }
    
    
    

    public void testPeriodic() {
    
    }
    
}
