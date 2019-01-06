package org.usfirst.frc.team126.robot.commands;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team126.robot.subsystems.Arm;

public class AutoRightToRight extends CommandGroup {

    public AutoRightToRight() {
    	addSequential(new ZeroArms(), 10);
    	addParallel(new ArmMovement(Arm.arm_position.arm_switch), 3);
    	addSequential(new AccelerateForward(), 3.4);
    	//addSequential(new DriveForward(), 5.2);
    	addSequential(new RotateDegrees(-90), 3);
    	addSequential(new DriveBackwards(), 0.5);
    	addSequential(new ArmMovement(Arm.arm_position.arm_scale), 4);
    	addSequential(new DriveForward(), 0.75);
    	addSequential(new ArmMovement(Arm.arm_position.arm_spit_cube), 1);
    	addSequential(new DriveBackwards(), 0.75);
    	addSequential(new ArmMovement(Arm.arm_position.arm_grab_cube), 15);

    	}
}
