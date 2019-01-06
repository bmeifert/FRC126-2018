package org.usfirst.frc.team126.robot.commands;
//import org.usfirst.frc.team126.robot.subsystems.Arm;

import org.usfirst.frc.team126.robot.subsystems.Arm;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoLeftToRight extends CommandGroup {

    public AutoLeftToRight() {
    	addSequential(new ZeroArms(), 10);
    	
    	addSequential(new DriveForward(), 3.8);
    	addSequential(new RotateDegrees(90), 3);

    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 2);
    	addSequential(new DriveForward(), 2.6);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 3);
    	addSequential(new RotateDegrees(-90), 3);
    	
    	
    	
    	addSequential(new ArmMovement(Arm.arm_position.arm_scale), 5);
    	/*
    	addSequential(new ZeroArms(), 10);
    	addSequential(new DriveForward(), 2.5);
    	addSequential(new AlignArea(2), 5);
    	addSequential(new DriveForward(), 0.5);
    	addSequential(new Pause(), 0.1);
    	addSequential(new RotateDegrees(85), 3);
    	addSequential(new DriveForward(), 3);
    	addSequential(new RotateDegrees(-90), 3);
    	addSequential(new AlignYaw(), 3);
    	addSequential(new AlignArea(3), 3);
    	addSequential(new ArmMovement(Arm.arm_position.arm_scale_place), 15);
    	*/
    }
}
