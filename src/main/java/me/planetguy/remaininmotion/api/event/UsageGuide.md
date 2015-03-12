Event-based API
=======================

All events are posted to RiMRegistry.blockMoveBus

1. If TileEntity exists, it is saved. Otherwise the IBlockPos may have a null TileEntity and NBT tag.
2. A BlockSelectForMoveEvent is posted when the block has been selected to be moved.
3. A BlockPreMoveEvent is posted when the block is about to be overwritten with RemIM's spectres.
4. All blocks are moved and placed at their new positions
5. One BlocksReplacedEvent is posted for the entire move operation
6. For each block that has a TileEntity:
   - A TEPreUnpackEvent is posted. The IBlockPos's entity will likely be null here.
   - The TileEntity is loaded from the NBT tag
   - A TEPrePlaceEvent is posted. You may replace the TileEntity in the IBlockPos here if you need a different TileEntity than TileEntity.createAndLoadEntity() produces.
   - The TileEntity is placed at its new position
   - A TEPostPlaceEvent is posted.
7. For each block that was moved:
   - If the block is being rotated, its rotateBlock method is called
   - If the block is being rotated, a BlockRotateEvent will be posted for it
   - The block's onBlockAdded method is called 
   - A MotionFinalizeEvent is posted for it