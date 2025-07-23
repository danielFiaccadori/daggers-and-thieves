package net.dndats.daggersandthieves.mechanics.pickpocketing;

import net.dndats.daggersandthieves.DaggersAndThieves;
import net.dndats.daggersandthieves.common.network.packets.C2S_PacketServerPickpocketExecute;
import net.dndats.daggersandthieves.helper.PickpocketingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

@EventBusSubscriber(modid = DaggersAndThieves.MODID, value = Dist.CLIENT)
public class PickpocketingClientHandler {

    @SubscribeEvent
    public static void updatePickpocketingProgress(ClientTickEvent.Pre event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        if (Minecraft.getInstance().options.keyAttack.isDown()) {
            Optional<LivingEntity> victim = PickpocketingHelper.getCurrentPickpocketing(player);

            if (victim.isPresent() && PickpocketingHelper.isInsidePickpocketRange(victim.get(), player)) {
                PickpocketingClientManager.incrementPickpocketingProgress(victim.get(), 1);

                if (PickpocketingClientManager.hasFinishedPickpocketing(victim.get())) {
                    PacketDistributor.sendToServer(new C2S_PacketServerPickpocketExecute());
                }
            }
        }
    }

}
