package org.usfirst.frc.team126.robot;

/**
\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		   _      ___      ____    
		 /' \   /'___`\   /'___\   
		/\_, \ /\_\ /\ \ /\ \__/   
		\/_/\ \\/_/// /__\ \  _``\ 
		   \ \ \  // /_\ \\ \ \L\ \
		    \ \_\/\______/ \ \____/
		     \/_/\/_____/   \/___/ 
	
	For reference purposes only. Will not work on *any* 2019 bot.

\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
**/
public class RobotMap {

	// Controls for Xbox 360 / Xbox One
	public static int stickY = 1; // Left stick Y
	public static int rStickY = 5; // Right stick Y
	public static int stickX = 4; // Right stick X
	public static int Rtrigger = 3; // Right trigger
	public static int Ltrigger = 2; // Left trigger
	public static int xboxA = 1; // A
	public static int xboxB = 2; // B
	public static int xboxX = 3; // X
	public static int xboxY = 4; // Y
	public static int xboxLT = 5; // Left trigger button
	public static int xboxRT = 6; // Right trigger button
	public static int xboxBack = 7; // Back
	public static int xboxStart = 8; // Start
	public static int xboxLS = 9; // Left stick button
	public static int xboxRS = 10; // Right stick button
	
	// Motor IDs (same for all robots)
	public static int frontLeft = 3;
	public static int backLeft = 4;
	public static int frontRight = 1;
	public static int backRight = 2;	
	public static int lowerArmMotor1 = 5;
	public static int lowerArmMotor2 = 6;
	
	public static int upperArmMotor1 = 7; // normal
	public static int upperArmMotor2 = 8;
	
	//public static int upperArmMotor1 = 8; // practicebot machine broke
	//public static int upperArmMotor2 = 7;
	
	public static double speedRatio;
	
	// Inversions
	public static int flinversion;
	public static int blinversion;
	public static int frinversion;
	public static int brinversion;
	public static int fbinversion;
	public static int lrinversion;
	public static int rotinversion;

	// Arm Positions 
    public static int RMupperArmRetractPos;
    public static int RMupperArmRestingPos;

    public static int RMLowerArmRestingPos;

	public static int RMlowerArmScaleTop;
	public static int RMupperArmScaleTop;
	public static int RMlowerArmSwitchTop;

	public static int RMlowerArmClimbTop;
	public static int RMupperArmClimbTop;

	public static int RMlowerArmGrabTop;
	public static int RMlowerArmGrabBottom;

	public static int RMlowerArmMax;
	public static int RMupperArmMax;
	
	public static boolean upperInverted;
	public static boolean intakeInverted;
	
	public static double upperarm1inversion;
	public static double upperarm2inversion;
	
	public static void setRobot(int robotID) {
		if(robotID == 0) { // 2017compbot
			flinversion = -1;
			blinversion = -1;
			frinversion = 1;
			brinversion = 1;
			fbinversion = 1;
			lrinversion = 1;
			rotinversion = 1;

			RMupperArmRetractPos = 500;
		    RMupperArmRestingPos = 110000;
		    
			RMlowerArmScaleTop = 108000;
			RMlowerArmSwitchTop = 40000;

			RMlowerArmClimbTop = 114000;
			RMupperArmClimbTop = 116000;

			RMlowerArmGrabTop = 800;
			RMlowerArmGrabBottom = 100;

			RMlowerArmMax = 118000;
			RMupperArmMax = 117000;
		}
		else if(robotID == 1) { // 2017practicebot
			flinversion = -1;
			blinversion = -1;
			frinversion = -1;
			brinversion = -1;
			fbinversion = 1;
			lrinversion = -1;
			rotinversion = -1;
			RMupperArmRetractPos = 500;
		    RMupperArmRestingPos = 96000;
		    
			RMlowerArmScaleTop = 108000;
			RMlowerArmSwitchTop = 40000;

			RMlowerArmClimbTop = 114000;
			RMupperArmClimbTop = 116000;

			RMlowerArmGrabTop = 800;
			RMlowerArmGrabBottom = 100;

			RMlowerArmMax = 118000;
			RMupperArmMax = 117000;		
		}
		else if(robotID == 2) { // 2018compbot
			flinversion = -1;
			blinversion = -1;
			frinversion = 1;
			brinversion = 1;
			fbinversion = 1;
			lrinversion = 1;
			rotinversion = 1;

			RMupperArmRetractPos = 500;
		    RMupperArmRestingPos = 120000;
		    
		    
			RMlowerArmScaleTop = 98000;
			RMupperArmScaleTop = 40000;
			
			RMlowerArmSwitchTop = 30000;

			RMlowerArmClimbTop = 106000;
			RMupperArmClimbTop = 90000;

			RMlowerArmGrabTop = 2000;
			RMlowerArmGrabBottom = 1500;
		    RMLowerArmRestingPos = RMlowerArmGrabBottom;

			RMlowerArmMax = 106000;
			//RMupperArmMax = 110000;
			RMupperArmMax = 122000;
			
			speedRatio = 0.83;
			upperInverted = true;
			intakeInverted = false;
			
			upperarm1inversion = 1;
			upperarm2inversion = 1;
			
			System.out.println("Init Comp Bot");
		}
		else if(robotID == 3) { // 2018practicebot
			flinversion = 1;
			blinversion = 1;
			frinversion = -1;
			brinversion = -1;
			fbinversion = 1;
			lrinversion = -1;
			rotinversion = -1;
			
			upperarm1inversion = -1;
			upperarm2inversion = 1;
		    
			RMupperArmRetractPos = 500;
		    RMupperArmRestingPos = 118000;
		    
		    
			//RMlowerArmScaleTop = 104000;
			RMlowerArmScaleTop = 95000;
			
			RMupperArmScaleTop = 75000;
			
			RMlowerArmSwitchTop = 30000;

			RMlowerArmClimbTop = 106000;
			RMupperArmClimbTop = 110000;

			RMlowerArmGrabTop = 1750;
			RMlowerArmGrabBottom = 1250;
		    RMLowerArmRestingPos = RMlowerArmGrabBottom;

			RMlowerArmMax = 106000;
			//RMupperArmMax = 110000;
			RMupperArmMax = 120000;
			speedRatio = 1;
			
			upperInverted = false;
			intakeInverted = false;
			
			RMLowerArmRestingPos = 2000;
		}	
	}
	

	

}
