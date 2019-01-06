package org.usfirst.frc.team126.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team126.robot.subsystems.Arm;

public class AutoCenterToRight extends CommandGroup {

    public AutoCenterToRight() {
    	/*
    	addSequential(new DriveForward(), 0.5);
    	addSequential(new ZeroArms(), 10);
    	addSequential(new DriveRight(), 2.3);
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 1.7);
    	addSequential(new DriveForward(), 1.7);
    	addSequential(new ArmMovement(Arm.arm_position.arm_switch), 2);
    	addSequential(new ArmMovement(Arm.arm_position.arm_spit_cube), 1);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_grab_cube), 1);
    	addSequential(new DriveBackwards(), 1);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_grab_cube), 5);
    	addSequential(new DriveLeft(), 2.3);
    	
    	addSequential(new ArmMovement(Arm.arm_position.arm_grab_cube), 5);
    	
    	addParallel(new DriveForward(), 0.5);
    	addSequential(new RunIntake(), 1.5);
    	
    	addParallel(new DriveBackwards(), 1);
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 3);
    	*/
    	/*
    	addSequential(new ZeroArms(), 10);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 1);
    	addSequential(new DriveForward(), 1);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 3);
    	addSequential(new RotateDegrees(30), 3);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 1.1);
    	addSequential(new DriveForward(), 1.1);
    	
    	addSequential(new ArmMovement(Arm.arm_position.arm_switch), 2);
    	addSequential(new ArmMovement(Arm.arm_position.arm_spit_cube), 1); // finish first auto
    
    	addParallel(new ArmMovement(Arm.arm_position.arm_grab_cube), 1.25); // grab cube
    	addSequential(new DriveBackwards(), 1.25);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_grab_cube), 3);
    	addSequential(new RotateDegrees(-30), 3);
    	
    	addSequential(new ArmMovement(Arm.arm_position.arm_grab_cube), 3);
    	
    	addParallel(new DriveForward(), 1.25);
    	addSequential(new RunIntake(), 1.25);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 0.5); // second auto
    	addSequential(new DriveBackwards(), 0.5);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 3);
    	addSequential(new RotateDegrees(35), 3);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 0.9);
    	addSequential(new DriveForward(), 0.9);
    	
    	addSequential(new ArmMovement(Arm.arm_position.arm_switch), 2);
    	addParallel(new DriveForward(), 0.25);
    	addSequential(new ArmMovement(Arm.arm_position.arm_spit_cube), 1);
    	*/
    	addSequential(new ZeroArms(), 10);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 0.5);
    	addSequential(new DriveForward(), 0.5);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 3);
    	addSequential(new RotateDegrees(45), 3);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 1.25);
    	addSequential(new DriveForward(), 1.5);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 3);
    	addSequential(new RotateDegrees(-40), 3);
    	
    	addSequential(new ArmMovement(Arm.arm_position.arm_switch), 2);
    	
    	addSequential(new DriveForward(), 1.25);
    	addSequential(new ArmMovement(Arm.arm_position.arm_drop_cube), 1);
    	addSequential(new Pause(), 0.5);
    	
    	addParallel(new ArmMovement(Arm.arm_position.arm_grab_cube), 1);
    	addSequential(new DriveBackwards(), 1);
    	
    	addSequential(new ArmMovement(Arm.arm_position.arm_grab_cube), 5);
    	
    	
    	
    	
    	
    	
    }
}