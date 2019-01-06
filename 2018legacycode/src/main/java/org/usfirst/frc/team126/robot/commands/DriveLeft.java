package org.usfirst.frc.team126.robot.commands;

import org.usfirst.frc.team126.robot.Robot;
import org.usfirst.frc.team126.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveLeft extends Command {
    public DriveLeft() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveBase);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.gyro.resetGyro();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.gyro.getAngle() > 2) {
    		Robot.driveBase.Drive(0, 0.5 * RobotMap.speedRatio, 0.1,1);
    	}
    	else if(Robot.gyro.getAngle() < -2) {
    		Robot.driveBase.Drive(0, 0.5 * RobotMap.speedRatio, -0.1,1);
    	}
    	else {
        Robot.driveBase.Drive(0, 0.5 * RobotMap.speedRatio, 0,1);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveBase.Drive(0, 0, 0, 1);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.driveBase.Drive(0, 0, 0,1);
}
}
