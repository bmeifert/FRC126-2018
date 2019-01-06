package org.usfirst.frc.team126.robot.commands;

import org.usfirst.frc.team126.robot.Robot;
import org.usfirst.frc.team126.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AccelerateForward extends Command {
	double lastSpeed;
    public AccelerateForward() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveBase);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveBase.Drive(0, 0, 0, 1);
    	Robot.gyro.resetGyro();
    	lastSpeed = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	lastSpeed = (lastSpeed * 9 + 0.8) / 10;

    	if(Robot.gyro.getAngle() > 2) {
    		Robot.driveBase.Drive(lastSpeed * RobotMap.speedRatio, 0, 0.1 * RobotMap.speedRatio,1);
    	}
    	else if(Robot.gyro.getAngle() < -2) {
    		Robot.driveBase.Drive(lastSpeed * RobotMap.speedRatio, 0, -0.1 * RobotMap.speedRatio,1);
    	}
    	else {
        Robot.driveBase.Drive(lastSpeed * RobotMap.speedRatio, 0, 0,1);
    	}
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
            return false;

    }

    // Called once after isFinished returns true
    protected void end() {
		System.out.println("Drive Forward END");
        Robot.driveBase.Drive(0, 0, 0, 1);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
		System.out.println("Drive Forward Interrupted");
        Robot.driveBase.Drive(0, 0, 0, 1);
    }
}
