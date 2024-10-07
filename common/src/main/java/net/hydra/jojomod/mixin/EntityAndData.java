package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityAndData implements IEntityAndData {

    @Unique
    private float roundabout$PrevTick = 0;

    @Unique
    private double roundabout$PrevX = 0;
    @Unique
    private double roundabout$PrevY = 0;
    @Unique
    private double roundabout$PrevZ = 0;

    @Unique
    public @Nullable ItemStack roundabout$RenderChest;
    @Unique
    public @Nullable ItemStack roundabout$RenderLegs;
    @Unique
    public @Nullable ItemStack roundabout$RenderBoots;
    @Unique
    public @Nullable ItemStack roundabout$RenderHead;
    @Unique
    public @Nullable ItemStack roundabout$RenderMainHand;
    @Unique
    public @Nullable ItemStack roundabout$RenderOffHand;

    public void roundabout$setRoundaboutRenderChest(@Nullable ItemStack chest){
        this.roundabout$RenderChest = chest;
    }
    public void roundabout$setRoundaboutRenderLegs(@Nullable ItemStack legs){
        this.roundabout$RenderLegs = legs;
    }
    public void roundabout$setRoundaboutRenderBoots(@Nullable ItemStack boots){
        this.roundabout$RenderBoots = boots;
    }
    public void roundabout$setRoundaboutRenderHead(@Nullable ItemStack head){
        this.roundabout$RenderHead = head;
    }
    public void roundabout$setRoundaboutRenderMainHand(@Nullable ItemStack mainhand){
        this.roundabout$RenderMainHand = mainhand;
    }
    public void roundabout$setRoundaboutRenderOffHand(@Nullable ItemStack offhand){
        this.roundabout$RenderOffHand = offhand;
    }

    public @Nullable ItemStack roundabout$getRoundaboutRenderChest(){
        return this.roundabout$RenderChest;
    }
    public @Nullable ItemStack roundabout$getRoundaboutRenderLegs(){
        return this.roundabout$RenderLegs;
    }
    public @Nullable ItemStack roundabout$getRoundaboutRenderBoots(){
        return this.roundabout$RenderBoots;
    }
    public @Nullable ItemStack roundabout$getRoundaboutRenderHead(){
        return this.roundabout$RenderHead;
    }
    public @Nullable ItemStack roundabout$getRoundaboutRenderMainHand(){
        return this.roundabout$RenderMainHand;
    }
    public @Nullable ItemStack roundabout$getRoundaboutRenderOffHand(){
        return this.roundabout$RenderOffHand;
    }

    public void roundabout$setRoundaboutPrevX(double roundaboutPrevX){
        this.roundabout$PrevX = roundaboutPrevX;
    }
    public void roundabout$setRoundaboutPrevY(double roundaboutPrevY){
        this.roundabout$PrevY = roundaboutPrevY;
    }
    public void roundabout$setRoundaboutPrevZ(double roundaboutPrevZ){
        this.roundabout$PrevZ = roundaboutPrevZ;
    }
    public double roundabout$getRoundaboutPrevX(){
        return this.roundabout$PrevX;
    }
    public double roundabout$getRoundaboutPrevY(){
        return this.roundabout$PrevY;
    }
    public double roundabout$getRoundaboutPrevZ(){
        return this.roundabout$PrevZ;
    }

    @Shadow
    private int remainingFireTicks;

    @Shadow
    @Final
    public double getX() {
        return 0;
    }

    @Shadow
    @Final
    public double getY() {
        return 0;
    }
    @Shadow
    @Final
    public double getZ() {
        return 0;
    }

    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    public void roundabout$Turn(double $$0, double $$1, CallbackInfo ci){
        if (((TimeStop) ((Entity) (Object) this).level()).CanTimeStopEntity(((Entity) (Object) this))){
            ci.cancel();
        }
    }


    @Shadow
    public Level level() {
        return null;
    }
    @Shadow
    @Final
    public boolean isRemoved() {
        return false;
    }
    @Inject(method = "changeDimension", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$changeDim(ServerLevel $$0, CallbackInfoReturnable<Boolean> ci) {
        if (((Entity)(Object)this) instanceof LivingEntity LE){
            if (this.level() instanceof ServerLevel && !this.isRemoved()) {
                 if (((StandUser)this).roundabout$getStand() != null){
                     StandEntity stand = ((StandUser)this).roundabout$getStand();
                     if (!stand.getHeldItem().isEmpty()) {
                         if (stand.canAcquireHeldItem) {
                             double $$3 = stand.getEyeY() - 0.3F;
                             ItemEntity $$4 = new ItemEntity(this.level(), stand.getX(), $$3, stand.getZ(), stand.getHeldItem().copy());
                             $$4.setPickUpDelay(40);
                             $$4.setThrower(stand.getUUID());
                             this.level().addFreshEntity($$4);
                             stand.setHeldItem(ItemStack.EMPTY);
                         }
                     }
                 }
            }
        }
    }

    @Override
    public float roundabout$getPreTSTick() {
        return this.roundabout$PrevTick;
    }

    @Override
    public void roundabout$setPreTSTick(float frameTime) {
        roundabout$PrevTick = frameTime;
    }
    @Override
    public void roundabout$resetPreTSTick() {
        roundabout$PrevTick = 0;
    }


    /**In a timestop, fire doesn't tick*/
    @Inject(method = "setRemainingFireTicks", at = @At("HEAD"), cancellable = true)
    protected void roundabout$SetFireTicks(int $$0, CallbackInfo ci){
        Entity entity = ((Entity)(Object) this);
        if (entity instanceof LivingEntity && !((TimeStop)entity.level()).getTimeStoppingEntities().isEmpty()
                && ((TimeStop)entity.level()).getTimeStoppingEntities().contains(entity)){
            ci.cancel();
        }
    }
    @Inject(method = "clearFire", at = @At("HEAD"), cancellable = true)
    protected void roundabout$ClearFire(CallbackInfo ci){
        Entity entity = ((Entity)(Object) this);
        if (entity instanceof LivingEntity && !((TimeStop)entity.level()).getTimeStoppingEntities().isEmpty()
                && ((TimeStop)entity.level()).getTimeStoppingEntities().contains(entity)){
            this.remainingFireTicks = 0;
        }
    }

    @Shadow
    public boolean isShiftKeyDown() {
        return false;
    }
    @Inject(method = "canRide", at = @At("HEAD"), cancellable = true)
    protected void roundabout$canRide(Entity $$0, CallbackInfoReturnable<Boolean> cir){
        if ($$0 instanceof StandEntity && (!(((Entity)(Object)this) instanceof LivingEntity) || ((TimeStop) ((Entity) (Object) this).level()).CanTimeStopEntity(((Entity) (Object) this)))){
            cir.setReturnValue(!this.isShiftKeyDown());
        }
    }
    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z", at = @At("HEAD"), cancellable = true)
    protected void roundabout$startRiding(Entity $$0, boolean $$1, CallbackInfoReturnable<Boolean> cir){
        if (((Entity)(Object)this) instanceof LivingEntity LE
                && ((StandUser)LE).roundabout$getStand() != null
                && ($$0.getRootVehicle().is(((StandUser)LE).roundabout$getStand()) || $$0.getRootVehicle().hasPassenger(((StandUser)LE).roundabout$getStand()))){
            cir.setReturnValue(false);
        }
    }



    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"),cancellable = true)
    protected void roundabout$push(Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity le && ((StandUser) le).roundabout$getStandPowers().cancelCollision(((Entity)(Object)this))) {
            ci.cancel();
        }
    }


    @Shadow
    private Vec3 deltaMovement;

    @Shadow public abstract void moveTo(BlockPos $$0, float $$1, float $$2);

    @Shadow public abstract void moveTo(double $$0, double $$1, double $$2);

    @Unique
    private Vec3 roundabout$DeltaBuildupTS = new Vec3(0,0,0);

    @Unique
    public Vec3 roundabout$getRoundaboutDeltaBuildupTS(){
        return this.roundabout$DeltaBuildupTS;
    }

    @Unique
    public void roundabout$setRoundaboutDeltaBuildupTS(Vec3 vec3){
        if (vec3 != null) {
            this.roundabout$DeltaBuildupTS = vec3;
        }
    }
    @Inject(method = "setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"), cancellable = true)
    protected void roundabout$SetDeltaMovement(Vec3 vec3, CallbackInfo ci){
        if (((TimeStop) ((Entity) (Object) this).level()).CanTimeStopEntity(((Entity) (Object) this))){
            if (vec3.distanceTo(new Vec3(0,0,0)) > (roundabout$DeltaBuildupTS.distanceTo(new Vec3(0,0,0)) - 0.35)) {
                this.roundabout$DeltaBuildupTS = vec3;
            }
            ci.cancel();
        }
    }

    @Unique
    public boolean roundabout$jamBreath = false;
    @Unique
    public void roundabout$setRoundaboutJamBreath(boolean roundaboutJamBreath){
        this.roundabout$jamBreath = roundaboutJamBreath;
    }
    @Unique
    public boolean roundabout$getRoundaboutJamBreath(){
        return this.roundabout$jamBreath;
    }

    @Inject(method = "setAirSupply", at = @At("HEAD"), cancellable = true)
    public void roundabout$SetAirSupply(int $$0, CallbackInfo ci) {
        if (roundabout$jamBreath){
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
    protected void roundabout$tick(CallbackInfo ci) {
        roundabout$tickQVec();
    }

    @Unique
    @Override
    public void roundabout$tickQVec(){
        if (!roundabout$qknockback2params.equals(Vec3.ZERO)){
            if (((Entity)(Object)this) instanceof LivingEntity le){
                le.teleportTo(roundabout$qknockback2params.x,roundabout$qknockback2params.y,roundabout$qknockback2params.z);
            } else {
                this.moveTo(roundabout$qknockback2params.x,roundabout$qknockback2params.y,roundabout$qknockback2params.z);
            }
            roundabout$qknockback2params =Vec3.ZERO;
        }
        if (!roundabout$qknockback.equals(Vec3.ZERO)){
            MainUtil.takeUnresistableKnockbackWithYBias(((Entity)(Object)this), roundabout$qknockbackparams.x,
                    roundabout$qknockback.x,
                    roundabout$qknockback.y,
                    roundabout$qknockback.z,
                    (float)roundabout$qknockbackparams.y);
            roundabout$setQVec(Vec3.ZERO);
        }
    }
    @Unique
    private Vec3 roundabout$qknockback = Vec3.ZERO;
    @Unique
    private Vec3 roundabout$qknockbackparams = Vec3.ZERO;
    @Unique
    private Vec3 roundabout$qknockback2params = Vec3.ZERO;
    @Unique
    @Override
    public void roundabout$setQVec(Vec3 ec){
        roundabout$qknockback = ec;
    }
    @Unique
    @Override
    public void roundabout$setQVecParams(Vec3 ec){
        roundabout$qknockbackparams = ec;
    }
    @Unique
    @Override
    public void roundabout$setQVec2Params(Vec3 ec){
        roundabout$qknockback2params = ec;
    }


}
