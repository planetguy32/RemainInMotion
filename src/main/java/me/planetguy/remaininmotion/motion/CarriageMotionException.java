package me.planetguy.remaininmotion.motion;

import me.planetguy.remaininmotion.core.RiMConfiguration;

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
		if (!RiMConfiguration.Debug.MuteMotionExceptions) {
			super.printStackTrace();
		}
	}
}
