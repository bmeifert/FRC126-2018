		
package org.usfirst.frc.team126.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team126.robot.commands.AutoRightToRight;
import org.usfirst.frc.team126.robot.commands.ZeroArms;
import org.usfirst.frc.team126.robot.commands.AutoLeftToLeft;
import org.usfirst.frc.team126.robot.commands.AutoRightToLeft;
import org.usfirst.frc.team126.robot.commands.AutoLeftToRight;
import org.usfirst.frc.team126.robot.commands.AutoNoData;
import org.usfirst.frc.team126.robot.commands.AutoRightToCenter;
import org.usfirst.frc.team126.robot.commands.AutoCenterToLeft;
import org.usfirst.frc.team126.robot.commands.AutoCenterToRight;
import org.usfirst.frc.team126.robot.commands.AutoLeftToCenter;
import org.usfirst.frc.team126.robot.subsystems.Arm;
import org.usfirst.frc.team126.robot.subsystems.DSdata;
import org.usfirst.frc.team126.robot.subsystems.Gyro;
import org.usfirst.frc.team126.robot.subsystems.MecanumDrivebase;
import org.usfirst.frc.team126.robot.subsystems.Pnuematics;
import org.usfirst.frc.team126.robot.subsystems.Solonoids;
import org.usfirst.frc.team126.robot.subsystems.Intake;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Robot extends IterativeRobot {
	public static Command autonomous;
	public static MecanumDrivebase driveBase; // Init components
	public static Pnuematics pnuematics;
	public static Solonoids solonoids;
	public static OI oi;
	public static Gyro gyro;
	public static Arm arm; 
	public static String robotLocation;
	public static String switchPriority;
	public static Intake intake;
	
	Preferences prefs;
	public static int RobotID;
	public static double ScaleMinTolerance;
	public static double ScaleMaxTolerance;
	public static double ScaleMaxSideTolerance;
	public static double ScaleMinSideTolerance;
	
	public static TalonSRX frontLeft = new TalonSRX(RobotMap.frontLeft); // define everything
	public static TalonSRX backLeft = new TalonSRX(RobotMap.backLeft);
	public static TalonSRX frontRight = new TalonSRX(RobotMap.frontRight);
	public static TalonSRX backRight = new TalonSRX(RobotMap.backRight);
	public static Spark intake1 = new Spark(9);
	// public static Spark intake2 = new Spark(8);
	


	boolean pick_auto = true; // Do auto based on game data
	//boolean pick_auto = false; // Just cross auto line

	@SuppressWarnings("rawtypes")
	SendableChooser robotType = new SendableChooser(); // Robot chooser
	@SuppressWarnings("rawtypes")
	SendableChooser robotposition = new SendableChooser(); // Position chooser
	@SuppressWarnings("rawtypes")
	SendableChooser priorityChooser = new SendableChooser(); // Priority chooser
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void robotInit() { // Import components
		oi = new OI();
		solonoids = new Solonoids();
		pnuematics = new Pnuematics();
		driveBase = new MecanumDrivebase();
		gyro = new Gyro();
		arm = new Arm();
		intake = new Intake();
		
		robotType.addObject("(Old) Compbot 2017", 0); // Robot chooser
		robotType.addObject("(Old) Practicebot 2017", 1);
		robotType.addDefault("[MATCH] Compbot 2018", 2);
		robotType.addObject("Practicebot 2018", 3);
		SmartDashboard.putData("WhichBot", robotType);
		
		robotposition.addDefault("Left", "left"); // Location chooser
		robotposition.addObject("Center", "center");
		robotposition.addObject("Right", "right");
		SmartDashboard.putData("BotPosition", robotposition);
		
		priorityChooser.addDefault("Scale Or Switch", "normal"); // Priority chooser
		priorityChooser.addObject("Switch Only", "switch");
		priorityChooser.addObject("Auto Line Only", "line");
		SmartDashboard.putData("AutoPriority", priorityChooser);
		
		Robot.arm.setArmsZeroed(false);
		
//		UsbCamera drivecam = CameraServer.getInstance().startAutomaticCapture();
//		drivecam.setResolution(640, 480);
		
	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}
	
	@Override
	public void autonomousInit() {
		robotLocation = (String) robotposition.getSelected();
		switchPriority = (String) priorityChooser.getSelected();
		if (pick_auto) {
			for(int x = 0; x < 100; x++) {
				if(DSdata.getGameData().length() == 3) {
					break;
				}
				else {
					Timer.delay(0.05);
				}
			}
			if(switchPriority == "normal") {
				if(DSdata.getGameData().length() == 3) { // if game data exists
					if(robotLocation == "left") { // if you're on the left
						if(DSdata.getGameData().charAt(1) == 'R') {
							if(DSdata.getGameData().charAt(0) == 'L') {
								autonomous = (Command) new AutoLeftToCenter();
							}
							else {
								autonomous = (Command) new AutoLeftToRight(); // left position to right scale
							}
						}
						else if(DSdata.getGameData().charAt(1) == 'L') {
							autonomous = (Command) new AutoLeftToLeft(); // left position to left scale
						}
					}
					else if(robotLocation == "right") { // if you're on the right
						if(DSdata.getGameData().charAt(1) == 'R') {
							autonomous = (Command) new AutoRightToRight(); // right position to right scale
						}
						else if(DSdata.getGameData().charAt(1) == 'L') {
							if(DSdata.getGameData().charAt(0) == 'R') {
								autonomous = (Command) new AutoRightToCenter();
							}
							else {
								autonomous = (Command) new AutoRightToLeft(); // right position to left scale
							}
						}
					}
					else if(robotLocation == "center") {
						if(DSdata.getGameData().charAt(0) == 'R') { // if you're in the center
							autonomous = (Command) new AutoCenterToRight(); // center position to right scale
						}
						else if(DSdata.getGameData().charAt(0) == 'L') {
							autonomous = (Command) new AutoCenterToLeft(); // center position to left scale
						}
					}
				}
				else { // if something went wrong then just cross the auto line and blame mechanical
					System.out.println("I blame mechanical");
					autonomous = (Command) new AutoNoData();
				}
			}
			else if(switchPriority == "switch") {
				if(DSdata.getGameData().length() == 3) { // if game data exists
					if(robotLocation == "left" && DSdata.getGameData().charAt(0) == 'L') {
						autonomous = (Command) new AutoLeftToCenter();
					}
					else if(robotLocation == "right" && DSdata.getGameData().charAt(0) == 'R') {
						autonomous = (Command) new AutoRightToCenter();
					}
					else if(robotLocation == "center" && DSdata.getGameData().charAt(0) == 'L') {
						autonomous = (Command) new AutoCenterToLeft();
					}
					else if(robotLocation == "center" && DSdata.getGameData().charAt(0) == 'R') {
						autonomous = (Command) new AutoCenterToRight();
					}
					else {
						autonomous = (Command) new AutoNoData();
					}
				}
			}
			else if(switchPriority == "line") {
				autonomous = (Command) new AutoNoData();
			}
		} 
		else { // Fallback
			autonomous = (Command) new AutoNoData();
		}
		if(DSdata.getGameData().length() != 3) {
			autonomous = (Command) new AutoNoData();
		}
		RobotID = (int) robotType.getSelected();
		RobotMap.setRobot(RobotID);
		Robot.arm.armInitMapValues();
    	Robot.arm.setArmsZeroed(false);
    	Robot.arm.setArmActive(false);
    	Robot.solonoids.cowCatcherExtend();
    	Robot.gyro.resetGyro();
		if(autonomous != null){
			autonomous.start();
		}
		else {
			System.out.println("Auto was null");
			autonomous = (Command) new AutoNoData();
			autonomous.start();
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		RobotID = (int) robotType.getSelected();
		RobotMap.setRobot(RobotID);
		Robot.arm.armInitMapValues();
    	Robot.arm.setArmActive(false);
		if(autonomous != null){
			autonomous.cancel();
		}
		if(!Robot.arm.getArmsZeroed()) {
	    	//Scheduler.getInstance().add(new ZeroArms()); // TODO uncomment
		}
    }

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}
