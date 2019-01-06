package org.usfirst.frc.team126.robot.subsystems;

import org.usfirst.frc.team126.robot.Robot;
import org.usfirst.frc.team126.robot.RobotMap;
import org.usfirst.frc.team126.robot.commands.DriveWithJoysticks;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.lang.Math;
/**
 * climberMotor.set(ControlMode.PercentOutput,0.0); 
 */
public class MecanumDrivebase extends Subsystem {

	double frontLeftMultiplier;
	double frontRightMultiplier;
	double backLeftMultiplier;
	double backRightMultiplier;
	double gyroValue;
	double stress;
	public void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoysticks());
	}
	public void Drive(double fb, double lr, double rot, double genmult) { /** Coefficient Drive Base by Keith Meifert **/
		fb = fb * RobotMap.fbinversion;
		lr = lr * RobotMap.lrinversion;
		rot = rot * RobotMap.rotinversion;
		frontLeftMultiplier = fb + rot;
		frontRightMultiplier = fb - rot;
		backLeftMultiplier = fb + rot;
		backRightMultiplier = fb - rot;
		if(lr > 0) { // Sideways coefficients (take precedence over but do not limit movement)
			frontLeftMultiplier = frontLeftMultiplier + Math.abs(lr);
			frontRightMultiplier = frontRightMultiplier - Math.abs(lr);
			backLeftMultiplier = backLeftMultiplier - Math.abs(lr);
			backRightMultiplier = backRightMultiplier + Math.abs(lr);
		}
		else {
			frontLeftMultiplier = frontLeftMultiplier - Math.abs(lr);
			frontRightMultiplier = frontRightMultiplier + Math.abs(lr);
			backLeftMultiplier = backLeftMultiplier + Math.abs(lr);
			backRightMultiplier = backRightMultiplier - Math.abs(lr);	
		}
		
		/**
		frontLeftMultiplier = fb;   // Default values for debug
		frontRightMultiplier = fb;
		backLeftMultiplier = fb;
		backRightMultiplier = fb;
		**/
		
		frontLeftMultiplier = frontLeftMultiplier * genmult; // Set general speed multiplier
		frontRightMultiplier = frontRightMultiplier * genmult;
		backLeftMultiplier = backLeftMultiplier * genmult;
		backRightMultiplier = backRightMultiplier * genmult;
		
		Robot.frontLeft.set(ControlMode.PercentOutput,(frontLeftMultiplier * RobotMap.flinversion)); // Set motors with correct inversions
		Robot.frontRight.set(ControlMode.PercentOutput,(frontRightMultiplier * RobotMap.frinversion));
		Robot.backLeft.set(ControlMode.PercentOutput,(backLeftMultiplier) * RobotMap.blinversion);
		Robot.backRight.set(ControlMode.PercentOutput,(backRightMultiplier * RobotMap.brinversion));
		
		stress += (Math.abs(frontLeftMultiplier) + Math.abs(frontRightMultiplier) + Math.abs(backLeftMultiplier) + Math.abs(backRightMultiplier)) / 4;
		stress = (stress * 49) / 50;
		SmartDashboard.putNumber("Motor Stress", stress);
	}
	public void RotateInPlace(double direction, double genmult) {
		direction = direction * RobotMap.rotinversion;
		frontLeftMultiplier = direction * 1;
		frontRightMultiplier = direction * -1;
		backLeftMultiplier = direction * 1;
		backRightMultiplier = direction * -1;
		
		frontLeftMultiplier = frontLeftMultiplier * genmult; // Set general speed multiplier
		frontRightMultiplier = frontRightMultiplier * genmult;
		backLeftMultiplier = backLeftMultiplier * genmult;
		backRightMultiplier = backRightMultiplier * genmult;
		
		
		Robot.frontLeft.set(ControlMode.PercentOutput,(frontLeftMultiplier * RobotMap.flinversion)); // Set motors with correct inversions
		Robot.frontRight.set(ControlMode.PercentOutput,(frontRightMultiplier * RobotMap.frinversion));
		Robot.backLeft.set(ControlMode.PercentOutput,(backLeftMultiplier) * RobotMap.blinversion);
		Robot.backRight.set(ControlMode.PercentOutput,(backRightMultiplier * RobotMap.brinversion));
	}
}
