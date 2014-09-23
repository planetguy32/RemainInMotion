package me.planetguy.remaininmotion ;

public class TransduplicativeSpectreEntity extends TeleportativeSpectreEntity {

	public void Release(){
		doRelease();
		
		for(BlockRecord r:Body)
			ShiftBlockPosition(r);
		
		doRelease();
	}

}
