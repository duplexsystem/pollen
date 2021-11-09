package gg.moonflower.pollen.core.network;

import gg.moonflower.pollen.api.network.message.PollinatedPacketContext;
import gg.moonflower.pollen.core.network.play.ClientboundSyncAnimationPacket;
import gg.moonflower.pollen.core.network.play.PollenClientPlayPacketHandler;
import gg.moonflower.pollen.pinwheel.api.common.animation.AnimatedEntity;
import gg.moonflower.pollen.pinwheel.api.common.animation.AnimationState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author Ocelot
 */
@ApiStatus.Internal
public class PollenClientPlayPacketHandlerImpl implements PollenClientPlayPacketHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void handleSyncAnimationPacket(ClientboundSyncAnimationPacket msg, PollinatedPacketContext ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null)
            return;

        ctx.enqueueWork(() ->
        {
            Entity e = level.getEntity(msg.getEntityId());
            if (!(e instanceof AnimatedEntity)) {
                LOGGER.warn("Server sent animation for entity: " + e + ", but it is not an instance of AnimatedEntity");
                return;
            }

            AnimatedEntity entity = (AnimatedEntity) e;

            int animationId = msg.getAnimationId();
            if (animationId == -1) {
                entity.resetAnimationState();
                return;
            }

            AnimationState[] animations = entity.getAnimationStates();
            if (animationId < 0 || animationId >= animations.length) {
                LOGGER.warn("Server sent invalid animation " + animationId + " for entity: " + e);
                return;
            }

            entity.setAnimationState(animations[animationId]);
        });
    }
}
