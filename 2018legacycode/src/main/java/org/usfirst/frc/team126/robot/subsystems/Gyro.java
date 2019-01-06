package org.usfirst.frc.team126.robot.subsystems;

import org.usfirst.frc.team126.robot.Robot;
import org.usfirst.frc.team126.robot.RobotMap;
import org.usfirst.frc.team126.robot.commands.DebugPeriodic;
import edu.wpi.first.wpilibj.SerialPort;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

/********************************************************************/
 
 public class Gyro extends Subsystem {

	private ADXRS450_Gyro gyro = null;
    AHRS ahrs;

	/**************************************************************************
	 Install a default command that will get called to
     * process the camera data
     */
	
	public Gyro() {
		initSensors();
		resetGyro();
	}
	
	    public void initDefaultCommand() {
		setDefaultCommand(new DebugPeriodic());
	}

    /* Does the camera see a valid target */
    public double getAngle() {
	    	if ( gyro != null ) {
	    		  return gyro.getAngle();
	    	}
	    	else {
	    		return 0.0;
	    	}
		

    }
    
   public void resetGyro() {
	    	if ( gyro != null ) {
	    		  gyro.reset();
	    	}
	   }
    private void initSensors() {
    	if ( gyro == null ) {
        	try {
        	    gyro = new ADXRS450_Gyro();
        	}
        	catch(RuntimeException e){
        		gyro = null;
        	}
    	}

    }	
}
