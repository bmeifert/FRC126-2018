package org.usfirst.frc.team126.robot.subsystems;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Get data from driverstation
 */
public class DSdata extends Subsystem {
	public static String storedGameData = "";
	public static boolean isGameDataStored = false;

	static DriverStation.Alliance allianceColor;
	public void initDefaultCommand() {
	}
	
	public static int getLocation() {
		return DriverStation.getInstance().getLocation();
	}
	
	public static String getAlliance() {
		allianceColor = DriverStation.getInstance().getAlliance();
		if(allianceColor == DriverStation.Alliance.Blue) {
			return "blue";
		}
		else {
			return "red";
		}
	}
	
	public static double getRemainingTime() {
		return DriverStation.getInstance().getMatchTime();
	}
	
	public static String getGameData() {
		if(DSdata.isGameDataStored) {
			return DSdata.storedGameData;
		}
		else {
			if(DriverStation.getInstance().getGameSpecificMessage().length() > 1) {
				DSdata.storedGameData = DriverStation.getInstance().getGameSpecificMessage();
				DSdata.isGameDataStored = true;
			}
			else {
				DSdata.storedGameData = "";
				DSdata.isGameDataStored = false;
			}
			return DSdata.storedGameData;
		}
	}
	
	public static double getVoltage() {
		return RobotController.getBatteryVoltage();
	}
	
	public static boolean isAutonomous() {
		return DriverStation.getInstance().isAutonomous();
	}
	
}

