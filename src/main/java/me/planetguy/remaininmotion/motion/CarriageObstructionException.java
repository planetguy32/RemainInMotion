package me.planetguy.remaininmotion.motion;

public class CarriageObstructionException extends CarriageMotionException {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 411737885124698990L;
	public int					X;
	public int					Y;
	public int					Z;

	public CarriageObstructionException(String Message, int X, int Y, int Z) {
		super(Message);

		this.X = X;
		this.Y = Y;
		this.Z = Z;
	}
}
