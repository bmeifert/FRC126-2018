package org.usfirst.frc.team126.robot.commands;

import org.usfirst.frc.team126.robot.OI;
import org.usfirst.frc.team126.robot.Robot;
import org.usfirst.frc.team126.robot.RobotMap;
import org.usfirst.frc.team126.robot.subsystems.Arm;
import org.usfirst.frc.team126.robot.subsystems.DSdata;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import edu.wpi.first.wpilibj.command.Scheduler;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
@SuppressWarnings("unused")
public class ArmControl extends Command {
	static int starting=0;
	static boolean running=false;
	public static boolean zeroArmsRun=false;
	

	static int startingu=0;
	static boolean runningu=false;
	boolean bxFirstPress = true;
	boolean byFirstPress = true;
	boolean bbFirstPress = true;
	boolean backFirstPress = true;

    public ArmControl() {
        requires(Robot.arm);
        //requires(Robot.driveBase);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @SuppressWarnings("static-access")
	protected void execute() {
    	
		if (DSdata.isAutonomous()) {
			return;
		}
    	
    	boolean ba,bb,bx,by,lt,rt,bdy,climb, grabber, ls, rs;
    	boolean resetu=false, resetl=false;
    	int povArm, povDrv;
    	boolean povArmUp,povArmDown,povArmLeft,povArmRight;
    	boolean povDrvUp,povDrvDown,povDrvLeft,povDrvRight;
        double leftStickY, rightStickY;
        
        Robot.arm.upperArmRetractPos = RobotMap.RMupperArmRetractPos;
        Robot.arm.upperArmRestingPos = RobotMap.RMupperArmRestingPos;
        
        Robot.arm.lowerArmScaleTop = RobotMap.RMlowerArmScaleTop;
        Robot.arm.lowerArmSwitchTop = RobotMap.RMlowerArmSwitchTop;

        Robot.arm.lowerArmClimbTop = RobotMap.RMlowerArmClimbTop;
        Robot.arm.upperArmClimbTop = RobotMap.RMupperArmClimbTop;

        Robot.arm.lowerArmGrabTop = RobotMap.RMlowerArmGrabTop;
        Robot.arm.lowerArmGrabBottom = RobotMap.RMlowerArmGrabBottom;

        Robot.arm.lowerArmMax = RobotMap.RMlowerArmMax;
        Robot.arm.upperArmMax = RobotMap.RMupperArmMax;

		//if (Robot.autonomous != null) {
		//	return;
		//}

		if ( !Robot.arm.getArmsZeroed() && zeroArmsRun==false) {
  		    //Scheduler.getInstance().add(new ZeroArms());
		    //zeroArmsRun=true;
		}

    	ba = Robot.oi.armController.getRawButton(RobotMap.xboxA);
    	bb = Robot.oi.armController.getRawButton(RobotMap.xboxB);
    	bx = Robot.oi.armController.getRawButton(RobotMap.xboxX);
    	by = Robot.oi.armController.getRawButton(RobotMap.xboxY);
    	lt = Robot.oi.armController.getRawButton(RobotMap.xboxLT);
    	rt = Robot.oi.armController.getRawButton(RobotMap.xboxRT);
    	ls = Robot.oi.armController.getRawButton(RobotMap.xboxLS);
    	rs = Robot.oi.armController.getRawButton(RobotMap.xboxRS);
    	double tl, tr;
		tl = Robot.oi.armController.getRawAxis(RobotMap.Ltrigger);
		tr = Robot.oi.armController.getRawAxis(RobotMap.Rtrigger);

    	resetl = Robot.oi.armController.getRawButton(RobotMap.xboxBack);

		leftStickY = Robot.oi.armController.getRawAxis(RobotMap.stickY); // Get and assign controller values
		rightStickY = Robot.oi.armController.getRawAxis(RobotMap.rStickY); // Get and assign controller values
		
    	
    	povArm = Robot.oi.armController.getPOV();
    	
    	grabber = Robot.oi.armController.getRawButton(RobotMap.xboxStart);

    	povDrv = Robot.oi.driveController.getPOV();
    	//bdy = Robot.oi.driveController.getRawButton(RobotMap.xboxY);
    	bdy = false;
        climb = Robot.oi.driveController.getRawButton(RobotMap.xboxStart);
    	
        
 	    //if (resetl && resetu) {
 	    //    Robot.arm.zeroArms();
 	    //}    
 
		povArmUp = povArmDown = povArmLeft = povArmRight = false;
//    	if (povArm != -1) {
//         	if (povArm >= 315 || povArm < 45 ) povArmUp = true;
//         	if (povArm >= 45 && povArm < 135 ) povArmRight = true;
//         	if (povArm >= 135 && povArm < 225 ) povArmDown = true;
//         	if (povArm >= 225 && povArm < 315 ) povArmLeft = true;
//    	}
		if (leftStickY < -.4) { povArmUp = true; }
		if (leftStickY > .4) { povArmDown= true; }
		if (rightStickY > .4) { povArmLeft = true; }
		if (rightStickY < -.4) { povArmRight = true; }

		povDrvUp = povDrvDown = povDrvLeft = povDrvRight = false;
    	if (povDrv != -1) {
         	if (povDrv >= 315 || povDrv < 45 ) povDrvUp = true;
         	if (povDrv >= 45 && povDrv < 135 ) povDrvRight = true;
         	if (povDrv >= 135 && povDrv < 225 ) povDrvDown = true;
         	if (povDrv >= 225 && povDrv < 315 ) povDrvLeft = true;
    	}
    	
    	if (ba) {
    		Robot.arm.abortArmMove();
    	}

		if (by) {
			if(byFirstPress) {
				Robot.arm.abortArmMove();
			}
			Robot.arm.doScale();
			byFirstPress = false;
		}
		else {
			byFirstPress = true;
		}

		if (bx) {
			if(bxFirstPress) {
				Robot.arm.abortArmMove();
			}
			Robot.arm.doSwitch();
			bxFirstPress = false;
		}
		else {
			bxFirstPress = true;
		}
		if(bb) {
			if(bbFirstPress) {
				Robot.arm.abortArmMove();
			}
			Robot.arm.doCubeGrab();
			bbFirstPress = false;
		}
		else {
			bbFirstPress = true;
		}

		if (tl > .5) {
			Robot.solonoids.grabber_open();
			Robot.intake.setIntake(-1);
		}
		else {
			if(!rs) {
				Robot.solonoids.grabber_close();
			}
			else {
				Robot.solonoids.grabber_open();
			}
		}
		
		if (tr > .5) {
			Robot.intake.setIntake(1);
		}
		if(tl < 0.5 && tr < 0.5) {
			if(!ls) {
				Robot.intake.intakeOff();
				//System.out.println("intake.intakeoff()");
			}
			else {
				Robot.intake.setIntake(-1);
			}
		}

		if (climb) {
			Robot.arm.doClimb();
		}
		

		if(resetl) {
			if(backFirstPress) {
				Robot.arm.abortArmMove();
			}
			Robot.arm.doHighScale();
			backFirstPress = false;
		}
		else {
			backFirstPress = true;
		}

		if (Robot.arm.isArmActive() == false) {
            if (povArmUp || povDrvUp) { 
            	if ( !running ) {
            		starting=20;
            		running=true;
            	}
            	double armSpeed = Arm.armSpeed * Math.abs(leftStickY);
            	if (starting > 10 ) {
            		if (armSpeed >= .7) {
            			Robot.arm.moveLowerArm(armSpeed/3);
            		} else {	
            			Robot.arm.moveLowerArm(armSpeed/2);
            		}
            	} else {
            		if (starting > 1 ) {
                		Robot.arm.moveLowerArm(armSpeed/2);
            		} else {
                		Robot.arm.moveLowerArm(armSpeed);
            		}	
            	}
                starting--;
		    }
           	if (povArmDown || povDrvDown) {
            	if ( !running ) {
            		starting=20;
            		running=true;
            	}
            	double armSpeed = Arm.armSpeed * Math.abs(leftStickY);
            	if (starting > 1 ) {
         			Robot.arm.moveLowerArm(armSpeed/starting * -1);
            	} else {	
               		Robot.arm.moveLowerArm(armSpeed * -1);
            	}
                starting--;
            }    
           	if (lt) { Robot.arm.moveLowerArmNoLimit(-0.45); }
           	
           	if (!povArmUp && !povArmDown && !povDrvUp && !povDrvDown && !lt) { 
           		Robot.arm.moveLowerArm(0.0);
           		running=false;
           		starting=0;
           	}

            if (povArmLeft || povDrvLeft) { 
            	if ( !runningu ) {
            		startingu=25;
            		runningu=true;
            	}
            	double armSpeed = Arm.armSpeed * Math.abs(rightStickY);
            	if (startingu > 12 ) {
            		if ( armSpeed >= .7 ) {
                		Robot.arm.moveUpperArm(armSpeed/3*-1);
            		} else {	
                		Robot.arm.moveUpperArm(armSpeed/2*-1);
            		}	
            	} else {
            		if (startingu > 6 ) {
                		Robot.arm.moveUpperArm(armSpeed/2*-1);
            		} else {
                		Robot.arm.moveUpperArm(armSpeed*-1);
            		}	
            	}
                startingu--;
		    }
           	if (povArmRight || povDrvRight) {
            	if ( !runningu ) {
            		startingu=25;
            		runningu=true;
            	}
            	double armSpeed = Arm.armSpeed * Math.abs(rightStickY);
            	if (startingu > 12 ) {
            		if ( armSpeed >= .7 ) {
            			Robot.arm.moveUpperArm(armSpeed/3);
            		} else {
            			Robot.arm.moveUpperArm(armSpeed/2);
            		}
            	} else {
            		if (startingu > 6 ) { 
                		Robot.arm.moveUpperArm(armSpeed/2); 
            		} else { 
                		Robot.arm.moveUpperArm(armSpeed);
            		}	
            	}
                startingu--;
            }    
           	if (rt) { Robot.arm.moveUpperArmNoLimit(-0.5); }
           	if (!povArmLeft && !povArmRight && !povDrvRight && !povDrvLeft && !rt) { 
           		Robot.arm.moveUpperArm(0.0);
       			runningu=false;
       			startingu=0;
       		}
		}    

		Robot.arm.doMove();
   }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
