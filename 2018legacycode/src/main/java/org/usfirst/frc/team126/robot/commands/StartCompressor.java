package org.usfirst.frc.team126.robot.commands;

import org.usfirst.frc.team126.robot.Robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class StartCompressor extends Command {
	public static Compressor c = new Compressor();
    public StartCompressor() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.pnuematics);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	c.setClosedLoopControl(true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
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
