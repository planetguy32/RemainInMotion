package me.planetguy.remaininmotion.spectre ;

import me.planetguy.remaininmotion.BlockRecord;

public class TransduplicativeSpectreEntity extends TeleportativeSpectreEntity {

	public void Release(){
		doRelease();
		
		for(BlockRecord r:Body)
			ShiftBlockPosition(r);
		
		doRelease();
	}

}
