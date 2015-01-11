package me.planetguy.remaininmotion;

import me.planetguy.lib.util.Lang;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RiMConfiguration;

public class CarriageMotionException extends Exception {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4411286708532256473L;
	
	public final ErrorStates errorState;

	public CarriageMotionException(ErrorStates state, String additionalMessage) {
		super(state.message + additionalMessage);
		this.errorState=state;
	}
	
	public CarriageMotionException(ErrorStates state) {
		this(state, "");
	}

	@Override
	public void printStackTrace() { // Mute exceptions depending on config
		if (!RiMConfiguration.Debug.MuteMotionExceptions) {
			super.printStackTrace();
		}
	}
	
	public static enum ErrorStates{
		//TODO move all literals to lang file
		
		//general error states
		NOT_ENOUGH_ENERGY("carriage does not have enough energy (has "),
		ALREADY_ACTIVE(Lang.translate(ModRiM.Handle	+ ".active")),
		NO_CARRIAGE(Lang.translate(ModRiM.Handle + ".noValidCarriage")),
		OVERSIZE(Lang.translate(ModRiM.Handle + ".overburdened")),
		
		//Unique to motors or anchored controllers
		ANCHORED_PUSHING(Lang.translate(ModRiM.Handle + ".noPushWhenAnchored")),
		ANCHORED_PULLING(Lang.translate(ModRiM.Handle + ".noPullWhenAnchored")), 
		ANCHORED_MOVING_SELF("carriage is attempting to move itself"),
		ANCHORED_BLOCKING_SELF("carriage is obstructed by its motor"),
		
		//Indicates that rotator carriages are banned
		ROTATION_BANNED(Lang.translate(ModRiM.Handle + ".noRotatorCarriage")),
		
		//special handling is necessary - expect a second arg in CME constructor
		INVALID_CARRIAGE("Invalid carriage setup: "),
		OBSTRUCTED("obstructed by "), 
		
				;
		
		private String message;
		
		ErrorStates(String message){
			this.message=message;
		}
	}
}
