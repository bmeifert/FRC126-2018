package org.usfirst.frc.team126.robot.commands;

import org.usfirst.frc.team126.robot.OI;
import org.usfirst.frc.team126.robot.Robot;
import org.usfirst.frc.team126.robot.RobotMap;
import org.usfirst.frc.team126.robot.subsystems.Arm;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
@SuppressWarnings("unused")
public class ArmMovement extends Command {
	boolean init=false;
	
    Arm.arm_position tpos;
    public ArmMovement(Arm.arm_position tpos_in) {
		requires(Robot.arm);
		//requires(Robot.driveBase);
        tpos = tpos_in;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @SuppressWarnings("static-access")
	protected void execute() {
    	if ( !init ) {
    		switch (tpos) {
    		case arm_scale_place:	
    			Robot.arm.doScalePlace();
    			System.out.println("ArmMovement Robot.arm.doScalePlace()");
    			break;
    		case arm_switch_place:	
    			Robot.arm.doSwitchPlace();
    			System.out.println("ArmMovement Robot.arm.doSwitchPlace()");
    			break;
    		case arm_switch:	
    			Robot.arm.doSwitch();
    			System.out.println("ArmMovement Robot.arm.doSwitch()");
    			break;
    		case arm_scale:	
    			Robot.arm.doScale();
    			System.out.println("ArmMovement Robot.arm.doScale()");
    			break;
    		case arm_drop_cube:	
    			Robot.arm.doDropCube();
    			System.out.println("ArmMovement Robot.arm.doDropCube()");
    			break;
    		case arm_spit_cube:	
    			Robot.arm.doSpitCube();  			
    			System.out.println("ArmMovement Robot.arm.doSpitCube()");
    			break;
    		case arm_grab_cube:	
    			Robot.arm.doCubeGrab();  			
    			System.out.println("ArmMovement Robot.arm.doCubeGrab()");
    			break;

    		default:
    			// Do Nothing
    			break;
    		}
    		init=true;
    	}  	

		System.out.println("ArmMovement execute()");

        if ( Robot.arm.isArmActive() ) {
        	Robot.arm.doMove();
        }           	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if ( !Robot.arm.isArmActive() ) {
        	Robot.arm.moveLowerArm(0);
        	Robot.arm.moveUpperArm(0);
    		System.out.println("ArmMovement isFinished() true");
        	return true;
        } 
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.arm.setArmActive(false);
    	Robot.arm.moveLowerArm(0);
    	Robot.arm.moveUpperArm(0);
    	Robot.intake.setIntake(0);
		System.out.println("ArmMovement end");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.arm.setArmActive(false);
    	Robot.arm.moveLowerArm(0);
    	Robot.arm.moveUpperArm(0);
    	Robot.intake.setIntake(0);
		System.out.println("ArmMovement interrupted");
    }
}
