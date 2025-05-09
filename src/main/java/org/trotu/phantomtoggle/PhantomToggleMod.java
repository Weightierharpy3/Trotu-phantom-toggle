package org.trotu.phantomtoggle;

import org.trotu.phantomtoggle.command.PhantomCommand;
import org.trotu.phantomtoggle.command.VersionCommand;
import org.trotu.phantomtoggle.db.DatabaseManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhantomToggleMod implements ModInitializer {
    public static final String MOD_ID = "phantom-toggle";
    public static final String MOD_VERSION = "1.0.0";
    public static final String MOD_CREATOR = "Weightierharpy3";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static DatabaseManager databaseManager;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Phantom Toggle Mod");
        
        // Initialize the database
        databaseManager = new DatabaseManager();
        databaseManager.initialize();
        
        // Register commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            PhantomCommand.register(dispatcher);
            VersionCommand.register(dispatcher);
        });
        
        // Register server stop event to close the database connection
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            LOGGER.info("Server stopping, closing database connection");
            if (databaseManager != null) {
                databaseManager.close();
            }
        });
    }
    
    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}