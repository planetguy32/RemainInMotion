package me.planetguy.remaininmotion.api.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

/**
 * Called before rendering a block being moved. Cancelable for overriding.
 *
 */

@SideOnly(Side.CLIENT)
@Cancelable
public class PreRenderDuringMovementEvent extends Event {

    public RenderBlocks renderBlocks;
    public int x;
    public int y;
    public int z;
    public TileEntity tile;
    public int pass;



}
