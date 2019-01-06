package org.usfirst.frc.team126.robot.subsystems;

import org.usfirst.frc.team126.robot.commands.Pistons;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Solonoids extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	boolean grabber_closed = true;
	boolean catcher_out = false;
	
	Solenoid s1 = new Solenoid(0);
	Solenoid s2 = new Solenoid(1);
	Solenoid shiv = new Solenoid(2);

	public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new Pistons());
    }

    public void toggle_grabber() {
    	if (grabber_closed) {  		
    		grabber_open();
    	} else {
    		grabber_close();
    	}
    }
    public void grabber_open(){
    	grabber_closed=false;
    	s1.set(true);
    }

    public void grabber_close(){
    	grabber_closed=true;
    	s1.set(false);
    }
    public void pusher_out(){
    	s2.set(true);
    }
    public void pusher_in(){
    	s2.set(false);
    }
    
    public boolean getGrabberClosed() {
    	return grabber_closed;
    }

    public void cowCatcherExtend() {
    	catcher_out=true;
    	shiv.set(true);
    }

    public void cowCatcherRetract() {
    	catcher_out=false;
    	shiv.set(false);
    }
    public void toggle_catcher() {
    	if (catcher_out) {  		
    		cowCatcherRetract();
    	} else {
    		cowCatcherExtend();
    	}
    }

}

