package org.usfirst.frc.team126.robot.commands;

import org.usfirst.frc.team126.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ZeroArms extends Command {
	int count = 0;
	
	double upper_arm_zero_speed = -0.4;
	double lower_arm_zero_speed = -0.45;
	
    public ZeroArms() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.arm);
    	SmartDashboard.putBoolean("ZeroArms End",false);
    	SmartDashboard.putBoolean("ZeroArms Finished",false);
    	System.out.println("ZeroArms ZeroArms()");
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("ZeroArms initialize()");
    	Robot.arm.setArmsZeroed(false);
		Robot.arm.setLowerZeroed(false);
		Robot.arm.setUpperZeroed(false);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	boolean zeroed = Robot.arm.getArmsZeroed();
    	
    	count++;

    	SmartDashboard.putNumber("ZeroArms Count",count);
    	SmartDashboard.putBoolean("ZeroArms Zeroed",zeroed);

    	System.out.printf("ZeroArms execute(), count %d\n",count);
    	System.out.println("ZeroArms execute(), zeroed " + zeroed);

    	if (!zeroed) {
    		if (!Robot.arm.getLowerZeroed()) {
        	   if (count > 150 || Robot.arm.getUpperZeroed()) { 
        		   Robot.arm.moveLowerArmNoLimit(lower_arm_zero_speed);
        	   }
    		} else {
         	   Robot.arm.moveLowerArmNoLimit(0);
    		}
    		if (!Robot.arm.getUpperZeroed()) {
         		Robot.arm.moveUpperArmNoLimit(upper_arm_zero_speed);
    		} else {
         		Robot.arm.moveUpperArmNoLimit(0);  			
    		}
    	}   	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	boolean zeroed = Robot.arm.getArmsZeroed();
    	SmartDashboard.putBoolean("ZeroArms End",zeroed);
    	SmartDashboard.putBoolean("ZeroArms Zeroed",zeroed);
    	if (zeroed) {
        	System.out.println("ZeroArms isfinished: true");
        	Robot.arm.moveLowerArmNoLimit(0);
      		Robot.arm.moveUpperArmNoLimit(0);
      		return true;
    	}
    	else {
    		return false; 
    	}
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.arm.moveLowerArmNoLimit(0);
  		Robot.arm.moveUpperArmNoLimit(0);
    	SmartDashboard.putBoolean("ZeroArms Finished",true);
    	System.out.printf("ZeroArms end()\n");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.arm.moveLowerArmNoLimit(0);
  		Robot.arm.moveUpperArmNoLimit(0);
    }
}
