package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockPlaceContext.class)
public class ZBlockFaceContext extends UseOnContext {

    /**Dripstone block has a bug in its code where it crashes if placed naturally without an attached
     * player, so it is exempt from this list.*/
    public ZBlockFaceContext(Player $$0, InteractionHand $$1, BlockHitResult $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(method = "getNearestLookingVerticalDirection", at = @At("HEAD"), cancellable = true)
    public void roundabout$getNearestLookingVDirection(CallbackInfoReturnable<Direction> cir) {
        if (this.getPlayer() == null){
            cir.setReturnValue(roundabout$getFacingAxis(Direction.Axis.Y));
        }
    }
    public Direction roundabout$getFacingAxis(Direction.Axis axis) {
        return switch (axis) {
            default -> throw new IncompatibleClassChangeError();
            case X -> {
                if (Direction.EAST.isFacingAngle(this.getRotation())) {
                    yield Direction.EAST;
                }
                yield Direction.WEST;
            }
            case Z -> {
                if (Direction.SOUTH.isFacingAngle(this.getRotation())) {
                    yield Direction.SOUTH;
                }
                yield Direction.NORTH;
            }
            case Y -> this.getRotation() < 0.0f ? Direction.UP : Direction.DOWN;
        };
    }
}