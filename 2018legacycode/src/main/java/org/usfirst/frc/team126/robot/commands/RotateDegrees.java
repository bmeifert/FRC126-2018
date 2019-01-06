package org.usfirst.frc.team126.robot.commands;

import org.usfirst.frc.team126.robot.Robot;
import org.usfirst.frc.team126.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotateDegrees extends Command {
	private double degrees;
	
    public RotateDegrees(double idegrees) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveBase);
    	degrees = idegrees;
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.gyro.resetGyro();
        Robot.driveBase.Drive(0, 0, 0, 1);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.gyro.getAngle() > 2 + degrees) {
    		Robot.driveBase.RotateInPlace(1, 0.35 * RobotMap.speedRatio);
    	}
    	else if(Robot.gyro.getAngle() < -2 + degrees) {
    		Robot.driveBase.RotateInPlace(-1, 0.35 * RobotMap.speedRatio);
    	}
    	else {
        Robot.driveBase.Drive(0, 0, 0,1);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (Robot.gyro.getAngle() < degrees + 2 && Robot.gyro.getAngle() > degrees - 2) {
    		return true;
    	} else {
    		return false;
    	}
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveBase.Drive(0, 0, 0, 1);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.driveBase.Drive(0, 0, 0, 1);
    }
}
