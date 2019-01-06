package org.usfirst.frc.team126.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team126.robot.subsystems.Arm;
public class AutoLeftToCenter extends CommandGroup {

    public AutoLeftToCenter() {
    	
    	
    	
    	addSequential(new DriveForward(), 2);
    	addSequential(new RotateDegrees(-90), 5);
    	addSequential(new DriveForward(), 2);
    	addSequential(new RotateDegrees(-90), 5);
    	addSequential(new DriveForward(), 1);
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	/*
    	addSequential(new ZeroArms(), 10);
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 2.8);
    	addSequential(new DriveForward(), 2.7);
    	addSequential(new RotateDegrees(90), 3);
    	addSequential(new ArmMovement(Arm.arm_position.arm_switch), 2);
    	addSequential(new DriveForward(), 0.6);
    	addSequential(new ArmMovement(Arm.arm_position.arm_drop_cube), 1);
    	addSequential(new Pause(), 0.5);
    	addSequential(new DriveBackwards(), 0.75);
    	addSequential(new ArmMovement(Arm.arm_position.arm_grab_cube), 5);
    	addSequential(new DriveRight(), 2);
    	*/
    	
    	
    	
    	
    	
    	
        //addSequential(new DriveForward(), 4.2);
    	//addSequential(new RotateDegrees(90), 3);
    	//addSequential(new AlignScale(), 3);
    	//addSequential(new AlignArea(1), 3);
    	//addSequential(new ArmMovement(Arm.arm_position.arm_scale_place), 15);
    }
}
