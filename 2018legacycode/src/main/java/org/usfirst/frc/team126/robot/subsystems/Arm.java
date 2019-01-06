package org.usfirst.frc.team126.robot.subsystems;

import org.usfirst.frc.team126.robot.Robot;
import org.usfirst.frc.team126.robot.RobotMap;
import org.usfirst.frc.team126.robot.commands.ArmControl;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 */
public class Arm extends Subsystem {
	public static double drive_speed=.30;
	public static int drive_loops = 60;

	public static double scale_drive_speed=.30;
	public static int scale_drive_loops = 50;

	double current_limit = 100.0;
	double current_max = 0;
	
	int start_count=0;
	
	int block_count=0;

	// Debug Speed
	//public static double armSpeed = 0.3;
	// Game Speed
	public static double armSpeed = 1;

    public static int upperArmRetractPos;
    public static int upperArmRestingPos;
    
	public static int lowerArmScaleTop;
	public static int lowerArmSwitchTop;

	public static int lowerArmClimbTop;
	public static int upperArmClimbTop;

	public static int lowerArmGrabTop;
	public static int lowerArmGrabBottom;

	public static int lowerArmMax;
	public static int upperArmMax;

	TalonSRX lowerArmMotor1 = new TalonSRX(RobotMap.lowerArmMotor1);
	TalonSRX lowerArmMotor2 = new TalonSRX(RobotMap.lowerArmMotor2);
	TalonSRX upperArmMotor1 = new TalonSRX(RobotMap.upperArmMotor1);
	TalonSRX upperArmMotor2 = new TalonSRX(RobotMap.upperArmMotor2);


	
    DigitalInput upperArmBackwardsLimitSwitch = new DigitalInput(0);
    DigitalInput upperArmForwardsLimitSwitch = new DigitalInput(1);
    DigitalInput lowerArmBackwardsLimitSwitch = new DigitalInput(2);
    DigitalInput lowerArmForwardsLimitSwitch = new DigitalInput(3);

	boolean arm_active = false;
	boolean arm_needs_move = false;
	boolean bypass_limits = false;
	boolean arms_zeroed = false;
	
	boolean upper_zeroed = false;
	boolean lower_zeroed = false;
	
	boolean blockUpperExtend = false;
	boolean blockUpperRetract = false;
	boolean blockLowerExtend = false;
	boolean blockLowerRetract = false;
	
	boolean driving_back = false;
	boolean driving_forward = false;
	boolean pause_before_open = false;
	boolean pause_active = false;
	
    //boolean waiting=false;
	int wait_loops=0;

	public static enum arm_position {
		arm_initial, arm_switch, arm_scale, arm_exchange, arm_climb,
		arm_scale_place, arm_switch_place, arm_grab_cube, arm_secure_cube,
		arm_scale_place_nodrive, arm_switch_place_nodrive, arm_drop_cube,
		arm_spit_cube, arm_high_scale
	}

	public static enum arm_states {
		begin, retractUpperFirst, raiseLowerArm, extendUpper, driveForward, 
		openGrabber, driveBackwards, pushCube, retactUpperSecond, 
		lowerLowerArm, resetUpperArm, doneState, driveBackwardsAgain,
		parkLowerArm, closeGrabber, armSpitCube
	}

	arm_states state = Arm.arm_states.begin;
	arm_position targetArmPosition;
    arm_position startingArmPosition;
    
