package org.usfirst.frc.team126.robot.subsystems;

import org.usfirst.frc.team126.robot.Robot;
//import org.usfirst.frc.team126.robot.commands.IntakeTest;
import org.usfirst.frc.team126.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	//(new IntakeTest());
    }
    public void setIntake(double intakeSpeed) {
    	if(RobotMap.intakeInverted) {
        	Robot.intake1.set(intakeSpeed * -1);
    	}
    	else {
        	Robot.intake1.set(intakeSpeed);
    	}
    }
    public void intakeOff() {
    	Robot.intake1.set(0);
    	//Robot.intake2.set(0);
    }
}

