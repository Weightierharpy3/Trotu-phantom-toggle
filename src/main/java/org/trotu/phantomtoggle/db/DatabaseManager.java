package org.trotu.phantomtoggle.db;

import org.trotu.phantomtoggle.PhantomToggleMod;
import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseManager {
    private final String DB_URL = "jdbc:h2:./config/phantom-toggle/phantom-toggle-db;AUTO_SERVER=TRUE";
    private Connection connection;
    private final Map<UUID, Boolean> playerPhantomsEnabledCache = new ConcurrentHashMap<>();
    
    public void initialize() {
        try {
            // Create the config directory if it doesn't exist
            File configDir = new File("./config/phantom-toggle");
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            
            // Connect to the database
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DB_URL);
            
            // Create the table if it doesn't exist
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS player_phantom_settings (" +
                    "player_uuid VARCHAR(36) PRIMARY KEY, " +
                    "phantoms_enabled BOOLEAN DEFAULT TRUE" +
                    ")"
                );
            }
            
            PhantomToggleMod.LOGGER.info("Database initialized successfully");
            
            // Load the cache with existing data
            loadCache();
        } catch (Exception e) {
            PhantomToggleMod.LOGGER.error("Failed to initialize database", e);
        }
    }
    
    private void loadCache() {
        playerPhantomsEnabledCache.clear();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT player_uuid, phantoms_enabled FROM player_phantom_settings")) {
            
            while (rs.next()) {
                UUID playerUuid = UUID.fromString(rs.getString("player_uuid"));
                boolean phantomsEnabled = rs.getBoolean("phantoms_enabled");
                playerPhantomsEnabledCache.put(playerUuid, phantomsEnabled);
            }
            
            PhantomToggleMod.LOGGER.info("Loaded {} player settings into cache", playerPhantomsEnabledCache.size());
        } catch (SQLException e) {
            PhantomToggleMod.LOGGER.error("Failed to load player settings cache", e);
        }
    }
    
    public boolean arePhantomSpawnsEnabled(UUID playerUuid) {
        return playerPhantomsEnabledCache.getOrDefault(playerUuid, true);
    }
    
    public boolean togglePhantomSpawns(UUID playerUuid) {
        boolean currentValue = arePhantomSpawnsEnabled(playerUuid);
        boolean newValue = !currentValue;
        
        try (PreparedStatement pstmt = connection.prepareStatement(
                "MERGE INTO player_phantom_settings (player_uuid, phantoms_enabled) KEY(player_uuid) VALUES (?, ?)")) {
            pstmt.setString(1, playerUuid.toString());
            pstmt.setBoolean(2, newValue);
            pstmt.executeUpdate();
            
            // Update the cache
            playerPhantomsEnabledCache.put(playerUuid, newValue);
            return newValue;
        } catch (SQLException e) {
            PhantomToggleMod.LOGGER.error("Failed to toggle phantom spawns for player {}", playerUuid, e);
            return currentValue;
        }
    }
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            PhantomToggleMod.LOGGER.error("Failed to close database connection", e);
        }
    }
}