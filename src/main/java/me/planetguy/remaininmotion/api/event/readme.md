Event-based API
=======================

The event-based API is designed to isolate users from 

All events are posted to RiMRegistry.blockMoveBus, which can be subscribed to by calling me.planetguy.remaininmotion.api.event.EventManager.registerEventHandler(Object) with the intended handler.

1. If TileEntity exists, it is saved. Otherwise the IBlockPos may have a null TileEntity and NBT tag.
2. A BlockSelectForMoveEvent is posted. The event can be canceled using Forge's canceling API, but calling cancel(String) lets you specify a player-readable reason that the move is impossible. The event can also be excluded, which causes the block to stay in place but allows the rest of the blocks being moved to move. This is the only opportunity to stop movement. If the motion is rotation, the subclass BlockSelectForRotateEvent will be posted instead.
3. A BlockPreMoveEvent is posted when the block is about to be overwritten with RemIM's spectres.
4. All blocks are moved and placed at their new positions, without triggering block updates or other callbacks.
5. One BlocksReplacedEvent is posted for the entire move operation.
6. For each block that has a TileEntity:
   - A TEPreUnpackEvent is posted. The IBlockPos's entity will likely be null here. If the operation is a rotation, then the subclass RotatingTEPreUnpackEvent will be used instead.
   - The TileEntity is loaded from the NBT tag
   - A TEPrePlaceEvent is posted. You may replace the TileEntity in the IBlockPos here if you need a different TileEntity than TileEntity.createAndLoadEntity() produces (eg. creating ForgeMultipart tile entities).
   - The TileEntity is placed at its new position
   - A TEPostPlaceEvent is posted.
7. For each block that was moved:
   - If the block is being rotated, its rotateBlock method is called
   - If the block is being rotated, a BlockRotateEvent will be posted for it
   - A CancelableOnBlockAddedEvent will be posted for it. If the event is never canceled, the block's onBlockAdded method will be called.
   - A MotionFinalizeEvent is posted for the block
