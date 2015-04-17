package me.planetguy.remaininmotion.api.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class PostRenderDuringMovementEvent extends Event {

    public RenderBlocks renderBlocks;
    public int x;
    public int y;
    public int z;
    public TileEntity tile;
    public int pass;

    /**
     * Called after rendering a block being moved.
     *
     * @param renderer
     * @param x
     * @param y
     * @param z
     * @param entity
     * @param pass
     *
     */
    public PostRenderDuringMovementEvent(RenderBlocks renderer, int x, int y, int z, TileEntity entity, int pass) {
        renderBlocks = renderer;
        this.x = x;
        this.y = y;
        this.z = z;
        this.tile = entity;
        this.pass = pass;
    }
}
