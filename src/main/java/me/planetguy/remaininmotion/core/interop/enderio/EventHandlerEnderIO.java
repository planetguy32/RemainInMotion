package me.planetguy.remaininmotion.core.interop.enderio;

import java.util.List;

/*
import crazypants.enderio.block.BlockDarkSteelLadder;
import crazypants.enderio.conduit.BlockConduitBundle;
import crazypants.enderio.conduit.IConduit;
import crazypants.enderio.conduit.TileConduitBundle;
import crazypants.enderio.item.skull.BlockEndermanSkull;
import crazypants.enderio.item.skull.TileEndermanSkull;
import crazypants.enderio.machine.AbstractMachineBlock;
import crazypants.enderio.machine.AbstractMachineEntity;
import crazypants.enderio.machine.enchanter.BlockEnchanter;
import crazypants.enderio.machine.enchanter.TileEnchanter;
import crazypants.enderio.machine.light.BlockElectricLight;
import crazypants.enderio.machine.light.TileElectricLight;
*/
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import me.planetguy.lib.util.Reflection;
import me.planetguy.lib.util.transformations.Rotator;
import me.planetguy.remaininmotion.api.event.BlockRotateEvent;
import me.planetguy.remaininmotion.api.event.IBlockPos;

public class EventHandlerEnderIO {

	/*
	public void handleRotation(BlockRotateEvent e) {
		try {
			IBlockPos pos=e.location;
			World world=pos.world();
			int x=pos.x();
			int y=pos.y();
			int z=pos.z();
			ForgeDirection axis=e.axis;

			Block targetBlock=world.getBlock(x,y,z);
			if(targetBlock instanceof BlockDarkSteelLadder) {
				world.setBlockMetadataWithNotify(x, y, z, 
						Rotator.newSide(world.getBlockMetadata(x, y, z), e.axis), 3);
			}else if(targetBlock instanceof BlockConduitBundle){
				List<IConduit> conduits = (List<IConduit>) Reflection.get(TileConduitBundle.class, world.getTileEntity(x, y, z), "conduits");
				for(IConduit conduit:conduits) {
					conduit.onAddedToBundle();
				}
			}else if(targetBlock instanceof BlockEndermanSkull) {
				int yaw=(int) Reflection.get(TileEndermanSkull.class, world.getTileEntity(x, y, z), "yaw");
				if(axis==ForgeDirection.UP)
					yaw=-90;
				else if(axis==ForgeDirection.DOWN)
					yaw += 90;
				yaw=(yaw+360)%360;
				Reflection.set(TileEndermanSkull.class, world.getTileEntity(x, y, z), "yaw", yaw);
			}else if(targetBlock instanceof AbstractMachineBlock) {
				AbstractMachineEntity ame=(AbstractMachineEntity) world.getTileEntity(x, y, z);
				ame.setFacing((short) Rotator.newSide(ame.getFacing(), axis));
			}else if(targetBlock instanceof BlockEnchanter) {
				TileEnchanter te=(TileEnchanter) world.getTileEntity(x, y, z);
				te.setFacing((short) Rotator.newSide(te.getFacing(), axis));
			}else if(targetBlock instanceof BlockElectricLight) {
				TileElectricLight light=(TileElectricLight) world.getTileEntity(x, y, z);
				light.setFace(ForgeDirection.values()[Rotator.newSide(light.getFace().ordinal(), axis)]);
			}
		}catch(Exception ignored) {
		}
	}*/

}
