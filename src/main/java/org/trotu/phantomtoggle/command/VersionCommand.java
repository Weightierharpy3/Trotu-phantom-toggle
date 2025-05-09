package org.trotu.phantomtoggle.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import org.trotu.phantomtoggle.PhantomToggleMod;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class VersionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("phantom")
                .then(CommandManager.literal("version")
                    .executes(VersionCommand::executeVersion)
                )
                .then(CommandManager.literal("creator")
                    .executes(VersionCommand::executeCreator)
                )
                .then(CommandManager.literal("info")
                    .executes(VersionCommand::executeInfo)
                )
        );
    }
    
    private static int executeVersion(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        source.sendFeedback(() -> Text.literal("Phantom Toggle Mod - Version: " + PhantomToggleMod.MOD_VERSION)
            .formatted(Formatting.GREEN), false);
        return 1;
    }
    
    private static int executeCreator(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        source.sendFeedback(() -> Text.literal("Phantom Toggle Mod - Created by: " + PhantomToggleMod.MOD_CREATOR)
            .formatted(Formatting.GOLD), false);
        return 1;
    }
    
    private static int executeInfo(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        source.sendFeedback(() -> Text.literal("Phantom Toggle Mod - Version: " + PhantomToggleMod.MOD_VERSION)
            .formatted(Formatting.GREEN), false);
        source.sendFeedback(() -> Text.literal("Created by: " + PhantomToggleMod.MOD_CREATOR)
            .formatted(Formatting.GOLD), false);
        source.sendFeedback(() -> Text.literal("Use /phantom toggle to toggle phantom spawning for yourself")
            .formatted(Formatting.AQUA), false);
        return 1;
    }
}