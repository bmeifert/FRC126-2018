package org.usfirst.frc.team126.robot.commands;
import org.usfirst.frc.team126.robot.Robot;
import org.usfirst.frc.team126.robot.subsystems.DSdata;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DebugPeriodic extends Command {
	double lastVoltage;
	public DebugPeriodic() {
		requires(Robot.gyro);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run`
	@Override
	protected void execute() {
		SmartDashboard.putNumber("Voltage", DSdata.getVoltage());
		SmartDashboard.putNumber("Gyro Angle", Robot.gyro.getAngle());
		SmartDashboard.putNumber("Time Left", DSdata.getRemainingTime());
	}
	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
