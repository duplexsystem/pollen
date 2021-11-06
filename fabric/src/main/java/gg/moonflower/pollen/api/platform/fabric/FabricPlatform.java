package gg.moonflower.pollen.api.platform.fabric;

import gg.moonflower.pollen.api.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class FabricPlatform extends Platform {

    FabricPlatform(String modId, Runnable commonInit, Runnable clientInit, Runnable commonPostInit, Runnable clientPostInit, Runnable commonNetworkInit, Runnable clientNetworkInit) {
        super(modId);
        commonInit.run();
        commonPostInit.run();
        commonNetworkInit.run();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            clientInit.run();
            clientPostInit.run();
            clientNetworkInit.run();
        }
    }
}
