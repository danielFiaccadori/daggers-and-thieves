package net.dndats.daggersandthieves.common.network.packets;

import net.dndats.daggersandthieves.DaggersAndThieves;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record C2S_PacketServerPickpocketExecute() implements CustomPacketPayload {

    public static final Type<C2S_PacketServerPickpocketExecute> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(DaggersAndThieves.MODID, "server_bash_execute"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2S_PacketServerPickpocketExecute> STREAM_CODEC =
            StreamCodec.of((RegistryFriendlyByteBuf buffer, C2S_PacketServerPickpocketExecute message) -> {
            }, (RegistryFriendlyByteBuf buffer) -> new C2S_PacketServerPickpocketExecute());

    public static void handleData(final C2S_PacketServerPickpocketExecute message, final IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                // Add logic here
            }).exceptionally(e -> {
                context.connection().disconnect(net.minecraft.network.chat.Component.literal(e.getMessage()));
                return null;
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
