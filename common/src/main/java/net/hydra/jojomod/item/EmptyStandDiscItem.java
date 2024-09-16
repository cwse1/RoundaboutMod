package net.hydra.jojomod.item;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EmptyStandDiscItem extends Item {
    public EmptyStandDiscItem(Properties $$0) {
        super($$0);
    }

    public void generateStandPowers(LivingEntity entity){
        ((StandUser)entity).setStandPowers(null);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if (!$$0.isClientSide) {
            ItemStack currentDisc = ((StandUser) $$1).roundabout$getStandDisc();
            if (!currentDisc.isEmpty()) {
                $$3.shrink(1);
                ((StandUser) $$1).setStand(null);
                ((StandUser) $$1).setActive(false);
                ((StandUser) $$1).roundabout$setStandDisc(ItemStack.EMPTY);
                this.generateStandPowers($$1);
                addItem($$1, currentDisc.copy());
            }
        }
        return InteractionResultHolder.consume($$3);
    }

    public void addItem(Player player, ItemStack stack){
        if (canAddItem(stack, player.getInventory())) {
            player.addItem(stack);
        } else {
            ItemEntity $$4 = new ItemEntity(player.level(), player.getX(),
                    player.getY() + player.getEyeHeight(), player.getZ(),
                    stack);
            $$4.setPickUpDelay(40);
            $$4.setThrower(player.getUUID());
            player.level().addFreshEntity($$4);
        }
    }
    public boolean canAddItem(ItemStack itemStack, Inventory inventory) {
        boolean bl = false;
        for (ItemStack itemStack2 : inventory.items) {
            if (!itemStack2.isEmpty() && (!ItemStack.isSameItemSameTags(itemStack2, itemStack) || itemStack2.getCount() >= itemStack2.getMaxStackSize())) continue;
            bl = true;
            break;
        }
        return bl;
    }
}