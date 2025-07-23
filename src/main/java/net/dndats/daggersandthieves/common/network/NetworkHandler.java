package net.dndats.daggersandthieves.common.network;

import net.dndats.daggersandthieves.DaggersAndThieves;
import net.dndats.daggersandthieves.common.network.packets.C2S_PacketServerPickpocketExecute;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = DaggersAndThieves.MODID)
public class NetworkHandler {

    private static final Map<CustomPacketPayload.Type<?>, NetworkMessage<?>> MESSAGES = new HashMap<>();
    private static boolean networkingRegistered = false;

    private static <T extends CustomPacketPayload> void addPayload(CustomPacketPayload.Type<T> type, StreamCodec<? extends FriendlyByteBuf, T> codec, IPayloadHandler<T> handler, NetworkDirection direction) {
        if (networkingRegistered) {
            throw new IllegalStateException("Cannot register new payload handlers after networking has been registered");
        }
        MESSAGES.put(type, new NetworkMessage<>(codec, handler, direction));
    }

    public static void register() {
        // Register S2C packets (empty)

        // Register C2S packets
        addPayload(C2S_PacketServerPickpocketExecute.TYPE, C2S_PacketServerPickpocketExecute.STREAM_CODEC, C2S_PacketServerPickpocketExecute::handleData, NetworkDirection.TO_SERVER);

        // Register BIDIRECTIONAL packets  (empty)
    }

    @SubscribeEvent
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void onRegisterPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(DaggersAndThieves.MODID);

        MESSAGES.forEach(((type, networkMessage) -> {
            switch ((networkMessage).direction()) {
                case TO_CLIENT -> registrar.playToClient(type, ((NetworkMessage) networkMessage).reader(), ((NetworkMessage) networkMessage).handler());
                case TO_SERVER -> registrar.playToServer(type, ((NetworkMessage) networkMessage).reader(), ((NetworkMessage) networkMessage).handler());
                case BIDIRECTIONAL -> registrar.playBidirectional(type, ((NetworkMessage) networkMessage).reader(), ((NetworkMessage) networkMessage).handler());
            }
        }));

        networkingRegistered = true;
    }

    private record NetworkMessage<T extends CustomPacketPayload>(StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler,
                                                                 NetworkDirection direction) {}

}
