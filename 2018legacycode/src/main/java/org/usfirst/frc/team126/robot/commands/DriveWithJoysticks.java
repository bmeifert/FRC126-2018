package org.usfirst.frc.team126.robot.commands;

import org.usfirst.frc.team126.robot.Robot;
import org.usfirst.frc.team126.robot.RobotMap;
import org.usfirst.frc.team126.robot.subsystems.*;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
/**
 *
 */
public class DriveWithJoysticks extends Command {
	double fb,lr,rot,presc,tl,tr,tx;
	boolean xboxLT, xboxRT, xboxRS, xboxLS, camMode, enabled;
	public DriveWithJoysticks() {
		requires(Robot.driveBase);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
			
	}

	// Called repeatedly when this Command is scheduled to run
	@SuppressWarnings("static-access")
	@Override
	protected void execute() {
		if (Robot.arm.isArmNeedsMove()) {
			return;
		}
		
		if (DSdata.isAutonomous()) {
			return;
		}
		fb = Robot.oi.driveController.getRawAxis(RobotMap.stickY); // Get and assign controller values
		tl = Robot.oi.driveController.getRawAxis(RobotMap.Ltrigger);
		tr = Robot.oi.driveController.getRawAxis(RobotMap.Rtrigger);
		rot = Robot.oi.driveController.getRawAxis(RobotMap.stickX);
		xboxLT = Robot.oi.driveController.getRawButton(RobotMap.xboxLT);
		xboxRT = Robot.oi.driveController.getRawButton(RobotMap.xboxRT);
		xboxLS = Robot.oi.driveController.getRawButton(RobotMap.xboxLS);
		xboxRS = Robot.oi.driveController.getRawButton(RobotMap.xboxRS);
		
		//System.out.printf("fb %0.2f, rot %0.2f, tl %0.2f, tr %0.2f\n", fb, rot, tl, tr);
		
		presc = 1; // Xbox prescision control
	
		if(tr > 0) {
			lr = tr;
		}
		else {
			lr = tl * -1;
		}
		Robot.oi.driveController.setRumble(GenericHID.RumbleType.kLeftRumble, 0); // Don't rumble the controller when driving normally
		Robot.oi.driveController.setRumble(GenericHID.RumbleType.kRightRumble, 0);
		if(Math.abs(fb) < 0.1) {
			fb = 0;
		}
		if(Math.abs(lr) < 0.1) {
			lr = 0;
		}
		if(Math.abs(rot) < 0.1) {
			rot = 0;
		}
	    Robot.driveBase.Drive((fb * -1),(lr * -1),(rot * -0.5),presc); // Drive with standard inversion offsets
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
