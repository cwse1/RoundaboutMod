package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.access.IMinecartTNT;
import net.hydra.jojomod.block.GasolineBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownObjectEntity extends ThrowableItemProjectile {
    private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(ThrownObjectEntity.class, EntityDataSerializers.ITEM_STACK);
    public final boolean places;

    public ThrownObjectEntity(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
        this.places = false;
    }

    public ThrownObjectEntity(LivingEntity living, Level $$1) {
        super(ModEntities.THROWN_OBJECT, living, $$1);
        places = false;
    }

    public ThrownObjectEntity(LivingEntity living, Level $$1, ItemStack itemStack, boolean places) {
        super(ModEntities.THROWN_OBJECT, living, $$1);
        this.entityData.set(ITEM_STACK, itemStack);
        this.places = places;
    }

    public ThrownObjectEntity(Level world, double p_36862_, double p_36863_, double p_36864_, ItemStack itemStack, boolean places) {
        super(ModEntities.THROWN_OBJECT, p_36862_, p_36863_, p_36864_, world);
        this.entityData.set(ITEM_STACK, itemStack);
        this.places = places;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ITEM_STACK, ItemStack.EMPTY);
    }
    @Override
    protected Item getDefaultItem() {
        return this.entityData.get(ITEM_STACK).getItem();
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);


        if (!this.level().isClientSide) {
            BlockPos pos = $$0.getBlockPos().relative($$0.getDirection());
            BlockState state = this.level().getBlockState(pos);
            if (this.places && (state.isAir() || state.canBeReplaced()) && !((this.getOwner() instanceof Player &&
                    (((Player) this.getOwner()).blockActionRestricted(this.getOwner().level(), pos, ((ServerPlayer)
                            this.getOwner()).gameMode.getGameModeForPlayer()))) ||
                    !this.getOwner().level().mayInteract(((Player) this.getOwner()), pos))){

                this.level().setBlockAndUpdate(pos, ((BlockItem)this.getDefaultItem()).getBlock().defaultBlockState());
            }
        }
        this.discard();
    }


    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        Entity $$1 = $$0.getEntity();
        float $$2 = 2.0f;

        Entity $$4 = this.getOwner();

        DamageSource $$5 = ModDamageTypes.of($$1.level(), ModDamageTypes.MATCH, $$4);

        SoundEvent $$6 = SoundEvents.FIRE_EXTINGUISH;
        Vec3 DM = $$1.getDeltaMovement();
        if ($$1 instanceof Creeper) {
            ((Creeper)$$1).ignite();
        } if ($$1.getType() == EntityType.TNT_MINECART) {
            DamageSource DS = $$1.damageSources().explosion($$1, $$0.getEntity());
            ((IMinecartTNT)$$1).roundabout$explode(DS, this.getDeltaMovement().lengthSqr());
            this.discard();
        } else if ($$1.hurt($$5, $$2)) {
            if ($$1.getType() == EntityType.ENDERMAN) {
                return;
            }

            if ($$1 instanceof LivingEntity $$7) {
                $$1.setDeltaMovement($$1.getDeltaMovement().multiply(0.4,0.4,0.4));
            }
            this.playSound($$6, 0.8F, 1.6F);
            this.discard();
        }

    }


    public void shootWithVariance(double $$0, double $$1, double $$2, float $$3, float $$4) {
        Vec3 $$5 = new Vec3($$0, $$1, $$2)
                .normalize()
                .add(
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4)
                )
                .scale((double)$$3);
        this.setDeltaMovement($$5);
        double $$6 = $$5.horizontalDistance();
        this.setYRot((float)(Mth.atan2($$5.x, $$5.z) * 180.0F / (float)Math.PI));
        this.setXRot((float)(Mth.atan2($$5.y, $$6) * 180.0F / (float)Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }


    public void shootFromRotationWithVariance(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shootWithVariance((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add($$9.x, $$0.onGround() ? 0.0 : $$9.y, $$9.z));
    }
}