package org.usfirst.frc.team126.robot.commands;

import org.usfirst.frc.team126.robot.Robot;
import org.usfirst.frc.team126.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Pistons extends Command {
	boolean grabber_closed=false;
	int coolDown=0;
	int shivCoolDown=0;
	
    public Pistons() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.solonoids);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }
    
    public boolean getGrabberClosed() {
    	return grabber_closed;
    }

    // Called repeatedly when this Command is scheduled to run
    @SuppressWarnings("static-access")
	protected void execute() {
    	boolean xda, xdx, xax, xab, shiv;
    	
    	if (coolDown > 0) { coolDown--; }
    	if (shivCoolDown > 0) { shivCoolDown--; }

    	xda = Robot.oi.driveController.getRawButton(RobotMap.xboxA);
    	xdx = Robot.oi.driveController.getRawButton(RobotMap.xboxX);

    	xax = Robot.oi.armController.getRawButton(RobotMap.xboxStart);
    	xab = Robot.oi.armController.getRawButton(RobotMap.xboxB);

        shiv = Robot.oi.driveController.getRawButton(RobotMap.xboxBack);

    	if(xdx){
    		Robot.solonoids.pusher_out();
    	} else {
    		if ( !Robot.arm.isArmActive()) {
    		    Robot.solonoids.pusher_in();
    		}    
    	}
    	
    	/*
    	if((xax || xda || xab) && coolDown == 0){
    		Robot.solonoids.toggle_grabber();
    		coolDown=25;
    	}
    	*/

    	if((shiv) && shivCoolDown == 0){
    		Robot.solonoids.toggle_catcher();
    		shivCoolDown=25;
    	}
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
