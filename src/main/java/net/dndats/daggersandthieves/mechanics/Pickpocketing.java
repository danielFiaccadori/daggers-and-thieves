package net.dndats.daggersandthieves.mechanics;

import net.dndats.api.helper.StealthHelper;
import net.dndats.daggersandthieves.DaggersAndThieves;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@EventBusSubscriber(modid = DaggersAndThieves.MODID)
public class Pickpocketing {

    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
//        Player player = event.getEntity();
//        if (player.level().isClientSide) return;
//
//        if (player.isCrouching()) {
//            if (!StealthHelper.isStealthy(player)) {
//                return;
//            }
//
//            if (!(event.getTarget() instanceof LivingEntity target)) {
//                return;
//            }
//
//            if (!(target instanceof InventoryCarrier carrier)) return;
//
//            event.setCanceled(true);
//
//            SimpleContainer inventory = carrier.getInventory();
//
//            List<Integer> nonEmptySlots = new ArrayList<>();
//            for (int i = 0; i < inventory.getContainerSize(); i++) {
//                player.sendSystemMessage(Component.literal("Slot " + i + ": " + inventory.getItem(i).getDisplayName()));
//                if (!inventory.getItem(i).isEmpty()) {
//                    nonEmptySlots.add(i);
//                }
//            }
//
//            if (nonEmptySlots.isEmpty()) return;
//
//            int randomSlotIndex = nonEmptySlots.get(RANDOM.nextInt(nonEmptySlots.size()));
//            ItemStack stolen = inventory.getItem(randomSlotIndex).copy();
//            stolen.setCount(1);
//
//            inventory.removeItem(randomSlotIndex, 1);
//
//            ItemHandlerHelper.giveItemToPlayer(player, stolen);
//
//            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, 1.5F);
//        }
    }

}
