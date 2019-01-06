package org.usfirst.frc.team126.robot.subsystems;

import org.usfirst.frc.team126.robot.commands.StartCompressor;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Pnuematics extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new StartCompressor());
    }
}