	public Arm () {
	   targetArmPosition = Arm.arm_position.arm_initial;
	   startingArmPosition = Arm.arm_position.arm_initial;
	   
	   arm_active = false;
	   arm_needs_move = false;
	   arms_zeroed = false;
	   lower_zeroed = false;
	   upper_zeroed = false;

	   getLowerArmMotor(1).setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20, 50);
	   getUpperArmMotor(1).setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20, 50);

	   //getLowerArmMotor(1).configSelectedFeedbackSensor(FeedbackDevice.PulseWidthEncodedPosition, 0, 50);
	   //getUpperArmMotor(1).configSelectedFeedbackSensor(FeedbackDevice.PulseWidthEncodedPosition, 0, 50);

	//	int upperPos = getUpperArmMotor(1).getSelectedSensorPosition(0);
	//	int lowerPos = getLowerArmMotor(1).getSelectedSensorPosition(0);

        //SmartDashboard.putNumber("PWE LowerPOSITION",lowerPos);
        //SmartDashboard.putNumber("PWE UpperPOSITION",upperPos);

       getLowerArmMotor(1).configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 50);
	   getUpperArmMotor(1).configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 50);

	    int upperPos = getUpperArmMotor(1).getSelectedSensorPosition(0);
		int lowerPos = getLowerArmMotor(1).getSelectedSensorPosition(0);

       //SmartDashboard.putNumber("MagAbs LowerPOSITION",lowerPos);
       //SmartDashboard.putNumber("MagAbs UpperPOSITION",upperPos);
		
		armInitMapValues();
	}
	
	public void armInitMapValues() {
       upperArmRetractPos = RobotMap.RMupperArmRetractPos;
       upperArmRestingPos = RobotMap.RMupperArmRestingPos;
       
   	   lowerArmScaleTop = RobotMap.RMlowerArmScaleTop;
   	   lowerArmSwitchTop = RobotMap.RMlowerArmSwitchTop;

   	   lowerArmClimbTop = RobotMap.RMlowerArmClimbTop;
   	   upperArmClimbTop = RobotMap.RMupperArmClimbTop;

   	   lowerArmGrabTop = RobotMap.RMlowerArmGrabTop;
   	   lowerArmGrabBottom = RobotMap.RMlowerArmGrabBottom;

   	   lowerArmMax = RobotMap.RMlowerArmMax;
   	   upperArmMax = RobotMap.RMupperArmMax;
	}

	public DigitalInput getUpperArmBackwardsLimitSwitch() {
		return upperArmBackwardsLimitSwitch;
	}

	public DigitalInput getLowerArmBackwardsLimitSwitch() {
		return lowerArmBackwardsLimitSwitch;
	}
	public DigitalInput getUpperArmForwardsLimitSwitch() {
		return upperArmForwardsLimitSwitch;
	}

	public DigitalInput getLowerArmForwardsLimitSwitch() {
		return lowerArmForwardsLimitSwitch;
	}

	public void zeroArms() {
		arms_zeroed=true;
		getUpperArmMotor(1).setSelectedSensorPosition(-1000,0,100);
		getLowerArmMotor(1).setSelectedSensorPosition(-100,0,100);
	}
	
	public boolean getArmsZeroed() {
		return(arms_zeroed);
	}

	public boolean getUpperZeroed() {
		return(upper_zeroed);
	}

	public void setUpperZeroed(boolean val) {
		upper_zeroed = val;
	}

	public boolean getLowerZeroed() {
		return(lower_zeroed);
	}

	public void setLowerZeroed(boolean val) {
		lower_zeroed = val;
	}

	public void setArmsZeroed(boolean zeroed) {
		arms_zeroed=zeroed;
	}

	public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ArmControl());
    }

    public void doScale() {
    	if (!arms_zeroed) { return; }
    	if ( isArmActive() && !DSdata.isAutonomous()) { return; }
    	setArmActive(true);
    	
        targetArmPosition = Arm.arm_position.arm_scale;
        state = Arm.arm_states.begin;
    }
    public void doHighScale() {
    	if (!arms_zeroed) { return; }
    	if ( isArmActive() && !DSdata.isAutonomous()) { return; }
    	setArmActive(true);
    	
        targetArmPosition = Arm.arm_position.arm_high_scale;
        state = Arm.arm_states.begin;
    }

    public void doScalePlace() {
    	if (!arms_zeroed) { return; }
    	if ( isArmActive() ) { return; }
    	setArmActive(true);
    	setArmNeedsMove(true);
        Robot.driveBase.Drive(0, 0, 0,1);
    	
        targetArmPosition = Arm.arm_position.arm_scale_place;
        state = Arm.arm_states.begin;
    }

    public void doScalePlaceNoDrive() {
    	if (!arms_zeroed) { return; }
    	if ( isArmActive() ) { return; }
    	setArmActive(true);
    	setArmNeedsMove(false);
        Robot.driveBase.Drive(0, 0, 0,1);
    	
        targetArmPosition = Arm.arm_position.arm_scale_place_nodrive;
        state = Arm.arm_states.begin;
    }

    public void doCubeGrab() {
    	if (!arms_zeroed) { return; }
    	if ( isArmActive() && !DSdata.isAutonomous()) { return; }
    	setArmActive(true);
    	setArmNeedsMove(false);
    	
	    targetArmPosition = Arm.arm_position.arm_grab_cube;
	    state = Arm.arm_states.begin;
	}

    public void doCubeSecure() {
    	if (!arms_zeroed) { return; }
    	if ( isArmActive() ) { return; }
    	setArmActive(true);
    	setArmNeedsMove(false);
    	
	        targetArmPosition = Arm.arm_position.arm_secure_cube;
	        state = Arm.arm_states.begin;
    }

    public void doDropCube() {
    	if (!arms_zeroed) { return; }
    	if ( isArmActive() && !DSdata.isAutonomous() ) { return; }
    	setArmActive(true);
    	setArmNeedsMove(false);
    	
	        targetArmPosition = Arm.arm_position.arm_drop_cube;
	        state = Arm.arm_states.begin;
    }

    public void doSpitCube() {
    	if (!arms_zeroed) { return; }
    	if ( isArmActive() && !DSdata.isAutonomous() ) { return; }
    	setArmActive(true);
    	setArmNeedsMove(false);
    	
	        targetArmPosition = Arm.arm_position.arm_spit_cube;
	        state = Arm.arm_states.begin;
    }

    
    public void doClimb() {
    	if (!arms_zeroed) { return; }
    	if ( isArmActive() ) { return; }
    	setArmActive(true);
        Robot.driveBase.Drive(0, 0, 0,1);
    	
        targetArmPosition = Arm.arm_position.arm_climb;
        state = Arm.arm_states.begin;
    }

    public void doSwitch() {
    	if (!arms_zeroed) { 
    		 System.out.println("doSwitch !arms_zeroed)");
    		 return; 
        }
    	if ( isArmActive() && !DSdata.isAutonomous() ) { 
     		 System.out.println("doSwitch !isArmActive");
    		 return; 
        }
    	setArmActive(true);
    	setArmNeedsMove(false);
    	
        targetArmPosition = Arm.arm_position.arm_switch;
        state = Arm.arm_states.begin;
        
    }

    public void doSwitchPlace() {
    	if (!arms_zeroed) { return; }
    	if ( isArmActive() ) { return; }
    	setArmActive(true);
    	setArmNeedsMove(true);
        Robot.driveBase.Drive(0, 0, 0,1);
    	
        targetArmPosition = Arm.arm_position.arm_switch_place;
        state = Arm.arm_states.begin;
    }

    public void doSwitchPlaceNoDrive() {
    	if (!arms_zeroed) { return; }
    	if ( isArmActive() ) { return; }
    	setArmActive(true);
    	setArmNeedsMove(false);
        Robot.driveBase.Drive(0, 0, 0,1);
    	
        targetArmPosition = Arm.arm_position.arm_switch_place_nodrive;
        state = Arm.arm_states.begin;
    }

    public void doExchange() {
    	if (!arms_zeroed) { return; }
    	if ( isArmActive() ) { return; }
    	setArmActive(true);
    	setArmNeedsMove(false);
    	
        targetArmPosition = Arm.arm_position.arm_exchange;
        state = Arm.arm_states.begin;
    }
  
    private double checkLowerCurrent(double speed) {
		double current = Robot.arm.getLowerArmMotor(1).getOutputCurrent();
		double current2 = Robot.arm.getLowerArmMotor(2).getOutputCurrent();

		//SmartDashboard.putBoolean("blockLowerExtend",blockLowerExtend);
		//SmartDashboard.putBoolean("blockLowerRetract",blockLowerRetract);
		
		if (current > 50) { System.out.println("Current: " + current); }
		if (current > current_limit || current2 > current_limit) {
			/*if (speed > 0) { 
				blockLowerExtend=true;
			} else {
				blockLowerRetract=true;
			}
			SmartDashboard.putBoolean("blockLowerExtend",blockLowerExtend);
			SmartDashboard.putBoolean("blockLowerRetract",blockLowerRetract);*/
			return speed;
		}
		
		if (speed > 0 && blockLowerExtend) {
			return 0;
		}

		if (speed < 0 && blockLowerRetract) {
			return 0;
		}
		
		return speed;
    }

    private void setLowerMotorSpeed(double inspeed) {
		double speed = checkLowerCurrent(inspeed);	

		getLowerArmMotor(1).set(ControlMode.PercentOutput,speed);
		getLowerArmMotor(2).set(ControlMode.PercentOutput,speed);  	
		System.out.println("setLowerMotorSpeed:" + speed);
    }

    public void moveLowerArmNoLimit(double inspeed) { 	
		bypass_limits = true;
		int pos = getLowerArmMotor(1).getSelectedSensorPosition(0);
		double speed = limit_speed(inspeed, pos, lowerArmMax, false);
		setLowerMotorSpeed(speed);
    }

    public void moveLowerArm(double inspeed) {
    	bypass_limits = false;
    	moveLowerArm(inspeed,lowerArmMax);
    }

    public void moveLowerArm(double inspeed, int target_pos) { 	
		bypass_limits = false;
		int pos = getLowerArmMotor(1).getSelectedSensorPosition(0);

		double speed = limit_speed(inspeed, pos, target_pos, false);
	    //System.out.println("moveLowerArm inspeed: " + inspeed + " speed: " + speed);
		setLowerMotorSpeed(speed);
    }

    private double checkUpperCurrent(double speed) {
		double current = Robot.arm.getUpperArmMotor(1).getOutputCurrent();
		//double current2 = Robot.arm.getUpperArmMotor(2).getOutputCurrent();
		double current2=0;
		
		//SmartDashboard.putBoolean("blockUpperExtend",blockUpperExtend);
		//SmartDashboard.putBoolean("blockUpperRetract",blockUpperRetract);
		
		if ( current > current_max ) {
			current_max = current;
			if (current_max > 50 ) { System.out.println("current_max: " + current_max); }		
			//SmartDashboard.putNumber("current_max",current_max);
		}

		if (current > current_limit || current2 > current_limit) { 
			if (speed > 0) { 
				blockUpperExtend=true;
				block_count = 200;
			} else {
				blockUpperRetract=true;
				block_count = 200;
			}
			//SmartDashboard.putBoolean("blockUpperExtend",blockUpperExtend);
			//SmartDashboard.putBoolean("blockUpperRetract",blockUpperRetract);
			return 0;
		}
		
		
		if (speed > 0 && blockUpperExtend) {
			return 0;
		}

		if (speed < 0 && blockUpperRetract) {
			return 0;
		}
		
		return speed;
    }

    private void setUpperMotorSpeed(double inspeed) {
		double speed = checkUpperCurrent(inspeed);

		getUpperArmMotor(1).set(ControlMode.PercentOutput,speed * RobotMap.upperarm1inversion);
		getUpperArmMotor(2).set(ControlMode.PercentOutput,speed * RobotMap.upperarm2inversion);  	
		SmartDashboard.putNumber("Upper Speed",speed);
    }

    public void moveUpperArmNoLimit(double inspeed) {
		bypass_limits = true;
		int pos = getUpperArmMotor(1).getSelectedSensorPosition(0);
		double speed = limit_speed(inspeed, pos, upperArmMax, true);
		if(RobotMap.upperInverted) {
			setUpperMotorSpeed(speed * -1);
		}
		else {
			setUpperMotorSpeed(speed);
		}
    }

    public void moveUpperArm(double inspeed) {
    	moveUpperArm(inspeed,upperArmMax);
    }

    public void moveUpperArm(double inspeed, int target_pos) { 	
		bypass_limits = false;
		int pos = getUpperArmMotor(1).getSelectedSensorPosition(0);
		double speed = limit_speed(inspeed, pos, target_pos, true);
		if(RobotMap.upperInverted) {
			setUpperMotorSpeed(speed * -1);
		}
		else {
			setUpperMotorSpeed(speed);
		}
    }

    private double limit_speed (double inspeed, int pos, int target_pos, boolean is_upper) {
    	double speed = inspeed;

    	boolean upperBackLimit, upperForwardLimit,
    	        lowerBackLimit, lowerForwardLimit;
    	
    	int first_drop = 9000;
    	int second_drop = 4500;

    	int first_drop_lower = RobotMap.RMLowerArmRestingPos + 15000;
    	int second_drop_lower = RobotMap.RMLowerArmRestingPos + 7500;
    	
    	int first_div = 2;
    	int second_div = 3;
    	
    	upperBackLimit = upperArmBackwardsLimitSwitch.get();
    	upperForwardLimit = upperArmForwardsLimitSwitch.get();
    	lowerBackLimit = lowerArmBackwardsLimitSwitch.get();
    	lowerForwardLimit = lowerArmForwardsLimitSwitch.get();

		//SmartDashboard.putBoolean("Upper Arm Backwards Limit",upperBackLimit);
		//SmartDashboard.putBoolean("Upper Arm Forwards Limit",upperForwardLimit);
		//SmartDashboard.putBoolean("Lower Arm Backwards Limit",lowerBackLimit);
		//SmartDashboard.putBoolean("lowerArmForwardsLimitSwitch",lowerForwardLimit);
    	
    	if ( is_upper ) {
    		if (inspeed < 0 && upperBackLimit == false) {
    			getUpperArmMotor(1).setSelectedSensorPosition(-1000,0,100);
    			upper_zeroed = true;
    			if (lower_zeroed) { arms_zeroed = true; }
    			return 0;
    		} else {
    			if (inspeed > 0 && upperForwardLimit == false) { 
    				getUpperArmMotor(1).setSelectedSensorPosition(RobotMap.RMupperArmMax,0,100);
    				return 0; 
    			}
    		}
    		if ( inspeed > 0 && !upper_zeroed) {
    			return 0; 					
    		}

            if (inspeed < 0 && pos < first_drop && bypass_limits == false ) { 
    			speed = speed / first_div;
    		}

    		if ( inspeed < -.6) {
    			if (inspeed < 0 && pos < second_drop && bypass_limits == false  ) { 
    				speed = speed / second_div;
    			}
    		}	

    		if (inspeed < 0 && pos < 100 && bypass_limits == false ) { 
    			speed = 0;
    		}

    		if (inspeed > 0 && pos > target_pos - first_drop ) { 
    			speed = speed / first_div;
    		}

    		if ( inspeed > .6) {
    			if (inspeed > 0 && pos > target_pos - second_drop) { 
    				speed = speed / second_div;
    			}
    		}	

    	} else {
    		if (inspeed < 0 && lowerBackLimit == false) {
    			getLowerArmMotor(1).setSelectedSensorPosition(-500,0,100);
    			lower_zeroed = true;
    			if (upper_zeroed) { arms_zeroed = true; }
    			return 0;
    		} else {
    			//if (lowerForwardLimit == false) { 
    			//	return 0; 
    			//}
    		}		
    		if ( inspeed > 0 && !lower_zeroed) {
    			return 0; 					
    		}

            if (inspeed < 0 && pos < (first_drop_lower*3) && bypass_limits == false ) { 
    			speed = speed / first_div;
    			if (speed > -0.3 && speed != 0 ) { speed = -0.3; }
    		}

    		if ( inspeed < -.8) {
    			if (inspeed < 0 && pos < (second_drop_lower*3) && bypass_limits == false  ) { 
    				speed = speed / second_div;
        			if (speed > -0.3 && speed != 0 ) { speed = -0.3; }
    			}
    		}	

    		if (inspeed < 0 && pos < RobotMap.RMlowerArmGrabBottom && bypass_limits == false ) { 
    			speed = 0;
    		}
    		
    		if (inspeed > 0 && pos > target_pos - first_drop_lower ) { 
    			speed = speed / first_div;
    			if (speed < .3 && speed != 0) { speed = .35; }
    		}

    		if ( inspeed > .8) {
    			if (inspeed > 0 && pos > target_pos - second_drop_lower) { 
    				speed = speed / second_div;
        			if (speed < .3 && speed != 0) { speed = .3; }
    			}
    		}	
    		
    		//System.out.println("Limit Speed Lower " + speed);
    	}

		if (inspeed > 0 && pos > target_pos ) { 
			speed = 0;
		}
		
		return speed;
    }
    
    public TalonSRX getLowerArmMotor(int index) {
    	if (index == 1) {
    		return lowerArmMotor1;
    	}
  		return lowerArmMotor2;
    }

    public TalonSRX getUpperArmMotor(int index) {
    	if (index == 1) {
    		return upperArmMotor1;
    	}
  		return upperArmMotor2;
    }
    
    public boolean isArmActive() {
    	return arm_active;
    }
    
    public void setArmActive(boolean active) {
    	arm_active = active;
    } 	

    public boolean isArmNeedsMove() {
    	return arm_needs_move;
    }
    
    public void setArmNeedsMove(boolean needs_move) {
    	arm_needs_move = needs_move;
    } 	

    public arm_position getTargetArmPos() {
    	return targetArmPosition;
    }
    
    public void setTargetArmPos(arm_position pos) {
    	targetArmPosition = pos;
    }

    public arm_position getStartingArmPos() {
    	return startingArmPosition;
    }
    
    public void setStartingArmPos(arm_position pos) {
    	startingArmPosition = pos;
    }
    
    public arm_states getState() {
    	return state;
    }

    public void doResetUpperArm() {
		int upperPos = Robot.arm.getUpperArmMotor(1).getSelectedSensorPosition(0);
		
		double speed = Arm.armSpeed;
		
		if (start_count > 1) {
			speed = Arm.armSpeed/start_count;
			start_count--;
		}

		switch (Robot.arm.getTargetArmPos()) {
    	case arm_scale_place:
    	case arm_switch_place:
    	case arm_scale_place_nodrive:
    	case arm_switch_place_nodrive:
    	case arm_grab_cube:	
    		if ( upperPos < upperArmRestingPos ) {
				Robot.arm.moveUpperArm(speed, upperArmRestingPos);	
    		} else {
    			if (upperPos > upperArmRestingPos + 500) {
    				Robot.arm.moveUpperArm(speed * -1, upperArmRestingPos);
    			} else {
					Robot.arm.moveUpperArm(0);
	  			    state = Arm.arm_states.parkLowerArm;
	  			    System.out.println("doResetUpperArm: next state parkLowerArm");
    			}    
    		}
    		break;
    	case arm_climb:
    		if ( upperPos < upperArmClimbTop ) {
				Robot.arm.moveUpperArm(speed, upperArmClimbTop);	
    		} else {
				Robot.arm.moveUpperArm(0);
  			    state = Arm.arm_states.doneState;
    		}		
    		break;

    	default:
			Robot.arm.moveUpperArm(0);
    		// do nothing
    		break;
    	}	    
    }
    public void doExtendUpperArm() {
		int upperPos = Robot.arm.getUpperArmMotor(1).getSelectedSensorPosition(0);
		
		double speed = Arm.armSpeed;
		
		if (start_count > 1) {
			speed = Arm.armSpeed/start_count;
			start_count--;
		}

		switch (Robot.arm.getTargetArmPos()) {
		case arm_high_scale:
    		if ( upperPos < RobotMap.RMupperArmScaleTop ) {
				Robot.arm.moveUpperArm(speed, RobotMap.RMupperArmScaleTop);	
    		} else {
				Robot.arm.moveUpperArm(0);
  			    state = Arm.arm_states.doneState;
    		}		
    		break;
    	case arm_scale_place:
    	case arm_switch_place:
    	case arm_scale_place_nodrive:
    	case arm_switch_place_nodrive:
    	case arm_grab_cube:
    	case arm_climb:
    	default:
			Robot.arm.moveUpperArm(0);
    		// do nothing
    		break;
    	}	    
    }

    public void doLowerLowerArm() {
		int lowerPos = Robot.arm.getLowerArmMotor(1).getSelectedSensorPosition(0);
		int stop_pos=-1;

		switch (Robot.arm.getTargetArmPos()) {
    	case arm_scale_place:
    	case arm_switch_place:
    	case arm_scale_place_nodrive:
    	case arm_switch_place_nodrive:
    		stop_pos = 5000;
    	default:
    		// do nothing
    		break;
    	}

		if (stop_pos != -1) {   
			if ( lowerPos > stop_pos ) {
				Robot.arm.moveLowerArm(Arm.armSpeed * -1, stop_pos);	
			} else {
				Robot.arm.moveLowerArm(0);
				Robot.solonoids.grabber_close();
				state = Arm.arm_states.resetUpperArm;
				start_count=8;
			}
		}	
    }

    public void doParkLowerArm() {
		int lowerPos = Robot.arm.getLowerArmMotor(1).getSelectedSensorPosition(0);
		int stop_pos=-1;

		switch (Robot.arm.getTargetArmPos()) {
    	case arm_scale_place:
    	case arm_switch_place:
    	case arm_scale_place_nodrive:
    	case arm_switch_place_nodrive:
    	case arm_secure_cube:
    	case arm_grab_cube:
    		stop_pos = lowerArmGrabBottom;
    		break;
    	default:
    		// do nothing
    		break;
    	}

        System.out.println("parkLowerArm stop_pos = " + stop_pos);
		if (stop_pos != -1) {   
			if ( lowerPos > stop_pos ) {
				Robot.arm.moveLowerArm(Arm.armSpeed * -1, stop_pos);	
                //System.out.println("parkLowerArm move lower arm " + lowerPos + " " + stop_pos);
			} else {
				Robot.arm.moveLowerArm(0);	
				//switch (Robot.arm.getTargetArmPos()) {
				//case arm_grab_cube:
				//	Robot.solonoids.grabber_open();
				//	break;
				//default:
				//	break;
				//}
                state = Arm.arm_states.doneState;
                System.out.println("parkLowerArm setting to done state");
			}
		} else {
            state = Arm.arm_states.doneState;
            System.out.println("parkLowerArm setting to done state stop_pos -1 case");		
		}
    }

    public void doOpenGrabber() {
    	if (pause_before_open) {
    		pause_before_open = false;
    		wait_loops = 25;
    	}
    	
    	wait_loops--;
    	if (wait_loops > 0) {
    		return;
    	}

		Robot.solonoids.grabber_open();

    	switch (Robot.arm.getTargetArmPos()) {
    	case arm_scale_place:
    	case arm_switch_place:
    	case arm_scale_place_nodrive:
    	case arm_switch_place_nodrive:
			state = Arm.arm_states.driveBackwardsAgain;
    	    break;
    	case arm_grab_cube:
    		pause_before_open=true;
			state = Arm.arm_states.closeGrabber;
    	    break;	
    	case arm_exchange:
    		state = Arm.arm_states.pushCube;
    		break;
    	default:
    		// do nothing
    		break;
    	}	    
    }

    public void doCloseGrabber() {
    	if (pause_before_open) {
    		pause_before_open = false;
    		wait_loops = 50;
    	}
    	
    	wait_loops--;
    	if (wait_loops > 0) {
    		return;
    	}

    	switch (Robot.arm.getTargetArmPos()) {
    	case arm_scale_place:
    	case arm_switch_place:
    	case arm_scale_place_nodrive:
    	case arm_switch_place_nodrive:
    		Robot.solonoids.grabber_close();
			state = Arm.arm_states.lowerLowerArm;
    	    break;
    	case arm_grab_cube:
    		Robot.solonoids.grabber_close();
			state = Arm.arm_states.raiseLowerArm;
    	    break;	
    	case arm_secure_cube:
    		Robot.solonoids.grabber_close();
			state = Arm.arm_states.retractUpperFirst;
			start_count=8;
			
    	    break;
    	default:
    		// do nothing
    		break;
    	}	    
    }

    public void doPushCube() {
    	switch (Robot.arm.getTargetArmPos()) {
    	case arm_exchange:
    		if (!pause_active) {
     		   wait_loops=100;
    		   pause_active = true;
			   Robot.solonoids.grabber_open();
    		} else {
    		   wait_loops--;
    		   if (wait_loops >= 80 && wait_loops > 0) {
        		   Robot.solonoids.pusher_out();
    		   }
    		   if (wait_loops == 0) {
    			   pause_active=false;
    			   Robot.solonoids.pusher_in();
    			   state = Arm.arm_states.doneState;
    		   }
    	   }
      	   break;
      	default:
      		//do nothing
      		break;
    	}
    }	

    public void doDriveForward() {
    	double speed=drive_speed;
    	int loops=drive_loops;
    	
    	switch (Robot.arm.getTargetArmPos()) {
    	case arm_scale_place_nodrive:
    	case arm_switch_place_nodrive:
            pause_before_open = true;
            state = Arm.arm_states.openGrabber;
			return;
    	case arm_scale_place:
    		speed=scale_drive_speed;
    		loops=scale_drive_loops;    		
    		break;
    	case arm_switch_place:
    		speed=drive_speed;
    		loops=drive_loops; 
    		break;
    	default:
    		//do nothing
    		break;
    	}		

   		if ( driving_forward== false ) {
    		wait_loops = loops;
    		driving_forward = true;
    		Robot.gyro.resetGyro();
        }
    	
    	if(Robot.gyro.getAngle() > 2) {
    		Robot.driveBase.Drive(speed, 0, 0.1,1);
    	}
    	else if(Robot.gyro.getAngle() < -2) {
    		Robot.driveBase.Drive(speed, 0, -0.1,1);
    	} else {
            Robot.driveBase.Drive(speed, 0, 0,1);
    	}
    	
    	wait_loops--;
    	if (wait_loops == 0) {
    		driving_forward = false;
            Robot.driveBase.Drive(0, 0, 0,1);
            if (Robot.arm.getTargetArmPos() != Arm.arm_position.arm_scale_place) {
                pause_before_open = true;
            }    
			state = Arm.arm_states.openGrabber;
    	}	    
    }

    public void doRaiseLowerArm() {
		int lowerPos = Robot.arm.getLowerArmMotor(1).getSelectedSensorPosition(0);
		int upperPos = Robot.arm.getUpperArmMotor(1).getSelectedSensorPosition(0);
		int stop_pos=-1;

		switch (Robot.arm.getTargetArmPos()) {
		case arm_scale:
		case arm_high_scale:
    	case arm_scale_place:
    	case arm_scale_place_nodrive:
    		stop_pos = lowerArmScaleTop;
    		//System.out.println("doRaiseLowerArm stop_pos" + stop_pos);
    	    break;
    	case arm_switch:    
    	case arm_switch_place:
    	case arm_switch_place_nodrive:
    		stop_pos = RobotMap.RMlowerArmSwitchTop;
    		//System.out.println("doRaiseLowerArm stop_pos " + stop_pos);
    	    break;
    	case arm_climb:
    		stop_pos = lowerArmClimbTop;
    	    break;
    	case arm_grab_cube:
    		stop_pos = lowerArmGrabTop;
    		break;
    	default:
    		// do nothing
    		break;
    	}

		if (stop_pos != -1) {   
			//System.out.println("RaiseLowerArm: stop_pos: "+stop_pos+", lowerPos"+lowerPos);
			if ( lowerPos < stop_pos ) {
				Robot.arm.moveLowerArm(Arm.armSpeed, stop_pos);
		    	switch (Robot.arm.getTargetArmPos()) {
		    	case arm_grab_cube: 
					if (upperPos < 50000) {
						Robot.arm.moveUpperArm(Arm.armSpeed);
					} else {
						Robot.arm.moveUpperArm(0);
					}
		    		break;
		    	default:
					Robot.arm.moveUpperArm(0);
					break;
				}
			} else if ( lowerPos > stop_pos + 2500) {	
				Robot.arm.moveLowerArm(Arm.armSpeed * -1, stop_pos);
				if (lowerPos < 20000) {
					if (upperPos < 50000) {
						Robot.arm.moveUpperArm(Arm.armSpeed);
					} else {
						Robot.arm.moveUpperArm(0);
					}
				}
				if (lowerPos > lowerArmScaleTop - 10000) {
					if (upperPos > RobotMap.RMupperArmRetractPos) {
						Robot.arm.moveLowerArm(Arm.armSpeed,RobotMap.RMupperArmRetractPos);
					} else {
						Robot.arm.moveUpperArm(0);					
					}
				}
			} else {
				Robot.arm.moveLowerArm(0);
				Robot.arm.moveUpperArm(0);
				switch (Robot.arm.getTargetArmPos()) {
				case arm_switch:
				case arm_scale:
					state = Arm.arm_states.doneState;
					break;
				case arm_high_scale:
					state = Arm.arm_states.extendUpper;
					break;
				case arm_grab_cube:
					state = Arm.arm_states.resetUpperArm;
					start_count=8;
					break;
				case arm_climb:
					state = Arm.arm_states.resetUpperArm;
					start_count=8;
					break;
				default:	
					state = Arm.arm_states.driveForward;
					break;
				}
			}
		}	
    }
    
   public void doDriveBackwardsAgain() {
    	switch (Robot.arm.getTargetArmPos()) {
    	case arm_scale_place_nodrive:
    	case arm_switch_place_nodrive:
    		if (!pause_active) { 
    			wait_loops=50;
    			pause_active=true;
    		}
    		if ( wait_loops-- > 0) {
    			return;
    		}
    		
    		pause_active=false;
			state = Arm.arm_states.lowerLowerArm;
			return;
		default:
		    // do nothing
			break;
    	}		

    	if ( driving_back == false ) {
    		wait_loops = drive_loops;
    		driving_back = true;
    		Robot.gyro.resetGyro();
    	}
    	
    	if (wait_loops > 0) {
    		if(Robot.gyro.getAngle() > 2) {
    			Robot.driveBase.Drive(drive_speed * -1, 0, 0.1,1);
    		}
    		else if(Robot.gyro.getAngle() < -2) {
    			Robot.driveBase.Drive(drive_speed * -1, 0, -0.1,1);
    		}
    		else {
    			Robot.driveBase.Drive(drive_speed * -1, 0, 0,1);
    		}
    	}
    	wait_loops--;
    	if (wait_loops == 0) {
    		driving_back = false;
            Robot.driveBase.Drive(0, 0, 0,1);
        	setArmNeedsMove(false);
			//state = Arm.arm_states.lowerLowerArm;
			state = Arm.arm_states.doneState;
    	}
    }
    
    public void doDriveBackwards() {
		state = Arm.arm_states.raiseLowerArm;
		return;

		/* Removed from the state machine for now, assumes we are
		 * starting back from the scale or switch
		 
		if ( driving_back == false ) {
    		wait_loops = drive_loops;
    		driving_back = true;
    		Robot.gyro.resetGyro();
    	}
    	
    	if(Robot.gyro.getAngle() > 2) {
    		Robot.driveBase.Drive(drive_speed * -1, 0, 0.1,1);
    	}
    	else if(Robot.gyro.getAngle() < -2) {
    		Robot.driveBase.Drive(drive_speed * -1, 0, -0.1,1);
    	}
    	else {
        Robot.driveBase.Drive(drive_speed * -1, 0, 0,1);
    	}
    	
    	wait_loops--;
    	if (wait_loops == 0) {
    		driving_back = false;
            Robot.driveBase.Drive(0, 0, 0,1);
			state = Arm.arm_states.raiseLowerArm;
    	}
    	*/
    }

    public void doRetractUpperFirst() {
		int upperPos = Robot.arm.getUpperArmMotor(1).getSelectedSensorPosition(0);
		int lowerPos = Robot.arm.getLowerArmMotor(1).getSelectedSensorPosition(0);

		double speed = Arm.armSpeed;
		
		if (start_count > 0) {
			speed = Arm.armSpeed/start_count;
			start_count--;
		}

		switch (Robot.arm.getTargetArmPos()) {
    	case arm_scale:
    	case arm_switch:
    	case arm_scale_place:
    	case arm_switch_place:
    	case arm_scale_place_nodrive:
    	case arm_switch_place_nodrive:
    	case arm_climb:
    	case arm_high_scale:

    		if ( upperPos > upperArmRetractPos ) {
				Robot.arm.moveUpperArm(speed * - 1, upperArmRetractPos);
				if (upperPos < upperArmRetractPos+15000) {
					Robot.arm.moveLowerArm(speed / 2);
				} else if (lowerPos < 14000 && upperPos < 10000) {
					Robot.arm.moveLowerArm(speed / 2, 10000);
				} else if (lowerPos < 10000) {
					Robot.arm.moveLowerArm(speed / 2, 10000);
				} else {
					Robot.arm.moveLowerArm(0);
				}
    		} else {
				Robot.arm.moveUpperArm(0);
				Robot.arm.moveLowerArm(0);
				switch (Robot.arm.getTargetArmPos()) {
		    	case arm_scale:
				case arm_high_scale:
					state = Arm.arm_states.raiseLowerArm;
					break;
		    	case arm_switch:
		    	case arm_scale_place_nodrive:
		    	case arm_switch_place_nodrive:
    				state = Arm.arm_states.raiseLowerArm;
		    		break;
		    	case arm_secure_cube:
    				state = Arm.arm_states.parkLowerArm;
    				break;
    			default:	
    				state = Arm.arm_states.driveBackwards;
    				break;
    			}
    		}
    		break;

    	case arm_secure_cube:
    		if ( upperPos > upperArmRetractPos ) {
				Robot.arm.moveUpperArm(speed * - 1, upperArmRetractPos);
    		} else {
				Robot.arm.moveUpperArm(0);
				Robot.arm.moveLowerArm(0);
				state = Arm.arm_states.parkLowerArm;
    		}
    		break;

    	case arm_grab_cube:	
    		if ( upperPos > upperArmRetractPos && lowerPos > 20000) {
				Robot.arm.moveUpperArm(speed * - 1, upperArmRetractPos);
    		} else {
				Robot.arm.moveUpperArm(0);
				Robot.arm.moveLowerArm(0);
   				state = Arm.arm_states.raiseLowerArm;
    		}
    		break;

    	default:
    		// do nothing
    		break;
		}	
    }

    public void doRetractUpperSecond() {
		int upperPos = Robot.arm.getUpperArmMotor(1).getSelectedSensorPosition(0);

		switch (Robot.arm.getTargetArmPos()) {
    	case arm_scale_place:
    	case arm_switch_place:
    	case arm_scale_place_nodrive:
    	case arm_switch_place_nodrive:
    	case arm_climb:
    		if ( upperPos > upperArmRetractPos ) {
				Robot.arm.moveUpperArm(Arm.armSpeed * - 1, upperArmRetractPos);	
    		} else {
				Robot.arm.moveUpperArm(0);	
    			state = Arm.arm_states.lowerLowerArm;
    		}
    		break;
    	default:
    		// do nothing
    		break;
		}	
    }

    public void doArmSpitCube() {
		if (!pause_active) { 
			wait_loops=100;
			pause_active=true;
		}
    		
    	wait_loops--;  	
    	if (wait_loops == 70) { 
    		 Robot.solonoids.grabber_open();
    	}
    	Robot.intake.setIntake(0.5);
    	if (wait_loops == 0) {
        	Robot.intake.setIntake(0);
			state = Arm.arm_states.doneState;
			pause_active=false;
    	}
    }

	public void doBegin() {
    	switch (Robot.arm.getTargetArmPos()) {
    	case arm_scale:
    	case arm_high_scale:
    	case arm_switch:
    	case arm_scale_place:
    	case arm_switch_place:
    	case arm_scale_place_nodrive:
    	case arm_switch_place_nodrive:
    		state = Arm.arm_states.retractUpperFirst;
    		start_count=8;
    	    break;
    	case arm_climb:
    		state = Arm.arm_states.raiseLowerArm;
    		break;
    	case arm_exchange:
    		state = Arm.arm_states.pushCube;
    	    break;
    	case arm_grab_cube:
    		state = Arm.arm_states.retractUpperFirst;
    		start_count=8;
    		//state = Arm.arm_states.raiseLowerArm;
    	    break;
    	case arm_secure_cube:
    		state = Arm.arm_states.closeGrabber;
    	    break;
    	case arm_spit_cube:
    		pause_active=false;
    		state = Arm.arm_states.armSpitCube;
    	    break;
    	case arm_drop_cube:
    		Robot.solonoids.grabber_open(); 
    		state = Arm.arm_states.doneState;
    	    break;
    	default:
    		// do nothing
    		break;
    	}    
    }
	
	public void abortArmMove() {
    	Robot.arm.moveLowerArmNoLimit(0);
  		Robot.arm.moveUpperArmNoLimit(0);
		Robot.arm.setArmActive(false);
		Robot.arm.setArmNeedsMove(false);	
	}
    

    public void doMove() {
    	int lowerPos=0, upperPos=0;
		double lowerVoltage=0, upperVoltage=0, upperCurrent, upperCurrent2, lowerCurrent, lowerCurrent2;
		
		lowerPos = Robot.arm.getLowerArmMotor(1).getSelectedSensorPosition(0);
		lowerVoltage = Robot.arm.getLowerArmMotor(1).getBusVoltage();
	
		upperPos = Robot.arm.getUpperArmMotor(1).getSelectedSensorPosition(0);
		upperVoltage = Robot.arm.getUpperArmMotor(1).getBusVoltage();

		lowerCurrent = Robot.arm.getLowerArmMotor(1).getOutputCurrent();
		lowerCurrent2 = Robot.arm.getLowerArmMotor(2).getOutputCurrent();
		upperCurrent = Robot.arm.getUpperArmMotor(1).getOutputCurrent();
		//upperCurrent2 = Robot.arm.getUpperArmMotor(2).getOutputCurrent();

    	boolean upperBackLimit = upperArmBackwardsLimitSwitch.get();
    	boolean upperForwardLimit = upperArmForwardsLimitSwitch.get();
    	boolean lowerBackLimit = lowerArmBackwardsLimitSwitch.get();
    	boolean lowerForwardLimit = lowerArmForwardsLimitSwitch.get();

		SmartDashboard.putBoolean("UB Limit",upperBackLimit);
		SmartDashboard.putBoolean("UF Limit",upperForwardLimit);
		SmartDashboard.putBoolean("LF Limit",lowerBackLimit);
		//SmartDashboard.putBoolean("lowerArmForwardsLimitSwitch",lowerForwardLimit);

		SmartDashboard.putNumber("Lower Position",lowerPos);
		SmartDashboard.putNumber("Upper Position",upperPos);

		//SmartDashboard.putNumber("Lower Voltage",lowerVoltage);
		//SmartDashboard.putNumber("Upper Voltage",upperVoltage);

		//SmartDashboard.putNumber("Upper Current",upperCurrent);
		//SmartDashboard.putNumber("Lower Current",lowerCurrent);
		//SmartDashboard.putNumber("Upper Current 2",upperCurrent2);
		//SmartDashboard.putNumber("Lower Current 2",lowerCurrent2);
	
		//SmartDashboard.putBoolean("isArmActive",Robot.arm.isArmActive());
		//SmartDashboard.putBoolean("isArmNeedsMove",Robot.arm.isArmNeedsMove());
		//SmartDashboard.putString("TargetArmPos",Robot.arm.getTargetArmPos().toString());
		//SmartDashboard.putString("armState",state.toString());

		if ( block_count > 0 ) {
			if (block_count == 1) {
				blockUpperRetract=false;
				blockUpperExtend=false;
				current_max=0;
			}
			block_count--;
		}

		if (Robot.arm.isArmActive()) {
			switch (Robot.arm.getState()) {
			case begin:
				doBegin();
				break;
			case retractUpperFirst:
				doRetractUpperFirst();
				break;
			case raiseLowerArm:
				doRaiseLowerArm();
				break;
			case extendUpper:
				doExtendUpperArm();
				break;
			case driveForward:
				doDriveForward();
				break;
			case openGrabber:
				doOpenGrabber();
				break;
			case closeGrabber:
				doCloseGrabber();
				break;
			case driveBackwards:
				doDriveBackwards();
				break;
			case driveBackwardsAgain:
				doDriveBackwardsAgain();
				break;
			case pushCube:
				doPushCube();
				break;
			case retactUpperSecond:
				doRetractUpperSecond();
				break;
			case lowerLowerArm:
				doLowerLowerArm();
				break;
			case resetUpperArm:
				doResetUpperArm();
				break;
			case parkLowerArm:
				doParkLowerArm();
				break;
			case armSpitCube:
				doArmSpitCube();
				break;
			case doneState:
				Robot.arm.setArmActive(false);
				Robot.arm.setArmNeedsMove(false);
				state = Arm.arm_states.begin;
		    	switch (Robot.arm.getTargetArmPos()) {
		    		case arm_scale_place:
		    		case arm_switch_place:
		    		case arm_scale_place_nodrive:
		    		case arm_switch_place_nodrive:
		    			Robot.arm.setTargetArmPos(Arm.arm_position.arm_grab_cube);
		    		    break;
		    		default:
		    			//Do Nothing
		    			break;
		    	}	
				break;
			}	
      } 
    }
}
