package me.planetguy.remaininmotion.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import me.planetguy.remaininmotion.drive.TileEntityCarriageDrive;

public class PacketCarriageUpdate {
	
	//Squared reach distance
	public static final double PLAYER_REACH=5*5;
	
	public static void send(TileEntityCarriageDrive te, long flags){
		NBTTagCompound tag=new NBTTagCompound();
		tag.setInteger("x", te.xCoord);
		tag.setInteger("y", te.yCoord);
		tag.setInteger("z", te.zCoord);
		
		tag.setLong("flags", flags);
		
		PacketManager.sendPacketToServer(TypesUp.RECONFIGURE, tag);
	}

	public static void receive(NBTTagCompound body, EntityPlayerMP playerEntity) {
		int x=body.getInteger("x");
		int y=body.getInteger("y");
		int z=body.getInteger("z");
		
		double senderDistanceSq=sq(x-playerEntity.posX)+sq(y-playerEntity.posY)+sq(z-playerEntity.posZ);
		
		if(senderDistanceSq<PLAYER_REACH){
			TileEntity te=playerEntity.worldObj.getTileEntity(x, y, z);
			if(te instanceof TileEntityCarriageDrive){
				((TileEntityCarriageDrive) te).setConfiguration(body.getLong("flags"));
			}
		}
	}
	
	private static double sq(double i){
		return i*i;
	}

}
