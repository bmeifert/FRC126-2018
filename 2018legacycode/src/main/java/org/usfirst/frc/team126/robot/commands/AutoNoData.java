package org.usfirst.frc.team126.robot.commands;
import org.usfirst.frc.team126.robot.subsystems.Arm;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoNoData extends CommandGroup {

    public AutoNoData() {
    	addSequential(new ZeroArms(), 10);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 2.2);
    	addSequential(new DriveForward(), 2.2);
    	
    	}
}
