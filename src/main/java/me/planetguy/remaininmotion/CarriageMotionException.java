package me.planetguy.remaininmotion;

import me.planetguy.remaininmotion.core.Configuration;

public class CarriageMotionException extends Exception {
	public CarriageMotionException(String Message) {
		super(Message);
		// this.printStackTrace();

	}

	public void printStackTrace() { // Mute exceptions depending on config
		if (!Configuration.Debug.MuteMotionExceptions) super.printStackTrace();
	}
}
