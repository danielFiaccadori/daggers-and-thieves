package net.dndats.daggersandthieves.common.network.packets;

import net.dndats.daggersandthieves.DaggersAndThieves;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record C2S_PacketServerPickpocketExecute(int target) implements CustomPacketPayload {

    public static final Type<C2S_PacketServerPickpocketExecute> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(DaggersAndThieves.MODID, "server_pickpocket_execute"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2S_PacketServerPickpocketExecute> STREAM_CODEC = StreamCodec
            .of((RegistryFriendlyByteBuf buffer, C2S_PacketServerPickpocketExecute packet) -> {
                buffer.writeInt(packet.target);
            }, (RegistryFriendlyByteBuf buffer) -> {
                int id = buffer.readInt();
                return new C2S_PacketServerPickpocketExecute(id);
            });

    public static void handleData(final C2S_PacketServerPickpocketExecute message, final IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                Player thief = context.player();
                Entity victim = thief.level().getEntity(message.target());

                if (victim instanceof LivingEntity) {
                    if (thief.getMainHandItem().isEmpty()) {
                        thief.setItemSlot(EquipmentSlot.MAINHAND, Items.EMERALD.getDefaultInstance());
                    }
                }

            }).exceptionally(e -> {
                context.connection().disconnect(Component.literal(e.getMessage()));
                return null;
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
