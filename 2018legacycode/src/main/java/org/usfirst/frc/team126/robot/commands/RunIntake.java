package org.usfirst.frc.team126.robot.commands;

import org.usfirst.frc.team126.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunIntake extends Command {
    public RunIntake() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.intake.setIntake(-1);
        Robot.solonoids.grabber_open();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.intake.setIntake(-1);
    	//System.out.println("RunIntake Execute");
    	Robot.solonoids.grabber_open();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
            return false;

    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.intake.setIntake(0);
    	Robot.solonoids.grabber_close();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.intake.setIntake(0);
    	Robot.solonoids.grabber_close();
    }
}
