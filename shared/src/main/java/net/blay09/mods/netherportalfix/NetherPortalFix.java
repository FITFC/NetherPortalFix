package net.blay09.mods.netherportalfix;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.PlayerChangedDimensionEvent;
import net.blay09.mods.netherportalfix.mixin.LivingEntityAccessor;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetherPortalFix {

    public static final String MOD_ID = "netherportalfix";

    public static final Logger logger = LogManager.getLogger();

    public static void initialize() {
        Balm.getEvents().onEvent(PlayerChangedDimensionEvent.class, event -> {
            final ServerPlayer player = event.getPlayer();
            final ResourceKey<Level> fromDim = event.getFromDim();
            final ResourceKey<Level> toDim = event.getToDim();
            final ResourceKey<Level> OVERWORLD = Level.OVERWORLD;
            final ResourceKey<Level> THE_NETHER = Level.NETHER;
            if ((fromDim == OVERWORLD && toDim == THE_NETHER) || (fromDim == THE_NETHER && toDim == OVERWORLD)) {
                BlockUtil.FoundRectangle fromPortal = ReturnPortalManager.findPortalAt(player, fromDim, ((LivingEntityAccessor) player).getLastPos());
                BlockPos toPos = player.blockPosition();
                if (fromPortal != null) {
                    ReturnPortalManager.storeReturnPortal(player, toDim, toPos, fromPortal);
                    NetherPortalFix.logger.info("Storing return portal from {} to {} in {}", toDim, fromPortal.minCorner, fromDim);
                } else {
                    NetherPortalFix.logger.info("Not storing return portal because I'm not in a portal.");
                }
            } else {
                NetherPortalFix.logger.info("Not storing return portal because it's from {} to {}", fromDim, toDim);
            }
        });
    }

}
