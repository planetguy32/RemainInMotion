package me.planetguy.remaininmotion;

import me.planetguy.remaininmotion.core.Configuration;

public class CarriageMotionException extends Exception {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4411286708532256473L;

	public CarriageMotionException(String Message) {
		super(Message);
		// this.printStackTrace();

	}

	@Override
	public void printStackTrace() { // Mute exceptions depending on config
		if (!Configuration.Debug.MuteMotionExceptions) {
			super.printStackTrace();
		}
	}
}
