package org.usfirst.frc.team126.robot.commands;

import org.usfirst.frc.team126.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team126.robot.subsystems.*;

/**
 *
 */
public class ThrowCube extends Command {
    public ThrowCube() {
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.intake.setIntake(1);
    }

    // Called repeatedly when this Command is scheduled to run
    /**
     * Aligns camera with the scale (pipeline 1)
     */
    protected void execute() {
    	Robot.intake.setIntake(1);
    }


    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.intake.intakeOff();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
