package org.trotu.phantomtoggle.mixin;

import org.trotu.phantomtoggle.PhantomToggleMod;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {

    /**
     * Injects at the beginning of the spawn method to prevent phantom spawning for players who disabled it
     */
    @Inject(method = "spawn", at = @At("HEAD"))
    private void onSpawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals, CallbackInfo ci) {
        // Only proceed if monster spawning is enabled
        if (!spawnMonsters) {
            return;
        }
        
        // Check if the insomnia game rule is enabled
        if (world.getGameRules().getBoolean(net.minecraft.world.GameRules.DO_INSOMNIA)) {
            // For each player, if they've disabled phantoms, reset their insomnia timer
            for (ServerPlayerEntity player : world.getPlayers()) {
                if (!PhantomToggleMod.getDatabaseManager().arePhantomSpawnsEnabled(player.getUuid())) {
                    // Reset the player's stats.timeSinceRest statistic to prevent phantoms from spawning
                    // This prevents phantoms from spawning for players who have disabled them
                    player.resetStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST));
                }
            }
        }
    }
}