package org.trotu.phantomtoggle.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import org.trotu.phantomtoggle.PhantomToggleMod;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PhantomCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("phantom")
                .requires(source -> source.hasPermissionLevel(0) && source.getEntity() instanceof ServerPlayerEntity) // Only players can use this command
                .then(CommandManager.literal("toggle")
                    .executes(PhantomCommand::executeToggle)
                )
                .then(CommandManager.literal("status")
                    .executes(PhantomCommand::executeStatus)
                )
        );
    }
    
    private static int executeToggle(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity(); // Safe as we checked in requires()
        
        boolean newState = PhantomToggleMod.getDatabaseManager().togglePhantomSpawns(player.getUuid());
        
        if (newState) {
            source.sendFeedback(() -> Text.literal("Phantom spawning enabled for you."), false);
        } else {
            source.sendFeedback(() -> Text.literal("Phantom spawning disabled for you."), false);
        }
        
        return 1;
    }
    
    private static int executeStatus(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity(); // Safe as we checked in requires()
        
        boolean enabled = PhantomToggleMod.getDatabaseManager().arePhantomSpawnsEnabled(player.getUuid());
        
        if (enabled) {
            source.sendFeedback(() -> Text.literal("Phantom spawning is currently enabled for you."), false);
        } else {
            source.sendFeedback(() -> Text.literal("Phantom spawning is currently disabled for you."), false);
        }
        
        return 1;
    }
}