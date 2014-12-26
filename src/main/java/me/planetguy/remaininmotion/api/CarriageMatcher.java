package me.planetguy.remaininmotion.api;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

/**
 * Used to mark something as a carriage.
 * 
 * Note that the simple case of tile entities that are Moveables and match
 * things with the same ID and meta is covered by RiM by default, so a
 * CarriageMatcher is unnecessary.
 * 
 * @author bill
 * 
 */
public interface CarriageMatcher {

	/**
	 * Returns whether the tile entities are compatible.
	 * 
	 * @param block1
	 * @param meta1
	 * @param entity1
	 * 
	 * @param bloc2k
	 * @param meta2
	 * @param entity2
	 * 
	 */
	public boolean matches(Block block1, int meta1, TileEntity entity1, Block bloc2k, int meta2, TileEntity entity2);

	/**
	 * Returns the carriage at the given location.
	 * 
	 * @param block
	 * @param meta
	 * @param te
	 * @return
	 */
	public Moveable getCarriage(Block block, int meta, TileEntity te);

}
