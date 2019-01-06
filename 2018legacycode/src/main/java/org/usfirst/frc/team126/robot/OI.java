package org.usfirst.frc.team126.robot;

import edu.wpi.first.wpilibj.Joystick;


public class OI {
	public static Joystick driveController = new Joystick(0);
	public static Joystick armController = new Joystick(1);
	
	public static void SwitchControllers(){
		driveController = new Joystick(1);
		armController = new Joystick(0);
	}
}
